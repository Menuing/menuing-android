package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mrg20.menuing_android.R;

public class UserProfile extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button allergies = (Button) findViewById(R.id.my_allergies);
        Button tastes = (Button) findViewById(R.id.my_tastes);
        Button termsAndConditions = (Button) findViewById(R.id.info_terms_conditions);

        allergies.setOnClickListener(this);
        tastes.setOnClickListener(this);
        termsAndConditions.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.info_terms_conditions:
                intent = new Intent(UserProfile.this, TermsAndConditionActivity.class);
                break;
            case R.id.my_allergies:
                intent = new Intent(UserProfile.this, AllergiesActivity.class);
                break;
            case R.id.my_tastes:
                intent = new Intent(UserProfile.this, MealDetails.class);
                break;
        }
        startActivity(intent);
    }
}
