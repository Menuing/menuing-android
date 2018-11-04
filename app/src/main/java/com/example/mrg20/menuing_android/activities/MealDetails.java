package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mrg20.menuing_android.R;

public class MealDetails extends AppCompatActivity implements View.OnClickListener {

    ImageView shoppinListIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button recipe = (Button) findViewById(R.id.first_recipe);
        recipe.setOnClickListener(this);
        recipe = (Button) findViewById(R.id.first_recipe2);
        recipe.setOnClickListener(this);

        shoppinListIcon = findViewById(R.id.meal_shopping_list_icon);
        shoppinListIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.first_recipe:
                intent = new Intent(MealDetails.this, RecipeDetails.class);
                break;
            case R.id.first_recipe2:
                intent = new Intent(MealDetails.this, RecipeDetails.class);
                break;
            case R.id.meal_shopping_list_icon:
                intent = new Intent(MealDetails.this, ShoppingListActivity.class);
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
