package com.example.mrg20.menuing_android.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrg20.menuing_android.R;
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

public class TastesActivity extends GlobalActivity implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    private LinearLayout checkBoxLayout;

    private ListView tastesList;

    private List<String> tastesListString;

    ArrayAdapter<String> arrayAdapter;
    private List<String> selectedCheckTaste = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tastes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        checkBoxLayout = findViewById(R.id.selectedTastesCheckboxListLayout);

        fillTastesList();
        tastesList = (ListView) findViewById(R.id.tastesScrollView);
        populateList();
        tastesList.setOnItemClickListener(this);

    }

    /*@Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = settings.edit();
        for(int i = 0; i < tastesListString.size(); i++){
            editor.putString("Text"+i, tastesListString.get(i));
        }
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //fillTastesList();
        List<String> currentList = new ArrayList<>();
        boolean b = false;
        int i = 0;
        String s = null;

        while (!b){
            s = settings.getString("Text"+i, "");
            if(s.equals(""))
                b = true;
            else
                currentList.add(s);
            i++;
        }

        for(i = 0; i < currentList.size();i++){
            addElementToList(currentList.get(i));
        }

        for(i = 0; i < tastesListString.size();i++){
            if(currentList.contains(tastesListString.get(i))){
                tastesListString.remove(i);
            }
        }

        populateList();
    }*/

    private void populateList(){
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                tastesListString
        );
        tastesList.setAdapter(arrayAdapter);

    }

    private void fillTastesList() {
        tastesListString = new ArrayList<>();
        TastesActivity.UrlConnectorGenIngredientList ur = new TastesActivity.UrlConnectorGenIngredientList();
        ur.execute();
        while(!ur.loaded){}
        tastesListString = ur.getListOfIngredients();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        vibrate();
        addElementToList(tastesListString.get(position));
        tastesListString.remove(position);
        populateList();
    }

    private void addElementToList(String element){
        CheckBox ckbx = CheckboxUtils.createNewCheckBox(element, this);
        ckbx.setOnCheckedChangeListener(this);
        checkBoxLayout.addView(ckbx);
        selectedCheckTaste.add(element);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked){
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

        final ProgressDialog dialog = new ProgressDialog(this);
        //dialog.setMessage(getString(R.string.login_logging));
        dialog.setMessage("SAVING...");
        dialog.setCancelable(false);
        dialog.show();

        for(int i = 0; i < 1000; i++){
            dialog.setProgress((i/10) * 0);
        }


        ArrayList<String> tastesSelected = new ArrayList<>();
        for(int i = 0; i<selectedCheckTaste.size(); i++){
            CheckBox cb = checkBoxLayout.findViewWithTag(selectedCheckTaste.get(i));
            if(cb != null && cb.isChecked())
                tastesSelected.add(selectedCheckTaste.get(i));

        }

        System.out.println("LLISTA DE SELECCIONATS" + selectedCheckTaste);
        System.out.println("LLISTA DE CHECKEDS" + tastesSelected);
        UrlConnectorUpdateTastes ur = new UrlConnectorUpdateTastes();
        ur.setTastes(new ArrayList<>(selectedCheckTaste));
        ur.execute();

        finish();
        return true;
    }


    // Async + thread, class to make the connection to the server
    private class UrlConnectorUpdateTastes extends AsyncTask<Void,Void,Void> {

        ArrayList<String> tastesSelected;

        void setTastes(ArrayList<String> allergies) {
            this.tastesSelected = allergies;
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
                        if(tastesSelected.contains(ingredientName)) {
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
                            .put("taste", true)
                            .put("allergy", false)
                            .toString();

                    System.out.println(jsonString);
                    OutputStream os = conn.getOutputStream();
                    os.write(jsonString.getBytes());
                    os.flush();
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
