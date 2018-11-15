package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

public class RecipeDetails extends GlobalActivity implements View.OnClickListener {

    ImageView shoppingListIcon;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        shoppingListIcon = findViewById(R.id.recipe_shopping_list_icon);
        shoppingListIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()){
            case R.id.recipe_shopping_list_icon:
                intent = new Intent(RecipeDetails.this, ShoppingListActivity.class);
                break;
        }
        startActivity(intent);
    }
}
