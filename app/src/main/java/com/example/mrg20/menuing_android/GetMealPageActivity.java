package com.example.mrg20.menuing_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.MealDetails;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;


public class GetMealPageActivity extends GlobalActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_meal_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView nopreference = (ImageView) findViewById(R.id.nopref);
        ImageView healthy = (ImageView) findViewById(R.id.healthy);
        ImageView threeIngredients = (ImageView) findViewById(R.id.threeing);
        ImageView fastRecipe = (ImageView) findViewById(R.id.fasttodo);

        nopreference.setOnClickListener(this);
        healthy.setOnClickListener(this);
        threeIngredients.setOnClickListener(this);
        fastRecipe.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.nopref:
                intent = new Intent(GetMealPageActivity.this, MealDetails.class);
                intent.putExtra("TYPE", NO_PREFERENCES);
                break;
            case R.id.healthy://TODO: s'ha de fer botonet de COCKTAIL
                intent = new Intent(GetMealPageActivity.this, MealDetails.class);
                intent.putExtra("TYPE", COCKTAIL);
                break;
            case R.id.threeing:
                intent = new Intent(GetMealPageActivity.this, MealDetails.class);
                intent.putExtra("TYPE", THREE_INGREDIENTS);
                break;
            case R.id.fasttodo:
                intent = new Intent(GetMealPageActivity.this, MealDetails.class);
                intent.putExtra("TYPE", FAST_TO_DO);
                break;
        }
        intent.putExtra("MODE", RECIPE);
        startActivity(intent);
    }
}
