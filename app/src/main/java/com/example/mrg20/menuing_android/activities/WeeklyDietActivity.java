package com.example.mrg20.menuing_android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.mrg20.menuing_android.MealHour;
import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class WeeklyDietActivity extends GlobalActivity implements TableLayout.OnClickListener {
    TableLayout tlFirst;
    TableLayout tlSecond;
    TableLayout tlThird;
    TableLayout tlForth;
    TableLayout tlFifth;
    TableLayout tlSixth;
    TableLayout tlSeventh;

    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtForth;
    TextView txtFifth;
    TextView txtSixth;
    TextView txtSeventh;
    
    List<TextView> txtBreakfasts;
    List<TextView> txtLunches1;
    List<TextView> txtLunches2;
    List<TextView> txtDinners;
    
    
    UrlConnectorGetWeeklyDiet ur;

    String mail;

    JSONArray diet;

    List<JSONObject> breakfasts;
    List<JSONObject> lunches1;
    List<JSONObject> lunches2;
    List<JSONObject> dinners;

    String firstDayDate;

            DateFormat dmyDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private SharedPreferences localSettings;

    final static String SETTINGS = "weekSettings";

    Date date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_diet);

        date = (Date)getIntent().getSerializableExtra("DAY");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        getAllFromFrontEnd();

        setThisWeekDays();

        setOnClickListeners();

        getDiet();

        parseRecipes();

        try {
            setRecipeNames();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setRecipeNames() throws JSONException {
        for (int i=0; i<breakfasts.size();i++){
            txtBreakfasts.get(i).setText(breakfasts.get(i).getString("name"));
            txtLunches1.get(i).setText(lunches1.get(i).getString("name"));
            txtLunches2.get(i).setText(lunches2.get(i).getString("name"));
            txtDinners.get(i).setText(dinners.get(i).getString("name"));
        }

    }

    private void getDiet() {
        try {
            diet = new JSONArray(getFromSharedPreferences());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(diet == null || diet.equals("")){
            diet = getNewWeeklyDiet();
        }
    }

    private void parseRecipes() {
        breakfasts = new ArrayList<>();
        lunches1 = new ArrayList<>();
        lunches2 = new ArrayList<>();
        dinners = new ArrayList<>();
        for (int i = 0; i < diet.length(); i++) {
            try {
                switch(i%4){
                    case 0:
                        breakfasts.add(diet.getJSONObject(i));
                        break;
                    case 1:
                        lunches1.add(diet.getJSONObject(i));
                        break;
                    case 2:
                        lunches2.add(diet.getJSONObject(i));
                        break;
                    case 3:
                        dinners.add(diet.getJSONObject(i));
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private JSONArray getNewWeeklyDiet() {
        ur = new UrlConnectorGetWeeklyDiet();
        ur.execute();
        while(!ur.loaded){if(ur.loaded)System.out.println(ur.loaded);}
        JSONArray newDiet = ur.getDiet();
        if(!newDiet.toString().equals("[]")) {
            SharedPreferences.Editor editor = localSettings.edit();
            editor.putString(mail + firstDayDate, newDiet.toString());
            editor.commit();
        }
        return newDiet;
    }

    private void setOnClickListeners() {
        tlFirst.setOnClickListener(this);
        tlSecond.setOnClickListener(this);
        tlThird.setOnClickListener(this);
        tlForth.setOnClickListener(this);
        tlFifth.setOnClickListener(this);
        tlSixth.setOnClickListener(this);
        tlSeventh.setOnClickListener(this);
    }

    private void getAllFromFrontEnd() {
        tlFirst = findViewById(R.id.first_column);
        tlSecond = findViewById(R.id.second_column);
        tlThird = findViewById(R.id.third_column);
        tlForth = findViewById(R.id.forth_column);
        tlFifth = findViewById(R.id.fifth_column);
        tlSixth = findViewById(R.id.sixth_column);
        tlSeventh = findViewById(R.id.seventh_column);

        txtFirst = findViewById(R.id.first);
        txtSecond = findViewById(R.id.second);
        txtThird = findViewById(R.id.third);
        txtForth = findViewById(R.id.forth);
        txtFifth = findViewById(R.id.fifth);
        txtSixth = findViewById(R.id.sixth);
        txtSeventh = findViewById(R.id.seventh);
        
        txtBreakfasts = new ArrayList<>();
        txtBreakfasts.add((TextView) findViewById(R.id.breakfast_first));
        txtBreakfasts.add((TextView) findViewById(R.id.breakfast_second));
        txtBreakfasts.add((TextView) findViewById(R.id.breakfast_third));
        txtBreakfasts.add((TextView) findViewById(R.id.breakfast_forth));
        txtBreakfasts.add((TextView) findViewById(R.id.breakfast_fifth));
        txtBreakfasts.add((TextView) findViewById(R.id.breakfast_sixth));
        txtBreakfasts.add((TextView) findViewById(R.id.breakfast_seventh));

        txtLunches1 = new ArrayList<>();
        txtLunches1.add((TextView) findViewById(R.id.lunch1_first));
        txtLunches1.add((TextView) findViewById(R.id.lunch1_second));
        txtLunches1.add((TextView) findViewById(R.id.lunch1_third));
        txtLunches1.add((TextView) findViewById(R.id.lunch1_forth));
        txtLunches1.add((TextView) findViewById(R.id.lunch1_fifth));
        txtLunches1.add((TextView) findViewById(R.id.lunch1_sixth));
        txtLunches1.add((TextView) findViewById(R.id.lunch1_seventh));
        
        txtLunches2 = new ArrayList<>();
        txtLunches2.add((TextView) findViewById(R.id.lunch2_first));
        txtLunches2.add((TextView) findViewById(R.id.lunch2_second));
        txtLunches2.add((TextView) findViewById(R.id.lunch2_third));
        txtLunches2.add((TextView) findViewById(R.id.lunch2_forth));
        txtLunches2.add((TextView) findViewById(R.id.lunch2_fifth));
        txtLunches2.add((TextView) findViewById(R.id.lunch2_sixth));
        txtLunches2.add((TextView) findViewById(R.id.lunch2_seventh));

        txtDinners = new ArrayList<>();
        txtDinners.add((TextView) findViewById(R.id.dinner_first));
        txtDinners.add((TextView) findViewById(R.id.dinner_second));
        txtDinners.add((TextView) findViewById(R.id.dinner_third));
        txtDinners.add((TextView) findViewById(R.id.dinner_forth));
        txtDinners.add((TextView) findViewById(R.id.dinner_fifth));
        txtDinners.add((TextView) findViewById(R.id.dinner_sixth));
        txtDinners.add((TextView) findViewById(R.id.dinner_seventh));
    }

    public void setThisWeekDays() {

        DateFormat df = new SimpleDateFormat("dd/MM");
        Calendar cal = Calendar. getInstance();
        cal.setTime(date);

        cal.setFirstDayOfWeek(Calendar.MONDAY);

        // 3. set calendars dOW field to the first dOW (last sunday)
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        firstDayDate = dmyDateFormat.format(cal.getTime());
        txtFirst.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        txtSecond.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        txtThird.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        txtForth.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        txtFifth.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        txtSixth.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        txtSeventh.setText(df.format(cal.getTime()));

    }

    private String getFromSharedPreferences() {
        localSettings  = getSharedPreferences(SETTINGS, 0);
        mail = settings.getString("UserMail", "");
        return localSettings.getString(mail+firstDayDate, "");
    }

    @Override
    public void onClick(final View view) {
        vibrate();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence []items = new CharSequence[2];
        items[0] = getString(R.string.see_meal);
        items[1] = getString(R.string.cancel);

        builder.setTitle(getString(R.string.select_option))
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Intent intent = new Intent(WeeklyDietActivity.this,MealHour.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("breakfast", breakfasts.get(which).toString());
                            bundle.putString("lunch1", lunches1.get(which).toString());
                            bundle.putString("lunch2", lunches2.get(which).toString());
                            bundle.putString("dinner", dinners.get(which).toString());
                            intent.putExtra("RECIPES", bundle);
                            startActivity(intent);
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Async + thread, class to make the connection to the server
    private class UrlConnectorGetWeeklyDiet extends AsyncTask<Void, Void, Void> {


        public boolean loaded = false;
        public boolean connection = true;

        private JSONArray thisDiet = new JSONArray();

        public JSONArray getDiet(){ return thisDiet;}

        HttpURLConnection conn;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                //GET ACTUAL USER ID
                URL url = new URL("http://" + ipserver + "/api/resources/recipes/getWeeklyDiet/?username=" + mail);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                int userID = -1;
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    thisDiet = new JSONArray(output);
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
