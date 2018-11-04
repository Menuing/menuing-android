package com.example.mrg20.menuing_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.CheckMealsActivity;
import com.example.mrg20.menuing_android.activities.HistoryFrag;
import com.example.mrg20.menuing_android.activities.UserProfile;


public class MainPageActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button getAMeal = (Button) findViewById(R.id.imageView1);
        Button checkMeals = (Button) findViewById(R.id.imageView2);
        Button nutri = (Button) findViewById(R.id.imageView3);
        Button historic = (Button) findViewById(R.id.imageView4);
        ImageView profile = (ImageView) findViewById(R.id.profile_logo);

        profile.setOnClickListener(this);
        getAMeal.setOnClickListener(this);
        checkMeals.setOnClickListener(this);
        nutri.setOnClickListener(this);
        historic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
