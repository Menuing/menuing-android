package com.example.mrg20.menuing_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
public class LoginActivity extends GlobalActivity implements View.OnClickListener {

    private EditText loginUsername;
    private EditText loginPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button createAc = (Button) findViewById(R.id.createAc);
        Button loginbtn = (Button) findViewById(R.id.login_btn);
        loginUsername = (EditText) findViewById(R.id.login_username);
        loginPassword = (EditText) findViewById(R.id.login_pw);

        createAc.setOnClickListener(this);
        loginbtn.setOnClickListener(this);

        if (savedInstanceState != null){
            loginUsername.setText(savedInstanceState.getString("user"));
            loginPassword.setText(savedInstanceState.getString("password"));
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (isLoggedIn()) loginAction();
    }// si ja estem logejats, que passi directament al menu principal

    private void loginAction(){
        Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("user",loginUsername.getText().toString());
        savedInstanceState.putString("password",loginPassword.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.login_btn:
                if (fieldsOK()) {
                    //TODO mirar que el mindundi estigui registrat
                    intent = new Intent(LoginActivity.this, MainPageActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.createAc:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    public boolean fieldsOK(){
        boolean result = false;

        if (TextUtils.isEmpty(loginUsername.getText().toString())){
            loginUsername.setError(getString(R.string.err_no_name));
        }//Camp nom buit
        else if (TextUtils.isEmpty(loginPassword.getText().toString())){
            loginPassword.setError(getString(R.string.err_no_password));
        }//Camp password buit
        else{
            result = true;
        }

        return result;
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
