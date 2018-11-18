package com.example.mrg20.menuing_android.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllergiesActivity extends GlobalActivity implements AdapterView.OnItemSelectedListener{

    private LinearLayout allergiesCBLayout;
    private AppCompatSpinner spinner;
    private List<String> allergiesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        allergiesCBLayout = (LinearLayout) findViewById(R.id.allergiesCheckboxListLayout);
        spinner = (AppCompatSpinner) findViewById(R.id.allergiesSpinner);
        spinner.setOnItemSelectedListener(this);
        updateUserAllergies(new ArrayList<String>());
        fillAllergiesList();
        populateSpinner();
    }

    /*public void onClickOther (View v) {
        vibrate();
        String newAllergy = "";
        // Create new check box in layout
        newAllergy = newAllergy.toLowerCase();
        String firstLetter = newAllergy.substring(0, 1).toUpperCase();
        newAllergy = firstLetter + newAllergy.substring(1);
        CheckBox ckbx = CheckboxUtils.createNewCheckBox(newAllergy, this);
        allergiesCBLayout.addView(ckbx);
    }*/

    private void fillAllergiesList() {
        allergiesList = new ArrayList<>();
        allergiesList.add("Select an element from the list");

        /*
        allergiesList.add(getString(R.string.celery));
        allergiesList.add(getString(R.string.crustaceans));
        allergiesList.add(getString(R.string.fish));
        allergiesList.add(getString(R.string.gluten));
        allergiesList.add(getString(R.string.milk));
        allergiesList.add(getString(R.string.molluscs));
        allergiesList.add(getString(R.string.mustard));
        allergiesList.add(getString(R.string.nuts));
        allergiesList.add(getString(R.string.peanuts));
        allergiesList.add(getString(R.string.potatoes));
        allergiesList.add(getString(R.string.salmon));
        allergiesList.add(getString(R.string.sesame));
        allergiesList.add(getString(R.string.soy));
        allergiesList.add(getString(R.string.vegetables));
        allergiesList.add(getString(R.string.monday));
        */
    }

    private void populateSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, allergiesList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView t = findViewById(R.id.allergiesSaveStatusTextView);
        t.setText("");
        if(parent.equals(findViewById(R.id.allergiesSpinner)) && position > 0 && id > 0) {
            addElementToList(allergiesList.get(position));
            allergiesList.remove(position);
            if(allergiesList.size() == 1)
                allergiesList.set(0, "THE LIST IS EMPTY");
            populateSpinner();
        }
        else{
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void addElementToList(String element){
        CheckBox ckbx = CheckboxUtils.createNewCheckBox(element, this);
        allergiesCBLayout.addView(ckbx);
    }

    //DE MOMENT NO SERVEIX DE RES, AMB LA BD POT SERVIR
    @Override
    public boolean onSupportNavigateUp() {
        vibrate();

        final ProgressDialog dialog = new ProgressDialog(this);
        //dialog.setMessage(getString(R.string.login_logging));
        dialog.setMessage("SAVING...");
        dialog.setCancelable(false);
        dialog.show();

        for(int i = 0; i < 1000; i++){
            dialog.setProgress((i/10) * 0);
        }

        finish();
        return true;
    }


    void updateUserAllergies(ArrayList<String> allergiesSelected){
        AllergiesActivity.UrlConnectorUpdateAllergies ur = new AllergiesActivity.UrlConnectorUpdateAllergies();
        allergiesSelected.add("pastesdino");
        ur.setAllergies(allergiesSelected);
        ur.execute();
    }

    // Async + thread, class to make the connection to the server
    private class UrlConnectorUpdateAllergies extends AsyncTask<Void,Void,Void> {

        ArrayList<String> allergiesSelected;

        void setAllergies(ArrayList<String> allergies) {
            this.allergiesSelected = allergies;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                //GET ACTUAL USER ID
                URL url = new URL("http://" + ipserver  + "/api/resources/users/?username=" + settings.getString("UserMail",""));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                int userID;
                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    JSONObject user = arr.getJSONObject(0);
                    userID = user.getInt("id");
                }else{
                    System.out.println("COULD NOT FIND USER");
                    return null;
                }
                conn.disconnect();
                //////////////////////////////////

                //GET INGREDIENT LIST AND COMPARE WITH THE ALLERGIES SELECTED
                ArrayList<Integer> ingredientIds = new ArrayList<>();
                url = new URL("http://" + ipserver  + "/api/resources/ingredients/all");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    System.out.println(output);
                    JSONArray arr = new JSONArray(output);
                    for(int i = 0; i<arr.length(); i++){
                        String ingredientName = arr.getJSONObject(i).getString("name");
                        if(allergiesSelected.contains(ingredientName)) {
                            ingredientIds.add(arr.getJSONObject(i).getInt("id"));
                        }
                    }
                }else{
                    System.out.println("COULD NOT FIND INGREDIENTS");
                    return null;
                }
                conn.disconnect();

                /*
                //UPDATE ALLERGIES IN DATABASE
                url = new URL("http://" + ipserver  + "/api/resources/tastesAllergies");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                for(int i=0; i<ingredientIds.size(); i++) {
                    String jsonSubstring = new JSONObject()
                            .put("userID", userID)
                            .put("ingredientId", ingredientIds.get(i))
                            .toString();

                    String jsonString = new JSONObject()
                            .put("key", jsonSubstring)
                            .put("taste", false)
                            .put("allergy", true)
                            .toString();

                    System.out.println(jsonString);
                    OutputStream os = conn.getOutputStream();
                    os.write(jsonString.getBytes());
                    os.flush();
                }
                System.out.println("CONNECTION CODE: " + conn.getResponseCode());
                conn.disconnect();
                */


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
