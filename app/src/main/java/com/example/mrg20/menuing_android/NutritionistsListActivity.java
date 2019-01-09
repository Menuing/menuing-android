package com.example.mrg20.menuing_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

public class NutritionistsListActivity extends GlobalActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritionists_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayout menu_photos = (LinearLayout)findViewById(R.id.chat);
        LinearLayout menu_photos2 = (LinearLayout)findViewById(R.id.chat2);
        menu_photos.setOnClickListener(this);
        menu_photos2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.chat:
                intent = new Intent(NutritionistsListActivity.this, NutritionistChat.class);
                intent.putExtra("name", "Georgia Jordan");
                break;

            case R.id.chat2:
                intent = new Intent(NutritionistsListActivity.this, NutritionistChat.class);
                intent.putExtra("name", "Malcom mix");
                break;
        }
        startActivity(intent);
    }
}
