package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mrg20.menuing_android.R;

public class RecipeDetails extends AppCompatActivity implements View.OnClickListener {

    ImageView shoppingList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        shoppingList = findViewById(R.id.recipe_shopping_list_icon);
        shoppingList.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()){
            case R.id.recipe_shopping_list_icon:
                intent = new Intent(RecipeDetails.this, ShoppingListActivity.class);
                break;
        }
    }
}
