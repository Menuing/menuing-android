package com.example.mrg20.menuing_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.MonthlyDietActivity;
import com.example.mrg20.menuing_android.activities.WeeklyDietActivity;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

public class MealScheduleActivity extends GlobalActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_schedule);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button day = (Button) findViewById(R.id.day);
        Button week = (Button) findViewById(R.id.week);
        Button month = (Button) findViewById(R.id.month);

        day.setOnClickListener(this);
        week.setOnClickListener(this);
        month.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.day:
                intent = new Intent(MealScheduleActivity.this, MealHour.class);
                break;
            case R.id.week:
                intent = new Intent(MealScheduleActivity.this, WeeklyDietActivity.class);
                break;
            case R.id.month:
                intent = new Intent(MealScheduleActivity.this, MonthlyDietActivity.class);
                break;
        }
        startActivity(intent);
    }
}
