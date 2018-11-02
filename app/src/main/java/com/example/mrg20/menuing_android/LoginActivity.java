package com.example.mrg20.menuing_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mrg20.menuing_android.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button createAc = (Button) findViewById(R.id.createAc);
        Button loginbtn = (Button) findViewById(R.id.login_btn);

        createAc.setOnClickListener(this);
        loginbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.login_btn:
                intent = new Intent(LoginActivity.this, MainPageActivity.class);
                break;
            case R.id.createAc:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                break;
        }
        startActivity(intent);
    }
}
