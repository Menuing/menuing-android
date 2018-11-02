package com.example.mrg20.menuing_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mrg20.menuing_android.R;

public class NutritionistsListActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritionists_list);

        LinearLayout menu_photos = (LinearLayout)findViewById(R.id.chat);
        menu_photos.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.chat:
                intent = new Intent(NutritionistsListActivity.this, NutritionistChat.class);
                break;
        }
        startActivity(intent);
    }
}
