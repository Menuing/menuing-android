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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
        recipeName = (String) tv.getText();

        ur = new RecipeDetails.UrlConnectorUpdateRating();
        ur.setRecipeName(recipeName);
        ur.execute();

        ratingBar = findViewById(R.id.recipeRatingBar);
        while(!ur.loaded){}
        ratingBar.setRating(ur.getRating());
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
        if(ratingChanged)
            ur.updateToDatabase();
        finish();
        return true;
    }

    // Async + thread, class to make the connection to the server
    private class UrlConnectorUpdateRating extends AsyncTask<Void, Void, Void> {

        ArrayList<String> allergiesSelected;

        public boolean loaded = false;

        public String recipeName = "";

        public void setRecipeName(String recipeName) {
            this.recipeName = recipeName;
        }

        private JSONObject thisRecipe;

        HttpURLConnection conn;

        float recipeRating;
        float getRating(){
            return recipeRating;
        }
        void updateRating(float r){
            recipeRating = r;
        }

        //TODO AIXO NO VA AIXI, MIRAR COM VA
        public void updateToDatabase(){
            try {
                URL url = new URL("http://" + ipserver  + "/api/resources/recipes/");
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Accept", "application/json");
                conn.connect();

                if(conn.getResponseCode() == 200) {
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(thisRecipe.toString());
                    wr.flush();
                    wr.close();
                }
            } catch (Exception e) {
                System.out.println("ILLO CABESA" + e);
            } finally {
                conn.disconnect();
            }

            try {
                URL url = new URL("http://" + ipserver  + "/api/resources/recipes/");
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                if(conn.getResponseCode() == 200) {
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    thisRecipe.remove("averagePuntuation");
                    thisRecipe.put("averagePuntuation", recipeRating);

                    wr.write(thisRecipe.toString());
                    wr.flush();
                    wr.close();

                }
            } catch (Exception e) {
                System.out.println("ACHO ACHO ACHO" + e);
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(loaded)
                return null;
            try {

                //GET ACTUAL USER ID
                URL url = new URL("http://" + ipserver  + "/api/resources/recipes/all");
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.connect();

                if(conn.getResponseCode() == 200) {
                    InputStreamReader inp = new InputStreamReader(conn.getInputStream());
                    BufferedReader br = new BufferedReader(inp);
                    while(br.ready()) {
                        String output = br.readLine();
                        JSONArray arr = new JSONArray(output);
                        System.out.println("RECIPE: " + arr.getJSONObject(0));
                        if(arr.getJSONObject(0).getString("name").equals(recipeName)) {
                            recipeRating = (float) arr.getJSONObject(0).get("averagePuntuation");
                            System.out.print("THIS RECIPE IS: " + thisRecipe.getString("name"));
                        }
                    }

                    /*for (int i = 0; i < arr.length(); i++) {
                        System.out.println("RECIPE: " + arr.getJSONObject(i));
                        if(arr.getJSONObject(i).getString("name").equals(recipeName)){
                            thisRecipe = arr.getJSONObject(i);
                            System.out.print("RECIPE NUMBER " + i + ": " + arr.getJSONObject(i).getString("name"));
                            recipeRating = (float) arr.getJSONObject(i).get("averagePuntuation");
                        }

                        System.out.print("THIS RECIPE IS: " + thisRecipe.getString("name"));
                    }*/

                    inp.close();
                    br.close();
                }else{
                    System.out.println("COULD NOT FIND RECIPES");
                    return null;
                }
                //////////////////////////////////

                //GET INGREDIENT LIST AND COMPARE WITH THE ALLERGIES SELECTED
                /*ArrayList<Integer> ingredientIds = new ArrayList<>();
                url = new URL("http://" + ipserver  + "/api/resources/ingredients/all");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    for(int i = 0; i<arr.length(); i++){
                        String ingredientName = arr.getJSONObject(i).getString("name");
                        if(allergiesSelected.contains(ingredientName)) {
                            ingredientIds.add(arr.getJSONObject(i).getInt("id"));
                        }
                    }
                    System.out.println("INGREDIENTS: " + arr);
                }else{
                    System.out.println("COULD NOT FIND INGREDIENTS");
                    return null;
                }
                conn.disconnect();

                //UPDATE ALLERGIES IN DATABASE
                url = new URL("http://" + ipserver  + "/api/resources/tastesAllergies");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                for(int i=0; i<ingredientIds.size(); i++) {
                    JSONObject subjson = new JSONObject()
                            .put("name", recipeName)
                            .put("ingredientId", ingredientIds.get(i));

                    String jsonString = new JSONObject()
                            .put("key", subjson)
                            .put("taste", false)
                            .put("allergy", true)
                            .toString();

                    System.out.println(jsonString);
                    OutputStream os = conn.getOutputStream();
                    os.write(jsonString.getBytes());
                    os.flush();
                }
                System.out.println("CONNECTION CODE: " + conn.getResponseCode());
                conn.disconnect();*/
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

    /*
    // Async + thread, class to make the connection to the server
    private class UrlConnectorGenIngredientList extends AsyncTask<Void,Void,Void> {
        ArrayList<String> ingredientList = new ArrayList<>();;
        public boolean loaded = false;

        ArrayList<String> getListOfIngredients() {
            return ingredientList;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                //GET INGREDIENT LIST AND COMPARE WITH THE ALLERGIES SELECTED
                URL url = new URL("http://" + ipserver  + "/api/resources/ingredients/all");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    for(int i = 0; i<arr.length(); i++){
                        String ingredientName = arr.getJSONObject(i).getString("name");
                        ingredientList.add(ingredientName);
                    }

                    loaded = true;
                }else{
                    System.out.println("COULD NOT FIND INGREDIENTS");
                    return null;
                }
                conn.disconnect();

            } catch (Exception e) {
                System.out.println("User could not have been introduced to the database " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }*/
}
