package com.example.mrg20.menuing_android.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.ActionBar.LayoutParams;


import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.utils.CheckboxUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllergiesActivity  extends AppCompatActivity{

    private LinearLayout otherLayout;
    private EditText otherText;
    private Pattern onlyLettersAndSpaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        otherLayout = findViewById(R.id.othersLayout);
        otherText = (EditText)findViewById(R.id.otherText);
        onlyLettersAndSpaces = Pattern.compile("^[A-Za-z]+((\\s)*[A-Za-z])*$");
    }

    public void onClickOther (View v) {
        String newAllergy = "";
        // Create new check box in layout
        newAllergy = otherText.getText().toString();
        Matcher matcher = onlyLettersAndSpaces.matcher(newAllergy);
        if(matcher.matches()) {
            newAllergy = newAllergy.toLowerCase();
            String firstLetter = newAllergy.substring(0, 1).toUpperCase();
            newAllergy = firstLetter + newAllergy.substring(1);
            CheckBox ckbx = CheckboxUtils.createNewCheckBox(newAllergy, this);
            otherLayout.addView(ckbx);
            otherText.setText("");
        }else{
            Toast.makeText(this, "I'm not able to add void text", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
