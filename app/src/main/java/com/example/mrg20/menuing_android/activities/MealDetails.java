package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;
import com.example.mrg20.menuing_android.other_classes.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Random;

public class MealDetails extends GlobalActivity implements View.OnClickListener {

    int URLMode = 0;

    JSONObject recipe1;
    JSONObject recipe2;

    MealDetails.UrlConnectorGetRecipes ur;

    ImageView shoppingListIcon;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            URLMode = getIntent().getExtras().getInt("URLMode");
        }

        setContentView(R.layout.activity_meal_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button recipe = (Button) findViewById(R.id.first_recipe);
        recipe.setOnClickListener(this);
        recipe = (Button) findViewById(R.id.first_recipe2);
        recipe.setOnClickListener(this);

        //shoppingListIcon = findViewById(R.id.meal_shopping_list_icon);
        //shoppingListIcon.setOnClickListener(this);

        ur = new UrlConnectorGetRecipes();
        ur.execute();
        while(!ur.loaded){}
        recipe1 = ur.getRecipe();
        ur = new UrlConnectorGetRecipes();
        ur.execute();
        while(!ur.loaded){}
        recipe2 = ur.getRecipe();

        fillFields();
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.first_recipe:
                intent = new Intent(MealDetails.this, RecipeDetails.class);
                intent.putExtra("recipe", recipe1.toString());
                startActivity(intent);
                break;
            case R.id.first_recipe2:
                intent = new Intent(MealDetails.this, RecipeDetails.class);
                intent.putExtra("recipe", recipe2.toString());
                startActivity(intent);
                break;
            /*case R.id.meal_shopping_list_icon:
                intent = new Intent(MealDetails.this, ShoppingListActivity.class);
                intent.putExtra("recipe1", recipe1.toString());
                intent.putExtra("recipe2", recipe2.toString());
                startActivity(intent);
                break;
                */
        }
    }

    private void fillFields(){
        TextView recipe1NameTV = (TextView) findViewById(R.id.textView2);
        TextView recipe1Rating = (TextView) findViewById(R.id.textView3);
        TextView recipe2NameTV = (TextView) findViewById(R.id.textView5);
        TextView recipe2Rating = (TextView) findViewById(R.id.textView4);
                //Aqui nirien les fotos
        try {
            recipe1NameTV.setText(recipe1.getString("name"));
            recipe2NameTV.setText(recipe2.getString("name"));
            String s = recipe1.getDouble("averagePuntuation")+"/10";
            recipe1Rating.setText(s);
            s = recipe2.getDouble("averagePuntuation")+"/10";
            recipe2Rating.setText(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    // Async + thread, class to make the connection to the server
    private class UrlConnectorGetRecipes extends AsyncTask<Void, Void, Void> {

        public boolean loaded = false;

        private JSONObject thisRecipe;

        public JSONObject getRecipe(){ return thisRecipe;}

        HttpURLConnection conn;

        @Override
        protected Void doInBackground(Void... params) {

            try {

                //GET ACTUAL USER ID
                URL url = new URL("http://" + ipserver + "/api/resources/recipes/getRandom/?username=" + settings.getString("UserMail", ""));
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                int userID = -1;
                System.out.println("BUSCANT RECEPTA");
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    System.out.println("REDLINE");
                    JSONObject arr = new JSONObject(output);
                    System.out.println(" OUTPUT: " + output);
                    thisRecipe = arr;
                    System.out.println("JSON_OBJECT");

                    br.close();
                } else {
                    System.out.println("COULD NOT FIND USER");
                    return null;
                }

                conn.disconnect();

                /*
                //GET RECIPE
                Random r = new Random();
                switch(URLMode){
                    case 1:
                        url = new URL("http://" + ipserver  + "/api/resources/recipes/healthy");
                        break;
                    case 2:
                        url = new URL("http://" + ipserver  + "/api/resources/recipes/3ingredient");
                        break;
                    case 3:
                        url = new URL("http://" + ipserver  + "/api/resources/recipes/2fast4uRecipe");
                        break;
                    default:
                        url = new URL("http://" + ipserver  + "/api/resources/recipes/id/" + r.nextInt(1000));
                        break;
                }
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                //conn.connect();

                System.out.println("OLA K ASE");

                if(conn.getResponseCode() == 200) {
                    InputStreamReader inp = new InputStreamReader(conn.getInputStream());
                    BufferedReader br = new BufferedReader(inp);
                    String output = br.readLine();
                    JSONObject obj = new JSONObject(output);
                    System.out.println("RECIPE: " + obj);
                    thisRecipe = obj;
                    System.out.println("THIS RECIPE IS: " + thisRecipe.getString("name"));

                    inp.close();
                    br.close();
                }else{
                    System.out.println("COULD NOT FIND RECIPES");
                    return null;
                }

                */
            } catch (Exception e) {
                System.out.println("ERROR AL LLEGIR LES RECEPTES TIO :( " + e);
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
