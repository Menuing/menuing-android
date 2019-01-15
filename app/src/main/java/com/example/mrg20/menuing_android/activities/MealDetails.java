package com.example.mrg20.menuing_android.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.mrg20.menuing_android.DatabaseHelper;
import com.example.mrg20.menuing_android.MainPageActivity;
import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

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
    JSONObject recipe1;
    JSONObject recipe2;
    boolean badConnection = false;
    MealDetails.UrlConnectorGetRecipes ur;
    LinearLayout secondRecipeLayout;

    Date date;
    int type;
    int mode;
    int num_recipes = 0;
    byte[] img1;
    byte[] img2;
    Bitmap bitmap1;
    Bitmap bitmap2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String recipe1String = "";
        String recipe2String = "";

        super.onCreate(savedInstanceState);
        //progress.show();

        setContentView(R.layout.activity_meal_details);
        secondRecipeLayout=(LinearLayout)this.findViewById(R.id.Second);
        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getInt("TYPE");
            mode = getIntent().getExtras().getInt("MODE");
            recipe1String =  getIntent().getExtras().getString("RECIPE1");
            recipe2String =  getIntent().getExtras().getString("RECIPE2");
            date = (Date)getIntent().getSerializableExtra("DAY");
        }

        if (mode == RECIPE) {
            secondRecipeLayout.setVisibility(LinearLayout.GONE);
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button recipe = (Button) findViewById(R.id.first_recipe);
        recipe.setOnClickListener(this);
        recipe = (Button) findViewById(R.id.first_recipe2);
        recipe.setOnClickListener(this);

        DatabaseHelper db = new DatabaseHelper(this);



        try {
            if(recipe1String != null && !recipe1String.equals("")) {
                recipe1 = new JSONObject(recipe1String);
            }
            if(recipe2String != null && !recipe2String.equals("")) {
                recipe2 = new JSONObject(recipe2String);
            }


            ur = new UrlConnectorGetRecipes();
            ur.execute();
            while (!ur.loaded) {
                if (ur.loaded) System.out.println(ur.loaded);
            }
            if(recipe1 == null) {
                recipe1 = ur.getRecipe();
            }
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
            } else {
                if(img1!=null){
                    try {
                        img1 = ur.img;
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        if (img1.length > 800000) {
                            opts.inSampleSize = 6;
                        } else if (img1.length > 400000) {
                            opts.inSampleSize = 4;
                        } else if (img1.length > 200000) {
                            opts.inSampleSize = 2;
                        } else {
                            opts.inSampleSize = 1;
                        }

                        bitmap1 = BitmapFactory.decodeByteArray(img1, 0, img1.length, opts);
                        ImageView img = (ImageView) findViewById(R.id.imageView5);
                        img.setImageBitmap(bitmap1);
                        img.setMinimumWidth(200);
                    }catch (Exception e) {
                        System.out.println("IMG 2 ERROR " + e);

                    }
                }
            }

            if(recipe1 != null)
                db.addData(recipe1);

            if(ur.connection && mode == MEAL) {
                ur.cancel(true);

                ur = new UrlConnectorGetRecipes();
                ur.execute();
                while (!ur.loaded) {
                    if (ur.loaded) System.out.println(ur.loaded);
                }
                if(recipe2 == null) {
                    recipe2 = ur.getRecipe();
                }
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
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }else{
                    if(img2 != null) {
                        try {
                            img2 = ur.img;
                            BitmapFactory.Options opts = new BitmapFactory.Options();
                            if (img1.length > 800000) {
                                opts.inSampleSize = 6;
                            } else if (img1.length > 400000) {
                                opts.inSampleSize = 4;
                            } else if (img1.length > 200000) {
                                opts.inSampleSize = 2;
                            } else {
                                opts.inSampleSize = 1;
                            }
                            bitmap2 = BitmapFactory.decodeByteArray(img2, 0, img2.length, opts);
                            ImageView img = (ImageView) findViewById(R.id.imageView6);
                            img.setImageBitmap(bitmap2);
                            img.setMinimumWidth(200);
                        } catch (Exception e) {
                            System.out.println("IMG 2 ERROR " + e);
                        }
                    }
                }

                if(recipe2 != null)
                    db.addData(recipe2);

                ur.cancel(true);
            }

            if(!badConnection)
                fillFields();

            progress.cancel();
        } catch (Exception e) {
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
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
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
        if(bitmap1!=null)
        bitmap1.recycle();
        if(bitmap2!=null)
        bitmap2.recycle();
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
                    intent.putExtra("img", img1);
                    startActivity(intent);
                    break;
                case R.id.first_recipe2:
                    intent = new Intent(MealDetails.this, RecipeDetails.class);
                    intent.putExtra("recipe", recipe2.toString());
                    intent.putExtra("img", img2);
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
            if(mode == MEAL ) {
                recipe2NameTV.setText(recipe2.getString("name"));
                if (recipe2NameTV.getText().length() >= 30) {
                    if (recipe2NameTV.getText().length() >= 60)
                        recipe2NameTV.setTextSize(12);
                    else
                        recipe2NameTV.setTextSize(15);
                } else {
                    recipe2NameTV.setTextSize(20);
                }
            }

            String s = "Rating: " + recipe1.getDouble("averagePuntuation")+"/5.0";
            recipe1Rating.setText(s);
            if(ur.connection && mode==MEAL) {
                s = "Rating: " + recipe2.getDouble("averagePuntuation") + "/5.0";
                recipe2Rating.setText(s);
            }
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

                switch (type){
                    case DINNER:
                        url = new URL("http://" + ipserver + "/api/resources/recipes/getDinnerDish/?username=" + settings.getString("UserMail", ""));
                        break;
                    case LUNCH:
                        if(num_recipes == 0) {
                            num_recipes++;
                            url = new URL("http://" + ipserver + "/api/resources/recipes/getFirstDish/?username=" + settings.getString("UserMail", ""));
                        }else{
                            url = new URL("http://" + ipserver + "/api/resources/recipes/getSecondDish/?username=" + settings.getString("UserMail", ""));
                        }
                        break;
                    case BREAKFAST:
                        url = new URL("http://" + ipserver + "/api/resources/recipes/getBreakfast/?username=" + settings.getString("UserMail", ""));
                        break;
                    case NO_PREFERENCES:
                        url = new URL("http://" + ipserver + "/api/resources/recipes/getRandom/?username=" + settings.getString("UserMail", ""));
                        break;
                    case THREE_INGREDIENTS:
                        url = new URL("http://" + ipserver + "/api/resources/recipes/getLowCost/?username=" + settings.getString("UserMail", ""));
                        break;
                    case FAST_TO_DO:
                        url = new URL("http://" + ipserver + "/api/resources/recipes/getFastToDo/?username=" + settings.getString("UserMail", ""));
                        break;
                    case COCKTAIL:
                        url = new URL("http://" + ipserver + "/api/resources/recipes/getCocktail/?username=" + settings.getString("UserMail", ""));
                        break;
                    case DESSERT:
                        url = new URL("http://" + ipserver + "/api/resources/recipes/getDessert/?username=" + settings.getString("UserMail", ""));
                        break;
                    default:
                        url = new URL("http://" + ipserver + "/api/resources/recipes/getRandom/?username=" + settings.getString("UserMail", ""));
                        break;
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
                    //&alt=json
                    URL google = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyDav6P_d4O-p4dZvdxamzS9l7zM69tYVD0&cx=002545423491658978052:-hcz3ln8tpo&q=" + thisRecipe.getString("name").replace(" ", "_") + "&searchType=image");
                    System.out.println("URL gOOgle " + google);
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
                            if(str.contains("src="))
                                System.out.println("SOURCE AQUI " + str);
                            if(str.contains("<img"))
                                System.out.println("IMG AQUI " + str);

                            output.append(str);
                        }

                        System.out.println(output);
                        JSONObject arr = new JSONObject(output.toString());
                        fotoUrl = arr.getJSONArray("items").getJSONObject(0).getString("link");

                    }

                    //TODO POSAR LA URL DE LA IMG TROBADA AQUI
                    System.out.println("IMG " + fotoUrl);
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
            if (progress!=null) {
                progress.cancel();
            }
        }
    }
}
