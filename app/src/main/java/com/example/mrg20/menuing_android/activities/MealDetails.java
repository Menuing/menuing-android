package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;
import com.example.mrg20.menuing_android.other_classes.Recipe;

public class MealDetails extends GlobalActivity implements View.OnClickListener {

    ImageView shoppingListIcon;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button recipe = (Button) findViewById(R.id.first_recipe);
        recipe.setOnClickListener(this);
        recipe = (Button) findViewById(R.id.first_recipe2);
        recipe.setOnClickListener(this);

        shoppingListIcon = findViewById(R.id.meal_shopping_list_icon);
        shoppingListIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.first_recipe:
                intent = new Intent(MealDetails.this, RecipeDetails.class);
                startActivity(intent);
                break;
            case R.id.first_recipe2:
                intent = new Intent(MealDetails.this, RecipeDetails.class);
                startActivity(intent);
                break;
            case R.id.meal_shopping_list_icon:
                intent = new Intent(MealDetails.this, ShoppingListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
