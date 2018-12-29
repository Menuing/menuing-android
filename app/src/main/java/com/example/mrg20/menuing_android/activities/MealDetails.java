package com.example.mrg20.menuing_android.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import cz.msebera.android.httpclient.util.ByteArrayBuffer;

public class MealDetails extends GlobalActivity implements View.OnClickListener {

    int URLMode = 0;

    JSONObject recipe1;
    JSONObject recipe2;
    boolean badConnection = false;
    MealDetails.UrlConnectorGetRecipes ur;

    Date date;
    int meal_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            URLMode = getIntent().getExtras().getInt("URLMode");
        }

        date = (Date)getIntent().getSerializableExtra("DAY");
        meal_type = getIntent().getExtras().getInt("TIME");

        setContentView(R.layout.activity_meal_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button recipe = (Button) findViewById(R.id.first_recipe);
        recipe.setOnClickListener(this);
        recipe = (Button) findViewById(R.id.first_recipe2);
        recipe.setOnClickListener(this);

        DatabaseHelper db = new DatabaseHelper(this);

        byte[] img1;
        byte[] img2;

        ur = new UrlConnectorGetRecipes();
        ur.execute();
        while(!ur.loaded){if(ur.loaded)System.out.println(ur.loaded);}
        recipe1 = ur.getRecipe();


        if(ur.connection == false){
            badConnection = true;
            System.out.println("NO CONNECTION");
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(MealDetails.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(MealDetails.this);
            }
            builder.setTitle(R.string.err_no_connection_label)
                    .setMessage(R.string.err_no_connection)
                    .setPositiveButton(R.string.err_no_connection_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MealDetails.this, MainPageActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }else{
            try {
                img1 = ur.img;
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(img1, 0, img1 .length);
                ImageView img = (ImageView) findViewById(R.id.imageView5);
                img.setImageBitmap(bitmap1);
            }catch (Exception e){
                System.out.println("IMG 1 ERROR " + e);
            }
        }

        if(recipe1 != null)
            db.addData(recipe1);


        if(ur.connection) {
            ur.cancel(true);

            ur = new UrlConnectorGetRecipes();
            ur.execute();
            while (!ur.loaded) {
                if (ur.loaded) System.out.println(ur.loaded);
            }
            recipe2 = ur.getRecipe();

            if (ur.connection == false) {
                badConnection = true;
                System.out.println("NO CONNECTION");
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MealDetails.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MealDetails.this);
                }
                builder.setTitle(R.string.err_no_connection_label)
                        .setMessage(R.string.err_no_connection)
                        .setPositiveButton(R.string.err_no_connection_btn, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MealDetails.this, MainPageActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }else{
                try {
                    img2 = ur.img;
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(img2, 0, img2 .length);
                    ImageView img= (ImageView) findViewById(R.id.imageView6);
                    img.setImageBitmap(bitmap2);
                }catch (Exception e){
                    System.out.println("IMG 2 ERROR " + e);
                }

            }

            if(recipe2 != null)
                db.addData(recipe2);

            ur.cancel(true);
        }




        if(!badConnection)
            fillFields();
    }

    @Override
    public void onStop(){
        if(ur != null && !ur.isCancelled()) {
            ur.cancel(true);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        if(ur != null && !ur.isCancelled()) {
            ur.cancel(true);
        }
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        vibrate();
        if(!badConnection) {
            Intent intent = null;
            switch (view.getId()) {
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
        public boolean connection = true;

        private JSONObject thisRecipe;
        public byte[] img;

        public JSONObject getRecipe(){ return thisRecipe;}

        HttpURLConnection conn;

        @Override
        protected Void doInBackground(Void... params) {

            try {

                //GET ACTUAL USER ID
                URL url = new URL("http://" + ipserver + "/api/resources/recipes/getRandom/?username=" + settings.getString("UserMail", ""));
                if(meal_type == DINNER) {
                    url = new URL("http://" + ipserver + "/api/resources/recipes/getDinnerDish/?username=" + settings.getString("UserMail", ""));
                }
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                int userID = -1;
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONObject obj = new JSONObject(output);
                    thisRecipe = obj;
                    br.close();
                } else {
                    this.loaded = true;
                    this.connection = false;
                    System.out.println("COULD NOT FIND RECIPE");
                    return null;
                }

                conn.disconnect();

            } catch (Exception e) {
                this.loaded = true;
                this.connection = false;
                System.out.println("POZO 1 " + e );
            }

            if(thisRecipe != null) {
                try {
                    //TODO -----------------------------
                    URL google = new URL("https://www.google.com/search?tbm=isch&q=" + thisRecipe.getString("name").replace(" ", "_"));
                    System.out.println("URL gOOgle " + google);
                    conn = (HttpURLConnection) google.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "text/html");
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String str;
                    StringBuffer response = new StringBuffer();
                    while((str = br.readLine()) != null){
                        response.append(str);
                    }
                    System.out.println(response);

                    //<img[^>]+src="([^">]+)"
                    /*Pattern pattern = Pattern.compile("<img[^>]+src=\"([^\">]+)\"");
                    Matcher m = pattern.matcher(html);
                    String ex = "";
                    if(m.find()){
                        ex = m.group(0);
                    }

                    System.out.println("\n SAS: " + ex);
                    System.out.println("\n\nHTML: " + html);
                    */
                    //TODO POSAR LA URL DE LA IMG TROBADA AQUI
                    URL imageUrl = new URL("https://vignette.wikia.nocookie.net/reborn/images/f/f7/Lambo.jpg/revision/latest?cb=20101117114931");
                    URLConnection ucon = imageUrl.openConnection();

                    InputStream is = ucon.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);

                    ByteArrayBuffer baf = new ByteArrayBuffer(500);
                    int current = 0;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                    }
                    img = baf.toByteArray();
                    loaded = true;
                } catch (Exception e) {
                    System.out.println("POZO IMG " + e);
                    img = null;
                    this.loaded = true;
                }
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
