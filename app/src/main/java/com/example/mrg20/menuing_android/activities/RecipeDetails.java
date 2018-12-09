package com.example.mrg20.menuing_android.activities;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class RecipeDetails extends GlobalActivity implements RatingBar.OnRatingBarChangeListener {

    String recipeName;
    RecipeDetails.UrlConnectorUpdateRating ur;

    boolean ratingChanged = false;

    RatingBar ratingBar;
    JSONObject recipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        if (getIntent().getExtras() != null) {
            try {
                String s = getIntent().getStringExtra("recipe");
                recipe = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView tv = findViewById(R.id.dish_detail_name);
        TextView ingredients = findViewById(R.id.ingredient1_detail);
        TextView instructions = findViewById(R.id.steps_recipe_detail);
        recipeName = (String) tv.getText();

        if(recipeName.length() >= 30){
            if(recipeName.length() >= 60)
                tv.setTextSize(15);
            else
                tv.setTextSize(20);
        }else{
            tv.setTextSize(25);
        }

        ur = new RecipeDetails.UrlConnectorUpdateRating();
        ur.execute();

        ratingBar = findViewById(R.id.recipeRatingBar);
        while(!ur.loaded){}

        try {
            if(recipe != null) {
                tv.setText(recipe.getString("name"));
                String textToFormat = recipe.getString("proportions");
                ingredients.setText(formatText(textToFormat));
                instructions.setText(recipe.getString("instructions"));
                ratingBar.setRating((float) recipe.getDouble("puntuation"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ratingBar.setOnRatingBarChangeListener(this);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        vibrate();
        ratingChanged = true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        vibrate();
        if(ratingChanged){
            ur = new RecipeDetails.UrlConnectorUpdateRating();
            ur.updateRecipe(recipe);
            ur.execute();
        }

        finish();
        return true;
    }

    private String formatText(String text){
        String result = "";
        String[] parts = text.split(";");
        for(int i = 0; i < parts.length; i++){
            result = result + parts[i] + "\n";
        }
        return result;
    }

    // Async + thread, class to make the connection to the server
    private class UrlConnectorUpdateRating extends AsyncTask<Void, Void, Void> {

        public boolean loaded = false;

//        public String recipeName = "";
        private JSONObject thisRecipe;

        private JSONObject user;

        HttpURLConnection conn;

        float recipeRating;

        void updateRecipe(JSONObject j){
            thisRecipe = j;
            try {
                recipeRating = (float) thisRecipe.getDouble("averagePuntuation");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
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
                    recipeName = arr.getString("name");	
                    System.out.println("JSON_OBJECT");	
                     br.close();	
                } else {	
                    System.out.println("COULD NOT FIND USER");	
                    return null;	
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
