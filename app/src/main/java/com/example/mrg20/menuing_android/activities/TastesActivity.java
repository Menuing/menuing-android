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

    ArrayAdapter<String> arrayAdapter;
    private List<String> selectedCheckAllergy = new ArrayList<>();


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
    }


    /***
     * Method to create the list of ingredients from database using a GET method
     */

    private void fillTastesList() {
        allTastesList = new ArrayList<String>();
        tastesListString = new ArrayList<String>();

        TastesActivity.UrlConnectorGenIngredientList ur = new TastesActivity.UrlConnectorGenIngredientList();
        ur.execute();
        while (!ur.loaded) {
        }
        allTastesList = ur.getListOfIngredients();
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


        ArrayList<String> tastesSelected = new ArrayList<>();
        for (int i = 0; i < selectedCheckAllergy.size(); i++) {
            CheckBox cb = checkBoxLayout.findViewWithTag(selectedCheckAllergy.get(i));
            if (cb != null && cb.isChecked())
                tastesSelected.add(selectedCheckAllergy.get(i));

        }
        UrlConnectorUpdateTastes ur = new UrlConnectorUpdateTastes();
        ur.setTastes(new ArrayList<>(selectedCheckAllergy));
        ur.execute();

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
            try {

                if (tastesSelected.size() == 0)
                    return null;

                //GET ACTUAL USER ID
                URL url = new URL("http://" + ipserver + "/api/resources/users/?username=" + settings.getString("UserMail", ""));
                System.out.println(url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                int userID = -1;
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    JSONObject user = arr.getJSONObject(0);
                    userID = user.getInt("id");
                    System.out.println("USER: " + user);
                    br.close();
                } else {
                    System.out.println("COULD NOT FIND USER");
                    return null;
                }

                conn.disconnect();
                if (userID == -1) {
                    System.out.println("USER NOT EXISTS");
                    return null;
                }
                //////////////////////////////////

                //GET INGREDIENT LIST AND COMPARE WITH THE tastes SELECTED
                ArrayList<Integer> ingredientIds = new ArrayList<>();
                url = new URL("http://" + ipserver + "/api/resources/ingredients/all");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    for (int i = 0; i < arr.length(); i++) {
                        String ingredientName = arr.getJSONObject(i).getString("name");
                        if (tastesSelected.contains(ingredientName)) {
                            ingredientIds.add(arr.getJSONObject(i).getInt("id"));
                        }
                    }
                    br.close();
                    System.out.println("INGREDIENTS: " + arr);
                } else {
                    System.out.println("COULD NOT FIND INGREDIENTS");
                    return null;
                }
                conn.disconnect();

                //UPDATE tastes IN DATABASE
                url = new URL("http://" + ipserver + "/api/resources/tastesAllergies");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                for (int i = 0; i < ingredientIds.size(); i++) {
                    JSONObject subjson = new JSONObject()
                            .put("userId", userID)
                            .put("ingredientId", ingredientIds.get(i));

                    String jsonString = new JSONObject()
                            .put("key", subjson)
                            .put("taste", false)
                            .put("allergy", true)
                            .toString();

                    System.out.println(jsonString);
                    OutputStream os = conn.getOutputStream();
                    os.write(jsonString.getBytes());
                    os.flush();
                    os.close();
                }
                System.out.println("CONNECTION CODE: " + conn.getResponseCode());
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("Tastes could not be saved " + e);
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
        ;
        public boolean loaded = false;

        ArrayList<String> getListOfIngredients() {
            return ingredientList;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                //GET INGREDIENT LIST AND COMPARE WITH THE tastes SELECTED
                URL url = new URL("http://" + ipserver + "/api/resources/ingredients/all");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    for (int i = 0; i < arr.length(); i++) {
                        String ingredientName = arr.getJSONObject(i).getString("name");
                        ingredientList.add(ingredientName);
                    }

                    br.close();
                    loaded = true;
                } else {
                    System.out.println("COULD NOT FIND INGREDIENTS");
                    return null;
                }
                conn.disconnect();

            } catch (Exception e) {
                System.out.println("User could not have been introduced to the database " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}