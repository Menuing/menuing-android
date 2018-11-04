package com.example.mrg20.menuing_android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.TermsAndConditionActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.register_contact_admins);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Contact us: menuingapp@gmail.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button terms = (Button) findViewById(R.id.termsconditions);
        Button termsAndConditions = (Button) findViewById(R.id.register_btn);

        terms.setOnClickListener(this);
        termsAndConditions.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.termsconditions:
                intent = new Intent(RegisterActivity.this, TermsAndConditionActivity.class);
                break;
            case R.id.register_btn:
                intent = new Intent(RegisterActivity.this, MainPageActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
