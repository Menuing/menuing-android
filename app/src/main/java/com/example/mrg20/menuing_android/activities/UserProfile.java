package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.mrg20.menuing_android.R;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void seeAllergies(View view) {
        Intent intent = new Intent(UserProfile.this, AllergiesActivity.class);
        startActivity(intent);
    }

    public void seeTastes(View view) {
        Intent intent = new Intent(UserProfile.this, TastesActivity.class);
        startActivity(intent);
    }

    public void seeTermsAndConditions(View view) {
        Intent intent = new Intent(UserProfile.this, TermsAndConditionActivity.class);
        startActivity(intent);
    }

}
