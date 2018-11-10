package com.example.mrg20.menuing_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mrg20.menuing_android.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

                EditText usernameEditText = (EditText) findViewById(R.id.login_username);
                String username = usernameEditText.getText().toString();

                EditText passwordEditText = (EditText) findViewById(R.id.login_pw);
                String password = usernameEditText.getText().toString();

                if(findUser(username,password)) {
                    intent = new Intent(LoginActivity.this, MainPageActivity.class);
                    startActivity(intent);
                }else {
                    TextView error = (TextView) findViewById(R.id.errorLogin);
                    error.setVisibility(TextView.VISIBLE);
                }
                break;

            case R.id.createAc:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }

    }

    boolean findUser(String username, String password){
        try {
            System.out.println("\n\nUSERNAME : " + username);
            URL url = new URL("http://localhost:8080/api/resources/users/?username=" + username); //GET USER BY USERNAME
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }


            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            if(conn.getResponseCode() == 200 || output != null){
                conn.disconnect();
                return true;
            }else{
                conn.disconnect();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e);
        }
        return false;
    }
}
