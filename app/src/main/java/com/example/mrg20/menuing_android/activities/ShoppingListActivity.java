package com.example.mrg20.menuing_android.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
//TODO EXTERMINAR AIXO

public class ShoppingListActivity extends GlobalActivity{
    ListView shoppingList;

    JSONObject recipe = null;

    JSONObject recipe1 = null;
    JSONObject recipe2 = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        if (getIntent().getExtras() != null) {
            try {
                if(getIntent().getStringExtra("recipe") == null) {
                    recipe1 = new JSONObject(getIntent().getStringExtra("recipe1"));
                    recipe2 = new JSONObject(getIntent().getStringExtra("recipe2"));
                }
                else{
                    recipe = new JSONObject(getIntent().getStringExtra("recipe"));
                }
            } catch (JSONException e){
                System.out.println("JERROR AL LLEGIR RECEPTES!!!1!");
            }
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        shoppingList = findViewById(R.id.shopping_list);

        ArrayList<String> ingredientList = new ArrayList<>();

        if(recipe != null){
            String s = "";
            try {
                s = recipe.getString("proportions");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[] chain = s.split(";");

            ingredientList.addAll(Arrays.asList(chain));
        }else{
            String s = "";
            String s2 = "";
            try {
                s = recipe1.getString("proportions");
                s2 = recipe2.getString("proportions");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[] chain = s.split(";");
            ingredientList.addAll(Arrays.asList(chain));
            chain = s2.split(";");
            ingredientList.addAll(Arrays.asList(chain));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, ingredientList);

        shoppingList.setAdapter(adapter);
    }
}
