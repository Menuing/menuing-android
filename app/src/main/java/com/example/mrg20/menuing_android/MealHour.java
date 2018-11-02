package com.example.mrg20.menuing_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.MealDetails;

public class MealHour extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_hour);

        ImageView day = (ImageView) findViewById(R.id.day);
        ImageView night = (ImageView) findViewById(R.id.night);

        day.setOnClickListener(this);
        night.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.day:
                intent = new Intent(MealHour.this, MealDetails.class);
                break;
            case R.id.night:
                intent = new Intent(MealHour.this, MealDetails.class);
                break;

        }
        startActivity(intent);
    }
}
