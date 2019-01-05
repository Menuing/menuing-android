package com.example.mrg20.menuing_android.activities.mealsHistory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mrg20.menuing_android.DatabaseHelper;
import com.example.mrg20.menuing_android.MainPageActivity;
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

public class HistoryMealDetail extends GlobalActivity implements RatingBar.OnRatingBarChangeListener {

    String recipeName;

    boolean ratingChanged = false;

    RatingBar ratingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_recipe_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        int id = b.getInt("position");

        if(id==-1){
            Intent in = new Intent(HistoryMealDetail.this, MainPageActivity.class);
            startActivity(in);
        }

        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getRecipeByID(id+1);
        cursor.moveToNext();
        String name = cursor.getString(1);
        String proportions = cursor.getString(3);
        String guide = cursor.getString(2);
        String rating = cursor.getString(9);


        TextView tv = findViewById(R.id.dish_detail_name);
        tv.setText(name);
        TextView ingredients = findViewById(R.id.ingredient1_detail);
        ingredients.setText(proportions);
        TextView instructions = findViewById(R.id.steps_recipe_detail);
        instructions.setText(guide);

        recipeName = (String) tv.getText();

        if (name.length() >= 30) {
            if (name.length() >= 60)
                tv.setTextSize(15);
            else
                tv.setTextSize(20);
        } else {
            tv.setTextSize(25);
        }

        float ratingFloat = Float.parseFloat(rating);
        System.out.println("rting: " + (int)ratingFloat);
        ratingBar = (RatingBar) findViewById(R.id.recipeRatingBar);
        ratingBar.setMax(5);
        ratingBar.setNumStars((int) ratingFloat);
        ratingBar.setRating(ratingFloat);
        ratingBar.setOnRatingBarChangeListener(this);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        vibrate();
        ratingChanged = true;
    }




    private class ConnectorUpdateRating extends AsyncTask<Void, Void, Void> {

        public boolean loaded = false;
        HttpURLConnection conn;
        public int recipeRating;
        public int recipeId;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL("http://" + ipserver + "/api/resources/usersRecipes/updateByUsername");
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonString = new JSONObject()
                        .put("username", settings.getString("UserMail",""))
                        .put("recipeId", recipeId)
                        .put("punctuation", recipeRating)
                        .toString();

                System.out.println(jsonString);
                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();
                conn.disconnect();

            } catch (Exception e) {
                this.loaded = true;
                System.out.println("Error saving punctuation " + e);
            } finally{
                conn.disconnect();
            }
            this.loaded = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            loaded = true;
            super.onPostExecute(result);
        }
    }

}

