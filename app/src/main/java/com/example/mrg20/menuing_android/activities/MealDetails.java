package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

public class MealDetails extends GlobalActivity implements View.OnClickListener {

    ImageView shoppingListIcon;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button recipe = (Button) findViewById(R.id.getRecipeButton1);
        recipe.setOnClickListener(this);
        recipe = (Button) findViewById(R.id.getRecipeButton2);
        recipe.setOnClickListener(this);

        shoppingListIcon = findViewById(R.id.meal_shopping_list_icon);
        shoppingListIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.getRecipeButton1:
                intent = new Intent(MealDetails.this, RecipeDetails.class);
                startActivity(intent);
                break;
            case R.id.getRecipeButton2:
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
