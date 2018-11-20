package com.example.mrg20.menuing_android;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;

import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.example.mrg20.menuing_android.activities.TermsAndConditionActivity;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;


public class RegisterActivity extends GlobalActivity implements View.OnClickListener {

    private EditText registerUsername;
    private EditText registerPassword;
    private EditText registerEmail;

    private CheckBox acceptCheckBox;

    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.register_contact_admins);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
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
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.termsconditions:
                intent = new Intent(RegisterActivity.this, TermsAndConditionActivity.class);
                startActivity(intent);
                break;
            case R.id.register_btn:
                if (fieldsOK()) {
                    pref = PreferenceManager.getDefaultSharedPreferences(this);

                    postUserInfo(registerEmail.getText().toString(), registerPassword.getText().toString());
                    super.register(registerEmail.getText().toString(), registerPassword.getText().toString());
                }
                break;
        }
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
        else if (registerPassword.getText().toString().length() < 6){
            registerPassword.setError(getString(R.string.err_short_password));
        }//Camp password es massa curt
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


    void postUserInfo(String username, String password){
        UrlConnectorRegister ur = new UrlConnectorRegister();
        ur.setPw(password);
        ur.setUsername(username);
        ur.execute();
    }

    // Async + thread, class to make the connection to the server
    private class UrlConnectorRegister extends AsyncTask<Void,Void,Void> {

        String username;
        String pw;

        void setUsername(String username) {
            this.username = username;
        }

        void setPw(String pw) {
            this.pw = pw;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                URL url = new URL("http://" + ipserver  + "/api/resources/users");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonString = new JSONObject()
                        .put("username", username)
                        .put("password", pw)
                        .toString();

                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                System.out.println("Connection code " + conn.getResponseCode());
                System.out.println("Json string" + jsonString);
                conn.disconnect();

            } catch (Exception e) {
                System.out.println("User could not have been introduced to the database " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}

