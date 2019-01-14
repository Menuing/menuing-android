package com.example.mrg20.menuing_android;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mrg20.menuing_android.activities.PaymentActivity;
import com.example.mrg20.menuing_android.activities.WeeklyDietActivity;
import com.example.mrg20.menuing_android.activities.mealsHistory.CheckMealsActivity;
import com.example.mrg20.menuing_android.activities.UserProfile;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;


public class MainPageActivity extends GlobalActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button getAMeal = (Button) findViewById(R.id.imageView1);
        Button checkMeals = (Button) findViewById(R.id.imageView2);
        Button nutri = (Button) findViewById(R.id.imageView3);
        Button historic = (Button) findViewById(R.id.imageView4);
        ImageView profile = (ImageView) findViewById(R.id.profile_logo);
        ImageView help = (ImageView) findViewById(R.id.profile_help);

        help.setOnClickListener(this);
        profile.setOnClickListener(this);
        getAMeal.setOnClickListener(this);
        checkMeals.setOnClickListener(this);
        nutri.setOnClickListener(this);
        historic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.profile_help:
                intent = new Intent(MainPageActivity.this, HelpActivity.class);
                break;
            case R.id.profile_logo:
                intent = new Intent(MainPageActivity.this, UserProfile.class);
                break;
            case R.id.imageView1:
                intent = new Intent(MainPageActivity.this, GetMealPageActivity.class);
                break;
            case R.id.imageView2:
                intent = new Intent(MainPageActivity.this, MealScheduleActivity.class);
                break;
            case R.id.imageView3:
                intent = new Intent(MainPageActivity.this, NutritionistsListActivity.class);
                break;
            case R.id.imageView4:
                //intent = new Intent(MainPageActivity.this, HistoryFrag.class);
                intent = new Intent(MainPageActivity.this, CheckMealsActivity.class);
                break;
        }
        startActivity(intent);
    }


}
