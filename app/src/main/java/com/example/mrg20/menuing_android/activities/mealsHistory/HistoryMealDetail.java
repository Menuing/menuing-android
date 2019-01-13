package com.example.mrg20.menuing_android.activities.mealsHistory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mrg20.menuing_android.DatabaseHelper;
import com.example.mrg20.menuing_android.MainPageActivity;
import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import cz.msebera.android.httpclient.util.ByteArrayBuffer;

public class HistoryMealDetail extends GlobalActivity implements RatingBar.OnRatingBarChangeListener {

    String recipeName;

    ConnectorUpdateRating cur;
    UrlConnectorGetImage urimage;
    DatabaseHelper db;
    boolean ratingChanged = false;
    int recipeId;
    int sqliteId;

    RatingBar ratingBar;
    int id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_recipe_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        id = b.getInt("position");

        if(id==-1){
            Intent in = new Intent(HistoryMealDetail.this, MainPageActivity.class);
            startActivity(in);
        }

        db = new DatabaseHelper(this);
        Cursor cursor = db.getData();
        int i = 0;
        while(cursor.moveToNext()){
            if(id == i) {
                break;
            }
            i++;
        }

        String name = cursor.getString(1);
        String proportions = cursor.getString(3);
        String guide = cursor.getString(2);
        String rating = cursor.getString(9);
        String myrating = cursor.getString(11);
        recipeId = Integer.parseInt(cursor.getString(10));
        sqliteId = Integer.parseInt(cursor.getString(0));
        System.out.println("MY RATING DE LA RECIPE " + myrating + " " + name);
        TextView tv = findViewById(R.id.dish_detail_name);
        tv.setText(name);
        TextView ingredients = findViewById(R.id.ingredient1_detail);
        ingredients.setText(formatText(proportions));
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
        System.out.println("rating: " + (int)ratingFloat);
        ratingBar = (RatingBar) findViewById(R.id.recipeRatingBar);
        ratingBar.setMax(5);
        ratingBar.setNumStars((int) ratingFloat);
        if(myrating != null)
            ratingBar.setRating(Float.parseFloat(myrating));
        ratingBar.setOnRatingBarChangeListener(this);

        urimage = new UrlConnectorGetImage();
        urimage.recipename = name;
        urimage.execute();
        while (!urimage.loaded){if(urimage.loaded) System.out.println("imageLoaded");}

        if(urimage.imageOK){
            BitmapFactory.Options opts = new BitmapFactory.Options();
            if(urimage.img.length>800000){
                opts.inSampleSize = 6;
            }else if(urimage.img.length>400000){
                opts.inSampleSize = 4;
            }else if(urimage.img.length>150000){
                opts.inSampleSize = 2;
            }else{
                opts.inSampleSize = 1;
            }
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(urimage.img, 0, urimage.img.length, opts);

            ImageView img = (ImageView) findViewById(R.id.dish_image);
            img.setImageBitmap(bitmap1);
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        vibrate();
        db.updateRate(Float.toString(v), sqliteId);

        cur = new ConnectorUpdateRating();
        cur.recipeRating = v;
        cur.recipeId = recipeId;
        cur.execute();

        ratingChanged = true;
    }

    private String formatText(String text){
        String result = "";
        String[] parts = text.split(";");
        for(int i = 0; i < parts.length; i++){
            result = result + parts[i] + "\n";
        }
        return result;
    }

    private class ConnectorUpdateRating extends AsyncTask<Void, Void, Void> {

        public boolean loaded = false;
        HttpURLConnection conn;
        public float recipeRating;
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
                        .put("punctuation", (int) recipeRating)
                        .toString();

                System.out.println(jsonString);
                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();
                System.out.println("CODE HTTP: " + conn.getResponseCode());
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


    private class UrlConnectorGetImage extends AsyncTask<Void, Void, Void> {

        public String recipename = "";
        public boolean loaded = false;
        boolean imageOK = false;
        public byte[] img;

        HttpURLConnection conn;

        @Override
        protected Void doInBackground(Void... params) {
            if(recipename != "") {
                try {
                    //&alt=json
                    URL google = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyDav6P_d4O-p4dZvdxamzS9l7zM69tYVD0&cx=002545423491658978052:-hcz3ln8tpo&q=" + recipename.replace(" ", "_") + "&searchType=image");
                    conn = (HttpURLConnection) google.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    String fotoUrl = "";
                    if(conn.getResponseCode() == 200) {
                        System.out.println("OK API GOOGLE");
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuffer output = new StringBuffer();;
                        String str;
                        while((str = br.readLine()) != null){
                            output.append(str);
                        }
                        JSONObject arr = new JSONObject(output.toString());
                        fotoUrl = arr.getJSONArray("items").getJSONObject(0).getString("link");
                    }

                    URL imageUrl = new URL(fotoUrl);
                    URLConnection ucon = imageUrl.openConnection();

                    InputStream is = ucon.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);

                    ByteArrayBuffer baf = new ByteArrayBuffer(500);
                    int current = 0;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                    }
                    img = baf.toByteArray();
                    conn.disconnect();
                    this.loaded = true;
                    this.imageOK = true;
                } catch (Exception e) {
                    System.out.println("Error IMG " + e);
                    img = null;
                    this.loaded = true;
                    this.imageOK = false;
                }
            }
            this.imageOK = true;
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

