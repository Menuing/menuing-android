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

public class TastesActivity extends GlobalActivity implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher {

    private LinearLayout checkBoxLayout;

    private ListView tastesList;

    private List<String> allTastesList;
    private List<String> tastesListString;

    private List<String> loadedTastes;

    ArrayAdapter<String> arrayAdapter;
    private List<String> selectedCheckAllergy = new ArrayList<>();

    UrlConnectorGenIngredientList tUR;
    UrlConnectorUpdateTastes uUR;

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

        tUR = new UrlConnectorGenIngredientList();
        uUR = new UrlConnectorUpdateTastes();

        fillTastesList();
        tastesList = (ListView) findViewById(R.id.tastesScrollView);
        filterList("");
        tastesList.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tUR.cancel(false);
        uUR.cancel(true);
    }

    /***
     * Method to create the list of ingredients from database using a GET method
     */

    private void fillTastesList() {
        allTastesList = new ArrayList<String>();
        tastesListString = new ArrayList<String>();

        tUR.execute();
        while (!tUR.loaded) {}

        loadedTastes = tUR.getLoadedTastesList();
        for(int i = 0; i<loadedTastes.size();i++) addElementToList(loadedTastes.get(i));
        allTastesList = tUR.getListOfIngredients();
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

        ArrayList<String> tastesSelected = new ArrayList<>();
        for (int i = 0; i < selectedCheckAllergy.size(); i++) {
            CheckBox cb = checkBoxLayout.findViewWithTag(selectedCheckAllergy.get(i));
            if (cb != null && cb.isChecked())
                tastesSelected.add(selectedCheckAllergy.get(i));

        }

        uUR.setTastes(new ArrayList<>(selectedCheckAllergy));
        uUR.execute();

        finish();
        return true;
    }


    // Async + thread, class to make the connection to the server
    private class UrlConnectorUpdateTastes extends AsyncTask<Void, Void, Void> {

        ArrayList<String> tastesSelected;

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

            }catch (Exception E){
                System.out.println("Could not save allergies");
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }


    // Async + thread, class to make the connection to the server
    private class UrlConnectorGenIngredientList extends AsyncTask<Void, Void, Void> {
        ArrayList<String> ingredientList = new ArrayList<>();
        ArrayList<String> loadedTastesList = new ArrayList<>();
        public boolean loaded = false;

        ArrayList<String> getListOfIngredients() {
            return ingredientList;
        }
        ArrayList<String> getLoadedTastesList() { return loadedTastesList; }

        public UrlConnectorGenIngredientList(){
            loaded = false;
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
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}