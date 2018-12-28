package com.example.mrg20.menuing_android;

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

import java.util.Calendar;
import java.util.Date;

public class MealHour extends GlobalActivity implements View.OnClickListener{

    Date date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_hour);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView day = (ImageView) findViewById(R.id.day);
        ImageView night = (ImageView) findViewById(R.id.night);

        date = new Date(getIntent().getStringExtra("DAY"));

        day.setOnClickListener(this);
        night.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.day:
                intent = new Intent(MealHour.this, MealDetails.class);
                break;
            case R.id.night:
                intent = new Intent(MealHour.this, MealDetails.class);
                break;

        }
        intent.putExtra("DAY", date);
        startActivity(intent);
    }
}
