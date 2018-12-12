package com.example.mrg20.menuing_android.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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

public class AllergiesActivity extends GlobalActivity implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher {

    private LinearLayout checkBoxLayout;

    private ListView allergiesList;

    private List<String> allAllergiesList;
    private List<String> allergiesListString;

    private List<String> loadedAllergies; //LIST OF ALLERGIES LOADED WHEN YOU ENTER THE LAYOUT

    ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> selectedCheckAllergy;


    private EditText filterEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies);
        System.out.println("CREATING");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        checkBoxLayout = (LinearLayout) findViewById(R.id.selectedAllergiesCheckboxListLayout);

        filterEditText = findViewById(R.id.allergiesFilterEditText);
        filterEditText.addTextChangedListener(this);

        System.out.println("BEFoRE FILL");
        fillAllergiesList();
        allergiesList = (ListView) findViewById(R.id.allergiesScrollView);
        filterList("");
        allergiesList.setOnItemClickListener(this);
    }


    /***
     * Method to create the list of ingredients from database using a GET method
     */

    private void fillAllergiesList() {
        allAllergiesList = new ArrayList<>();
        allergiesListString = new ArrayList<>();
        selectedCheckAllergy = new ArrayList<>();

        AllergiesActivity.UrlConnectorGenIngredientList ur = new AllergiesActivity.UrlConnectorGenIngredientList();
        ur.execute();
        while(!ur.loaded){}
        ur.cancel(true);
        loadedAllergies = ur.getLoadedAllergiesList();
        for(int i = 0; i<loadedAllergies.size();i++) addElementToList(loadedAllergies.get(i));
        allAllergiesList = ur.getListOfIngredients();
    }

    private void populateList(){
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                allergiesListString
        );
        allergiesList.setAdapter(arrayAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        vibrate();
        addElementToList(allergiesListString.get(position));
        allergiesListString.remove(position);
        populateList();
    }

    private void addElementToList(String element){
        CheckBox ckbx = CheckboxUtils.createNewCheckBox(element, this);
        ckbx.setOnCheckedChangeListener(this);
        checkBoxLayout.addView(ckbx);
        selectedCheckAllergy.add(element);
        System.out.println("Loaded " + loadedAllergies);
    }

    private void filterList(String text){
        if(text.isEmpty()){
            allergiesListString = allAllergiesList;
        }
        else {
            allergiesListString = new ArrayList<>();
            int listSize = allAllergiesList.size();
            for (int i = 0; i < listSize; i++) {
                if (allAllergiesList.get(i).toLowerCase().contains(text.toLowerCase()))
                    allergiesListString.add(allAllergiesList.get(i));
            }
        }

        Collections.sort(allergiesListString, new Comparator<String>() {
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
        if(!isChecked){
            allergiesListString.add(buttonView.getText().toString());

            Collections.sort(allergiesListString, new Comparator<String>() {
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
        }*/

        /*
        ArrayList<String> allergiesSelected = new ArrayList<>();
        for(int i = 0; i<selectedCheckAllergy.size(); i++){
            CheckBox cb = checkBoxLayout.findViewWithTag(selectedCheckAllergy.get(i));
            if(cb != null && cb.isChecked()) {
                System.out.println(selectedCheckAllergy.get(i));
                allergiesSelected.add(selectedCheckAllergy.get(i));
            }

        }
        */

        UrlConnectorUpdateAllergies ur = new UrlConnectorUpdateAllergies();
        ur.setAllergies(selectedCheckAllergy);
        ur.execute();
        while(!ur.saved){}
        ur.cancel(true);
        finish();
        return true;
    }

    // Async + thread, class to make the connection to the server
    private class UrlConnectorUpdateAllergies extends AsyncTask<Integer,Integer,Integer> {

        ArrayList<String> allergiesSelected;
        public boolean saved = false;
        void setAllergies(ArrayList<String> allergies) {
            this.allergiesSelected = allergies;
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            System.out.println("USER " + settings.getString("UserMail",""));
            System.out.println("INGREDIENTS SELECCIONATS " + allergiesSelected);
            try {
                URL url = new URL("http://" + ipserver + "/api/resources/tastesAllergies/overrideIngredients");
                System.out.println(url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonString = new JSONObject()
                        .put("username", settings.getString("UserMail",""))
                        .put("ingredients", allergiesSelected)
                        .put("taste", false)
                        .toString();

                System.out.println(jsonString);
                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();
                System.out.println("HTTP CODE " + conn.getResponseCode());
                conn.disconnect();

            }catch (Exception e){
                System.out.println("Could not save allergies " + e);
            }
            saved = true;
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            saved = true;
            super.onPostExecute(result);

        }
    }


    // Async + thread, class to make the connection to the server
    private class UrlConnectorGenIngredientList extends AsyncTask<Integer,Integer,Integer> {
        ArrayList<String> ingredientList;
        ArrayList<String> userAllergies;
        public boolean loaded;

        ArrayList<String> getListOfIngredients() {
            return ingredientList;
        }
        ArrayList<String> getLoadedAllergiesList() { return userAllergies; }
        public UrlConnectorGenIngredientList(){
            loaded = false;
            userAllergies = new ArrayList<>();
            ingredientList = new ArrayList<>();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                URL url = new URL("http://" + ipserver  + "/api/resources/ingredients/excludingIngredientList");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");

                String jsonString = new JSONObject()
                        .put("username", settings.getString("UserMail","") )
                        .put("taste", false)
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
                    System.out.println("COULD NOT FIND INGREDIENTS");
                    return null;
                }
                conn.disconnect();

            } catch (Exception e) {
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
                        .put("taste", false)
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
                        userAllergies.add(ingredientName);
                    }
                    br.close();
                }else{
                    System.out.println("COULD NOT FIND INGREDIENTS");
                    return null;
                }
                conn.disconnect();

            } catch (Exception e) {
                System.out.println("Ingredients not found " + e);
            }
            loaded = true;

            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            loaded = true;
            System.out.println("INGREDIENTS CARGATS");
            super.onPostExecute(result);
        }
    }
}