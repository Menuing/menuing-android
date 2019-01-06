package com.example.mrg20.menuing_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        Toast.makeText(getApplicationContext(), getString(R.string.login_logging), Toast.LENGTH_SHORT).show();
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
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.login_btn:
                if (fieldsOK()) {
                    /*
                    intent = new Intent(LoginActivity.this, MainPageActivity.class);
                    startActivity(intent);
                    finish();*/
                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage(getString(R.string.loading));
                    dialog.setProgress(0);
                    dialog.setCancelable(false);

                    Handler h = new Handler(){
                        @Override
                        public void handleMessage(Message msg){
                            switch (msg.what){
                                case 0:
                                    dialog.cancel();
                                    if ((Boolean) msg.obj) {
                                        loginAction();
                                    }else{
                                        Toast.makeText(getApplicationContext(), getString(R.string.err_login_fail), Toast.LENGTH_SHORT).show();
                                    }
                            }
                        }
                    };//Handler per esperar que acabi la Task el sistema, si no ho pot tallar abans de que acabi el login

                    super.signedIn(loginUsername.getText().toString(), loginPassword.getText().toString(), h);
                    dialog.show();
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
