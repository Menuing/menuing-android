package com.example.mrg20.menuing_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.MealDetails;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class MealHour extends GlobalActivity implements View.OnClickListener{

    Date date;
    String breakfast = "";
    String lunch1 = "";
    String lunch2 = "";
    String dinner = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_hour);

        if(progress == null || !progress.getContext().equals(this)) {
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.loading));
            //progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.setCancelable(false);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView morning = (ImageView) findViewById(R.id.morning);
        ImageView day = (ImageView) findViewById(R.id.day);
        ImageView night = (ImageView) findViewById(R.id.night);

        date = (Date)getIntent().getSerializableExtra("DAY");

        Bundle b = getIntent().getBundleExtra("RECIPES");
        if(b!=null) {
            breakfast = b.getString("breakfast");
            lunch1 = b.getString("lunch1");
            lunch2 = b.getString("lunch2");
            dinner = b.getString("dinner");
        }

        morning.setOnClickListener(this);
        day.setOnClickListener(this);
        night.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        super.progress.show();
        switch(view.getId()) {
            case R.id.morning:
                intent = new Intent(MealHour.this, MealDetails.class);
                intent.putExtra("TYPE", BREAKFAST);
                intent.putExtra("MODE", MEAL);
                intent.putExtra("RECIPE1", breakfast);
                break;
            case R.id.day:
                intent = new Intent(MealHour.this, MealDetails.class);
                intent.putExtra("TYPE", LUNCH);
                intent.putExtra("MODE", MEAL);
                intent.putExtra("RECIPE1", lunch1);
                intent.putExtra("RECIPE2", lunch2);
                break;
            case R.id.night:
                intent = new Intent(MealHour.this, MealDetails.class);
                intent.putExtra("TYPE", DINNER);
                intent.putExtra("MODE", RECIPE);
                intent.putExtra("RECIPE1", dinner);
                break;

        }
        intent.putExtra("DAY", date);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progress.cancel();
    }
}
