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
    private List<String> selectedCheckAllergies = new ArrayList<>();

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


    /***
     * Method to create the list of ingredients from database using a GET method
     */

    private void fillAllergiesList() {
        allergiesList = new ArrayList<String>();

        allergiesList.add("Select an element from the list");

        AllergiesActivity.UrlConnectorGenIngredientList ur = new AllergiesActivity.UrlConnectorGenIngredientList();
        ur.execute();
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Select an element from the list");

        while(!ur.loaded){}

        ingredients.addAll(ur.getListOfIngredients());
        allergiesList = ingredients;
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

        ckbx.setTag(element);
        allergiesCBLayout.addView(ckbx);
        selectedCheckAllergies.add(element);
    }

    //DE MOMENT NO SERVEIX DE RES, AMB LA BD POT SERVIR
    @Override
    public boolean onSupportNavigateUp() {
        vibrate();
        /*
        final ProgressDialog dialog = new ProgressDialog(this);
        //dialog.setMessage(getString(R.string.login_logging));
        dialog.setMessage("SAVING...");
        dialog.setCancelable(false);
        dialog.show();

        for(int i = 0; i < 1000; i++){
            dialog.setProgress((i/10) * 0);
        }
        */
        ArrayList<String> allergiesSelected = new ArrayList<>();
        for(int i = 0; i<selectedCheckAllergies.size(); i++){
            CheckBox cb = allergiesCBLayout.findViewWithTag(selectedCheckAllergies.get(i));
            if(cb.isChecked())
                allergiesSelected.add(selectedCheckAllergies.get(i));

        }
        System.out.println("LLISTA DE SELECCIONATS" + selectedCheckAllergies);
        System.out.println("LLISTA DE CHECKEDS" + allergiesSelected);
        UrlConnectorUpdateAllergies ur = new UrlConnectorUpdateAllergies();
        ur.setAllergies(allergiesSelected);
        ur.execute();

        finish();
        return true;
    }


    /***
     * Method to update allergies in database
     * @param allergiesSelected array of the names of the ingredients chose as allergies
     *                          If they are selected or not is not checked
     */
    void updateUserAllergies(ArrayList<String> allergiesSelected){
        AllergiesActivity.UrlConnectorUpdateAllergies ur = new AllergiesActivity.UrlConnectorUpdateAllergies();
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
                System.out.println(url);
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
                    System.out.println("USER: " + user);
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
                    JSONArray arr = new JSONArray(output);
                    for(int i = 0; i<arr.length(); i++){
                        String ingredientName = arr.getJSONObject(i).getString("name");
                        if(allergiesSelected.contains(ingredientName)) {
                            ingredientIds.add(arr.getJSONObject(i).getInt("id"));
                        }
                    }
                    System.out.println("INGREDIENTS: " + arr);
                }else{
                    System.out.println("COULD NOT FIND INGREDIENTS");
                    return null;
                }
                conn.disconnect();

                //UPDATE ALLERGIES IN DATABASE
                url = new URL("http://" + ipserver  + "/api/resources/tastesAllergies");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                for(int i=0; i<ingredientIds.size(); i++) {
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
                }
                System.out.println("CONNECTION CODE: " + conn.getResponseCode());
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("Allergies could not be saved " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }


    // Async + thread, class to make the connection to the server
    private class UrlConnectorGenIngredientList extends AsyncTask<Void,Void,Void> {
        ArrayList<String> ingredientList = new ArrayList<>();;
        public boolean loaded = false;

        ArrayList<String> getListOfIngredients() {
            return ingredientList;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                //GET INGREDIENT LIST AND COMPARE WITH THE ALLERGIES SELECTED
                URL url = new URL("http://" + ipserver  + "/api/resources/ingredients/all");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    for(int i = 0; i<arr.length(); i++){
                        String ingredientName = arr.getJSONObject(i).getString("name");
                        ingredientList.add(ingredientName);
                    }

                    loaded = true;
                }else{
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
