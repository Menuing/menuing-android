package com.example.mrg20.menuing_android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.TermsAndConditionActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText registerUsername;
    private EditText registerPassword;
    private EditText registerEmail;

    private CheckBox acceptCheckBox;

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
                Snackbar.make(view, getString(R.string.contact_gmail), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button terms = (Button) findViewById(R.id.termsconditions);
        Button termsAndConditions = (Button) findViewById(R.id.register_btn);
        registerUsername = (EditText) findViewById(R.id.register_username);
        registerPassword = (EditText) findViewById(R.id.register_pw);
        registerEmail = (EditText) findViewById(R.id.register_mail);
        acceptCheckBox = (CheckBox) findViewById(R.id.checkbox_meat);

        terms.setOnClickListener(this);
        termsAndConditions.setOnClickListener(this);

        if (savedInstanceState != null){
            registerUsername.setText(savedInstanceState.getString("user"));
            registerPassword.setText(savedInstanceState.getString("password"));
            registerEmail.setText(savedInstanceState.getString("mail"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("user",registerUsername.getText().toString());
        savedInstanceState.putString("password",registerPassword.getText().toString());
        savedInstanceState.putString("mail",registerEmail.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.termsconditions:
                intent = new Intent(RegisterActivity.this, TermsAndConditionActivity.class);
                startActivity(intent);
                break;
            case R.id.register_btn:
                if (fieldsOK()) {
                    intent = new Intent(RegisterActivity.this, MainPageActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
        //Si no entrem a termes i condicions, tanquem pantalla de registre
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public boolean fieldsOK(){
        boolean result = false;
        String mail = registerEmail.getText().toString();

        if (TextUtils.isEmpty(registerUsername.getText().toString())){
            registerUsername.setError(getString(R.string.err_no_name));
        }//Camp nom buit
        else if (TextUtils.isEmpty(registerPassword.getText().toString())){
            registerPassword.setError(getString(R.string.err_no_password));
        }//Camp password buit
        else if(TextUtils.isEmpty(mail)){
            registerEmail.setError(getString(R.string.err_no_mail));
        }//Camp mail buit
        else if (!isEmailValid(mail)){
            registerEmail.setError(getString(R.string.err_wrong_mail));
        }//Mail invalid (ex: qualsevol cosa que no sigui algo@algo.algo)
        else{
            if(acceptCheckBox.isChecked()) {
                result = true;
            }else {//Tot correcte
                acceptCheckBox.setError(getString(R.string.err_no_terms_and_conditions));
            }//No han acceptat termes i condicions
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
