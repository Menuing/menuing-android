package com.example.mrg20.menuing_android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrg20.menuing_android.LoginActivity;
import com.example.mrg20.menuing_android.MainPageActivity;
import com.example.mrg20.menuing_android.MealScheduleActivity;
import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

public class UserProfile extends GlobalActivity implements View.OnClickListener{

    private EditText mailTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mailTextView = findViewById(R.id.email);

        String s = settings.getString("UserMail", "");
        if(!s.equals(""))
            mailTextView.setText(s);

        Button allergies = (Button) findViewById(R.id.my_allergies);
        Button tastes = (Button) findViewById(R.id.my_tastes);
        Button termsAndConditions = (Button) findViewById(R.id.user_termsconditions);
        Button cancelPremium = (Button) findViewById(R.id.cancelPrime);
        Button logout = (Button) findViewById(R.id.logoutButton);

        if(!premiumSettings.contains(settings.getString("UserMail",""))){
            cancelPremium.setVisibility(Button.GONE);
        }

        allergies.setOnClickListener(this);
        tastes.setOnClickListener(this);
        termsAndConditions.setOnClickListener(this);
        cancelPremium.setOnClickListener(this);
        if(logout!=null)
            logout.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        vibrate();
        finish();
        startActivity(new Intent(UserProfile.this, MainPageActivity.class));
        return true;
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.user_termsconditions:
                intent = new Intent(UserProfile.this, TermsAndConditionActivity.class);
                startActivity(intent);
                break;
            case R.id.my_allergies:
                intent = new Intent(UserProfile.this, AllergiesActivity.class);
                startActivity(intent);
                break;
            case R.id.my_tastes:
                intent = new Intent(UserProfile.this, TastesActivity.class);
                startActivity(intent);
                break;
            case R.id.cancelPrime:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                CharSequence []items = new CharSequence[2];
                items[0] = getString(R.string.yes);
                items[1] = getString(R.string.no);
                builder.setTitle(getString(R.string.sure))
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    deletePrime();
                                    Intent intentPopup = new Intent(UserProfile.this, UserProfile.class);
                                    intentPopup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intentPopup);
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.logoutButton:
                super.logout();
                intent = new Intent(UserProfile.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
