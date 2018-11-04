package com.example.mrg20.menuing_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.MealDetails;

public class MealByIngredients extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_by_ingredients);

        Button saveIngredients = (Button) findViewById(R.id.saveIngredients);

        saveIngredients.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.saveIngredients:
                intent = new Intent(MealByIngredients.this, MealDetails.class);
                break;
        }
        startActivity(intent);
    }

}
