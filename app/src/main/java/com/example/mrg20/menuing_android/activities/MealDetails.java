package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MealDetails extends GlobalActivity implements View.OnClickListener {

    int URLMode = 0;

    JSONObject recipe1;
    JSONObject recipe2;

    MealDetails.UrlConnectorGetRecipes ur;

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

        ur = new UrlConnectorGetRecipes();
        ur.execute();
        while(!ur.loaded){if(ur.loaded)System.out.println(ur.loaded);}
        recipe1 = ur.getRecipe();
        ur.cancel(true);

        ur = new UrlConnectorGetRecipes();
        ur.execute();
        while(!ur.loaded){if(ur.loaded)System.out.println(ur.loaded);}
        recipe2 = ur.getRecipe();

        fillFields();
    }

    @Override
    public void onStop(){
        if(!ur.isCancelled()) {
            ur.cancel(true);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        if(!ur.isCancelled()) {
            ur.cancel(true);
        }
        super.onDestroy();
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
            if(recipe1NameTV.getText().length() >= 30){
                if(recipe1NameTV.getText().length() >= 60)
                    recipe1NameTV.setTextSize(12);
                else
                    recipe1NameTV.setTextSize(15);
            }else{
                recipe1NameTV.setTextSize(20);
            }

            recipe2NameTV.setText(recipe2.getString("name"));
            if(recipe2NameTV.getText().length() >= 30){
                if(recipe2NameTV.getText().length() >= 60)
                    recipe2NameTV.setTextSize(12);
                else
                    recipe2NameTV.setTextSize(15);
            }else{
                recipe2NameTV.setTextSize(20);
            }

            String s = "Rating: " + recipe1.getDouble("averagePuntuation")+"/5.0";
            recipe1Rating.setText(s);
            s = "Rating: " + recipe2.getDouble("averagePuntuation")+"/5.0";
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
                System.out.println("BUSCANT USUARI");
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    System.out.println("REDLINE");
                    JSONObject obj = new JSONObject(output);
                    thisRecipe = obj;
                    br.close();
                    loaded = true;
                } else {
                    System.out.println("COULD NOT FIND USER");
                    return null;
                }

                conn.disconnect();
                return null;
            } catch (Exception e) {

            }
            loaded = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            loaded = true;
            super.onPostExecute(result);
        }
    }
}
