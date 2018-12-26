package com.example.mrg20.menuing_android.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar.LayoutParams;


import com.example.mrg20.menuing_android.DatabaseHelper;
import com.example.mrg20.menuing_android.MainPageActivity;
import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.RegisterActivity;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;
import com.example.mrg20.menuing_android.utils.CheckboxUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TastesActivity extends GlobalActivity implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher {

    private LinearLayout checkBoxLayout;

    private ListView tastesList;

    private List<String> allTastesList;
    private List<String> tastesListString;

    private List<String> loadedTastes;

    ArrayAdapter<String> arrayAdapter;
    private List<String> selectedCheckAllergy = new ArrayList<>();

    TastesActivity.UrlConnectorGenIngredientList urGen;
    TastesActivity.UrlConnectorUpdateTastes urSave;

    private EditText filterEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tastes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        checkBoxLayout = (LinearLayout) findViewById(R.id.selectedTastesCheckboxListLayout);

        filterEditText = findViewById(R.id.tastesFilterEditText);
        filterEditText.addTextChangedListener(this);

        fillTastesList();
        tastesList = (ListView) findViewById(R.id.tastesScrollView);
        filterList("");
        tastesList.setOnItemClickListener(this);

        /*
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor sas = db.getData();
        System.out.println(sas);
        System.out.println("DATA:");

        while (sas.moveToNext()) {
            System.out.println("name " + sas.getString(1));
            System.out.println("inst " + sas.getString(2));
            System.out.println("proportions " + sas.getString(3));
            System.out.println("cal " + sas.getString(4));
            System.out.println("sodium " + sas.getString(5));
            System.out.println("fat " + sas.getString(6));
            System.out.println("protein " + sas.getString(7));
            System.out.println("foto " + sas.getString(8));
            System.out.println("averagePuntuation " + sas.getString(9));
            System.out.println("__________________________");
        }
        */


    }


    @Override
    public void onStop(){
        if(!urSave.isCancelled()) {
            urSave.cancel(true);
        }
        if(!urGen.isCancelled()) {
            urGen.cancel(true);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        if(!urSave.isCancelled()) {
            urSave.cancel(true);
        }
        if(!urGen.isCancelled()) {
            urGen.cancel(true);
        }
        super.onDestroy();
    }



    /***
     * Method to create the list of ingredients from database using a GET method
     */

    private void fillTastesList() {
        allTastesList = new ArrayList<String>();
        tastesListString = new ArrayList<String>();

        urGen = new TastesActivity.UrlConnectorGenIngredientList();
        urGen.execute();
        while (!urGen.loaded) {if(urGen.loaded)System.out.println(urGen.loaded);}
        urGen.cancel(true);
        loadedTastes = urGen.getLoadedTastesList();
        for(int i = 0; i<loadedTastes.size();i++) addElementToList(loadedTastes.get(i));
        allTastesList = urGen.getListOfIngredients();

        if(urGen.connection == false){
            System.out.println("NO CONNECTION");
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(TastesActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(TastesActivity.this);
            }
            builder.setCancelable(false);
            //builder.setFinishOnTouchOutside(false);

            builder.setTitle(R.string.err_no_connection_label)
                    .setMessage(R.string.err_no_connection)
                    .setPositiveButton(R.string.err_no_connection_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(TastesActivity.this, MainPageActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
    }

    private void populateList() {
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                tastesListString
        );
        tastesList.setAdapter(arrayAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        vibrate();
        addElementToList(tastesListString.get(position));
        tastesListString.remove(position);
        populateList();
    }

    private void addElementToList(String element) {
        CheckBox ckbx = CheckboxUtils.createNewCheckBox(element, this);
        ckbx.setOnCheckedChangeListener(this);
        checkBoxLayout.addView(ckbx);
        selectedCheckAllergy.add(element);
    }

    private void filterList(String text) {
        if (text.isEmpty()) {
            tastesListString = allTastesList;
        } else {
            tastesListString = new ArrayList<>();
            int listSize = allTastesList.size();
            for (int i = 0; i < listSize; i++) {
                if (allTastesList.get(i).toLowerCase().contains(text.toLowerCase()))
                    tastesListString.add(allTastesList.get(i));
            }
        }

        Collections.sort(tastesListString, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        populateList();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        filterList(s.toString());
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        filterList(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        filterList(s.toString());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            tastesListString.add(buttonView.getText().toString());

            Collections.sort(tastesListString, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            checkBoxLayout.removeView(buttonView);
            if(selectedCheckAllergy.contains(buttonView.getText().toString()))
                selectedCheckAllergy.remove(buttonView.getText().toString());
            populateList();
        }
    }

    //DE MOMENT NO SERVEIX DE RES, AMB LA BD POT SERVIR
    @Override
    public boolean onSupportNavigateUp() {
        vibrate();

        /*final ProgressDialog dialog = new ProgressDialog(this);
        //dialog.setMessage(getString(R.string.login_logging));
        dialog.setMessage("SAVING...");
        dialog.setCancelable(false);
        dialog.show();

        for(int i = 0; i < 1000; i++){
            dialog.setProgress((i/10) * 0);
        }


        ArrayList<String> tastesSelected = new ArrayList<>();
        for (int i = 0; i < selectedCheckAllergy.size(); i++) {
            CheckBox cb = checkBoxLayout.findViewWithTag(selectedCheckAllergy.get(i));
            if (cb != null && cb.isChecked())
                tastesSelected.add(selectedCheckAllergy.get(i));

        }*/

        urSave = new UrlConnectorUpdateTastes();
        urSave.setTastes(new ArrayList<>(selectedCheckAllergy));
        urSave.execute();
        while(!urSave.saved){if(urSave.saved)System.out.println(urSave.saved);}
        urSave.cancel(true);
        finish();
        return true;
    }


    // Async + thread, class to make the connection to the server
    private class UrlConnectorUpdateTastes extends AsyncTask<Void, Void, Void> {

        ArrayList<String> tastesSelected;
        public boolean saved = false;
        void setTastes(ArrayList<String> tastes) {
            this.tastesSelected = tastes;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (tastesSelected.size() == 0)
                return null;

            try {
                URL url = new URL("http://" + ipserver + "/api/resources/tastesAllergies/overrideIngredients");
                System.out.println(url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonString = new JSONObject()
                        .put("username", settings.getString("UserMail",""))
                        .put("ingredients", tastesSelected)
                        .put("taste", true)
                        .toString();

                System.out.println(jsonString);
                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();
                System.out.println("HTTP CODE " + conn.getResponseCode());
                conn.disconnect();

                url = new URL("http://" + ipserver + "/api/resources/recommendedRecipes/calculateRecommendedRecipes?username=" + settings.getString("UserMail",""));
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                System.out.println("HTTP CODE " + conn.getResponseCode());
                conn.disconnect();

            }catch (Exception e){
                System.out.println("Could not save tastes " + e);
            }

            saved = true;
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            saved = true;
            super.onPostExecute(result);
        }
    }


    // Async + thread, class to make the connection to the server
    private class UrlConnectorGenIngredientList extends AsyncTask<Void, Void, Void> {
        ArrayList<String> ingredientList = new ArrayList<>();
        ArrayList<String> loadedTastesList = new ArrayList<>();
        public boolean loaded = false;
        public boolean connection = true;

        ArrayList<String> getListOfIngredients() {
            return ingredientList;
        }
        ArrayList<String> getLoadedTastesList() { return loadedTastesList; }

        public UrlConnectorGenIngredientList(){
            loaded = false;
            connection = true;
            loadedTastesList = new ArrayList<>();
            ingredientList = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://" + ipserver  + "/api/resources/ingredients/excludingIngredientList");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");

                String jsonString = new JSONObject()
                        .put("username", settings.getString("UserMail","") )
                        .put("taste", true)
                        .toString();

                System.out.println(jsonString);

                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();

                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    for(int i = 0; i<arr.length(); i++){
                        String ingredientName = arr.getJSONObject(i).getString("name");
                        ingredientList.add(ingredientName);
                    }
                    br.close();
                }else{
                    this.loaded = true;
                    this.connection = false;
                    System.out.println("COULD NOT FIND INGREDIENTS");
                    return null;
                }
                conn.disconnect();

            } catch (Exception e) {
                this.loaded = true;
                this.connection = false;
                System.out.println("Ingredients not found " + e);
            }


            try {
                //GET USER ALLERGIES
                URL url = new URL("http://" + ipserver  + "/api/resources/ingredients/userTasteAllergyIngredients");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                String jsonString = new JSONObject()
                        .put("username", settings.getString("UserMail","") )
                        .put("taste", true)
                        .toString();

                System.out.println(jsonString);

                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();

                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    for(int i = 0; i<arr.length(); i++){
                        String ingredientName = arr.getJSONObject(i).getString("name");
                        loadedTastesList.add(ingredientName);
                    }
                    br.close();
                }else{
                    this.loaded = true;
                    this.connection = false;
                    System.out.println("COULD NOT FIND INGREDIENTS");
                    return null;
                }
                conn.disconnect();

            } catch (Exception e) {
                this.loaded = true;
                this.connection = false;
                System.out.println("Ingredients not found " + e);
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