package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class RecipeDetails extends GlobalActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    /*AIXO PAL POSTMAN
    {
    "id": 2,
    "name": "test",
    "instructions": "todo",
    "calories": "10.0",
    "sodium": "10.0",
    "fat": "10.0",
    "protein": "10.0",
    "urlPhoto": "jajajaXD",
    "averagePuntuation": "3.0"
}
     */

    ImageView shoppingListIcon;
    String recipeName;
    RecipeDetails.UrlConnectorUpdateRating ur;

    boolean ratingChanged = false;

    RatingBar ratingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        shoppingListIcon = findViewById(R.id.recipe_shopping_list_icon);
        shoppingListIcon.setOnClickListener(this);

        TextView tv = findViewById(R.id.dish_detail_name);
        TextView ingredients = findViewById(R.id.textView7);
        TextView instructions = findViewById(R.id.steps_recipe_detail);
        recipeName = (String) tv.getText();

        ur = new RecipeDetails.UrlConnectorUpdateRating();
        //ur.setRecipeName(recipeName);
        //ur.setRecipeName("Boudin Blanc Terrine with Red Onion Confit ");
        ur.execute();

        ratingBar = findViewById(R.id.recipeRatingBar);
        while(!ur.loaded){}
        ratingBar.setRating(ur.getRating());
        JSONObject recipe = ur.getRecipe();

        try {
            tv.setText(recipe.getString("name"));
            ingredients.setText(recipe.getString("proportions"));
            instructions.setText(recipe.getString("instructions"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ratingBar.setOnRatingBarChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()){
            case R.id.recipe_shopping_list_icon:
                intent = new Intent(RecipeDetails.this, ShoppingListActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        vibrate();
        ratingChanged = true;
        ur.updateRating(v);
    }

    @Override
    public boolean onSupportNavigateUp() {
        vibrate();
        if(ratingChanged){
            ur = new RecipeDetails.UrlConnectorUpdateRating();
            ur.setRecipeName("Boudin Blanc Terrine with Red Onion Confit ");
            ur.updateRating(ratingBar.getRating());
            ur.execute();
        }

        finish();
        return true;
    }

    // Async + thread, class to make the connection to the server
    private class UrlConnectorUpdateRating extends AsyncTask<Void, Void, Void> {

        public boolean loaded = false;

        public String recipeName = "";

        public void setRecipeName(String recipeName) {
            this.recipeName = recipeName;
        }
        public JSONObject getRecipe(){ return thisRecipe;}

        private JSONObject thisRecipe;
        private JSONObject user;

        HttpURLConnection conn;

        float recipeRating;
        float getRating(){
            return recipeRating;
        }
        void updateRating(float r){
            recipeRating = r;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                //GET ACTUAL USER ID
                URL url = new URL("http://" + ipserver + "/api/resources/users/?username=" + settings.getString("UserMail", ""));
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                int userID = -1;
                System.out.println("BUSCANT USUARI");
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    System.out.println("REDLINE");
                    JSONArray arr = new JSONArray(output);
                    System.out.println("ARRRRRRRRRR + OUTPUT: " + output);
                    user = arr.getJSONObject(0);
                    System.out.println("JSON_OBJECT");
                    userID = user.getInt("id");
                    System.out.println("USER: " + user);
                    br.close();
                } else {
                    System.out.println("COULD NOT FIND USER");
                    return null;
                }

                conn.disconnect();
                if (userID == -1) {
                    System.out.println("USER NOT EXISTS");
                    return null;
                }

                //GET RECIPE
                Random r = new Random();
                url = new URL("http://" + ipserver  + "/api/resources/recipes/id/" + r.nextInt(1000));
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                //conn.connect();

                System.out.println("OLA K ASE");

                if(conn.getResponseCode() == 200) {
                    InputStreamReader inp = new InputStreamReader(conn.getInputStream());
                    BufferedReader br = new BufferedReader(inp);
                    //while(br.ready()) {
                        String output = br.readLine();
                        //JSONArray arr = new JSONArray(output);
                        JSONObject obj = new JSONObject(output);
                        System.out.println("RECIPE: " + obj);
                        recipeRating = (float) obj.getDouble("averagePuntuation");
                        thisRecipe = obj;
                        System.out.println("THIS RECIPE IS: " + thisRecipe.getString("name"));

                    inp.close();
                    br.close();
                }else{
                    System.out.println("COULD NOT FIND RECIPES");
                    return null;
                }

                //PUT
                try{
                    url = new URL("http://" + ipserver  + "/api/resources/usersRecipes/");
                    System.out.println(url);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);


                    OutputStream os = conn.getOutputStream();

                    JSONObject subjson = new JSONObject()
                            .put("userId", user.getInt("id"))
                            .put("recipeId", thisRecipe.getInt("id"));

                    String jsonString = new JSONObject()
                            .put("key", subjson)
                            .put("puntuation",2 )
                            .toString();
                    System.out.println("daosdnsa " + jsonString);

                    os.write(jsonString.getBytes());
                    os.flush();
                    os.close();


                    System.out.println("CONNECTION CODE: " + conn.getResponseCode());
                    conn.disconnect();
                }catch (Exception e){
                    System.out.println("*dab* soc retrassat i no me donen paga");
                    System.out.println(e.toString());
                }

            } catch (Exception e) {
                System.out.println("Error blablabla " + e);
            } finally{
                conn.disconnect();
            }
            loaded = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
