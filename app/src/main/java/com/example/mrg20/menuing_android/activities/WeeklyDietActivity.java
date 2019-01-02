package com.example.mrg20.menuing_android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mrg20.menuing_android.MealHour;
import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.util.ByteArrayBuffer;


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

    UrlConnectorGetWeeklyDiet ur;

    String mail;

    JSONObject diet;

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

        setOnClickListeners();

        setThisWeekDays();

        try {
            diet = new JSONObject(getFromSharedPreferences());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(diet == null || diet.equals("")){
            diet = getNewWeeklyDiet();
        }

    }

    private JSONObject getNewWeeklyDiet() {
        ur = new UrlConnectorGetWeeklyDiet();
        ur.execute();
        while(!ur.loaded){if(ur.loaded)System.out.println(ur.loaded);}
        JSONObject newDiet = ur.getDiet();
        SharedPreferences.Editor editor = localSettings.edit();
        editor.putString(mail, newDiet.toString());
        editor.commit();
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
    }

    public void setThisWeekDays() {

        DateFormat df = new SimpleDateFormat("dd/MM");
        Calendar cal = Calendar. getInstance();
        cal.setTime(date);

        cal.setFirstDayOfWeek(Calendar.SUNDAY);

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
        return localSettings.getString(mail, "");
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
                            bundle.putString("dia", ((TextView)view.findViewWithTag("day")).getText().toString());
                            intent.putExtra("data", bundle);
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

        private JSONObject thisDiet = new JSONObject();

        public JSONObject getDiet(){ return thisDiet;}

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
                    JSONArray obj = new JSONArray(output);
                    thisDiet.put(firstDayDate, obj);
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
