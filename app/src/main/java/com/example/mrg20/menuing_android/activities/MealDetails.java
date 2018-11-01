package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mrg20.menuing_android.R;

public class MealDetails extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        Button recipe = (Button) findViewById(R.id.first_recipe);

        recipe.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.info_terms_conditions:
                intent = new Intent(MealDetails.this, RecipeDetails.class);
                break;
        }
        startActivity(intent);
    }
}
