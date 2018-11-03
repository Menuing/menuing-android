package com.example.mrg20.menuing_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.MealDetails;


public class GetMealPageActivity extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_meal_page);

        ImageView nopreference = (ImageView) findViewById(R.id.img1);
        ImageView ingredients = (ImageView) findViewById(R.id.img2);
        ImageView money = (ImageView) findViewById(R.id.img3);

        nopreference.setOnClickListener(this);
        ingredients.setOnClickListener(this);
        money.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.imageView1:
                intent = new Intent(GetMealPageActivity.this, MealDetails.class);
                break;
            case R.id.imageView2:
                intent = new Intent(GetMealPageActivity.this, MealScheduleActivity.class);
                break;
            case R.id.imageView3:
                intent = new Intent(GetMealPageActivity.this, NutritionistsListActivity.class);
                break;
        }
        startActivity(intent);
    }
}
