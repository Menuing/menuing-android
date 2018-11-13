package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

public class UserProfile extends GlobalActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button allergies = (Button) findViewById(R.id.my_allergies);
        Button tastes = (Button) findViewById(R.id.my_tastes);
        Button termsAndConditions = (Button) findViewById(R.id.user_termsconditions);

        allergies.setOnClickListener(this);
        tastes.setOnClickListener(this);
        termsAndConditions.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.user_termsconditions:
                intent = new Intent(UserProfile.this, TermsAndConditionActivity.class);
                break;
            case R.id.my_allergies:
                intent = new Intent(UserProfile.this, AllergiesActivity.class);
                break;
            case R.id.my_tastes:
                intent = new Intent(UserProfile.this, TastesActivity.class);
                break;
        }
        startActivity(intent);
    }
}
