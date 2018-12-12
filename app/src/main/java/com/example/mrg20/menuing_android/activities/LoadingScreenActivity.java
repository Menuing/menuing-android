package com.example.mrg20.menuing_android.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class LoadingScreenActivity extends GlobalActivity{

    int URLMode = 0;
    UrlConnectorGetRecipes ur;

    JSONObject recipe1;
    JSONObject recipe2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            URLMode = getIntent().getExtras().getInt("URLMode");
        }

        setContentView(R.layout.test_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);

        Button b = (Button) findViewById(R.id.button1);
        b.setVisibility(View.INVISIBLE);
        p.setVisibility(View.VISIBLE);

        //TODO DESCOMENTAR I MIRAR A GLOBALACTIVITY COM CRIDAR AIXO DESDE DINS DE LA ASYNC TASK (si es que es pot)
        /*
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.login_logging));
        dialog.setProgress(0);
        dialog.setCancelable(false);

        Handler h = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        dialog.cancel();
                        if ((Boolean) msg.obj) {
                            loginAction();
                        }else{
                            Toast.makeText(getApplicationContext(), getString(R.string.err_login_fail), Toast.LENGTH_SHORT).show();
                        }
                }
            }
        };//Handler per esperar que acabi la Task el sistema, si no ho pot tallar abans de que acabi el login
        */


        //TODO: aixo es lo que va dins de la async task
        /*

        final Message msg = new Message();
        final boolean[] obj = new boolean[1];

        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        obj[0] = true;
                        if (!task.isSuccessful()) {
                            obj[0] = false;
                        }
                        else{
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("UserMail", mail);
                            editor.commit();
                        }
                        msg.what = 0;
                        msg.obj = obj[0];
                        h.sendMessage(msg);
                    }
                });

         */

    }

    @Override
    public void onStart() {
        super.onStart();
        ur = new UrlConnectorGetRecipes();
        ur.execute();
        while(!ur.loaded){}
        System.out.print("UR.LOADED HA ACABAT >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        if(ur.getReturnCode()!=200){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("ERROR 404, SE SIENTE :D");
            dialog.setProgress(0);
            //dialog.setCancelable(false);
        }else{
            recipe1 = ur.getRecipe();
            ur = new UrlConnectorGetRecipes();
            ur.execute();
            while(!ur.loaded){}
            if(ur.getReturnCode()!=200){
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("ERROR 404, SE SIENTE :D");
                dialog.setProgress(0);
                //dialog.setCancelable(false);
            }else{
                recipe2 = ur.getRecipe();
                Toast.makeText(this, "JAJAJAJAJJAJAE", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Async + thread, class to make the connection to the server
    private class UrlConnectorGetRecipes extends AsyncTask<Void, Long, Integer> {

        public boolean loaded = false;

        private JSONObject thisRecipe;

        public JSONObject getRecipe(){ return thisRecipe;}

        private int HttpReturnCode;

        public int getReturnCode(){
            System.out.print("GET RETURN CODE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            return HttpReturnCode;
        }

        private JSONObject user;

        HttpURLConnection conn;

        @Override
        protected Integer doInBackground(Void... params) {

            try {

                //GET ACTUAL USER ID
                URL url = new URL("http://" + ipserver + "/api/resources/users/?username=" + settings.getString("UserMail", ""));
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                int userID = -1;
                System.out.println("BUSCANT USUARI");
                HttpReturnCode = conn.getResponseCode();
                publishProgress(0L);
                if (HttpReturnCode == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    System.out.println("REDLINE");
                    JSONArray arr = new JSONArray(output);
                    System.out.println("ARRRRRRRRRR + OUTPUT: " + output);
                    if(arr.length() > 0) {
                        user = arr.getJSONObject(0);
                        System.out.println("JSON_OBJECT");
                        userID = user.getInt("id");
                    }
                    System.out.println("USER: " + user);
                    br.close();
                } else {
                    System.out.println("COULD NOT FIND USER");
                    return HttpReturnCode;
                }

                conn.disconnect();
                if (userID == -1) {
                    System.out.println("USER NOT EXISTS");
                    return HttpReturnCode;
                }

                publishProgress(25L);
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
                        url = new URL("http://" + ipserver + "/api/resources/recipes/getRandom/?username=" + settings.getString("UserMail", ""));
                        break;
                }
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                //conn.connect();

                System.out.println("OLA K ASE");

                publishProgress(50L);

                HttpReturnCode = conn.getResponseCode();
                if (HttpReturnCode == 200) {
                    InputStreamReader inp = new InputStreamReader(conn.getInputStream());
                    BufferedReader br = new BufferedReader(inp);
                    String output = br.readLine();
                    JSONObject obj = new JSONObject(output);
                    System.out.println("RECIPE: " + obj);
                    thisRecipe = obj;
                    System.out.println("THIS RECIPE IS: " + thisRecipe.getString("name"));

                    publishProgress(75L);

                    inp.close();
                    br.close();
                }else{
                    System.out.println("COULD NOT FIND RECIPES");
                    return HttpReturnCode;
                }

                publishProgress(100L);

            } catch (Exception e) {
                System.out.println("ERROR AL LLEGIR LES RECEPTES TIO :( " + e);
            } finally{
                conn.disconnect();
            }
            loaded = true;
            publishProgress();
            return HttpReturnCode;

        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);

            System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa");
            loaded = true;
        }
    }
}
