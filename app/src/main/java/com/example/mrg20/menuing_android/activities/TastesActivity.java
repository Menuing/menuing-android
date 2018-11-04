package com.example.mrg20.menuing_android.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.utils.CheckboxUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TastesActivity extends AppCompatActivity {
    private LinearLayout otherLayout;
    private EditText otherText;
    private Pattern onlyLettersAndSpaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tastes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        otherLayout = findViewById(R.id.othersLayout);
        otherText = (EditText)findViewById(R.id.otherText);
        onlyLettersAndSpaces = Pattern.compile("^[A-Za-z]+((\\s)*[A-Za-z])*$");
    }

    public void onClickOther (View v) {
        String newTaste = "";
        // Create new check box in layout
        newTaste = otherText.getText().toString();
        Matcher matcher = onlyLettersAndSpaces.matcher(newTaste);
        if(matcher.matches()) {
            newTaste = newTaste.toLowerCase();
            String firstLetter = newTaste.substring(0, 1).toUpperCase();
            newTaste = firstLetter + newTaste.substring(1);
            CheckBox ckbx = CheckboxUtils.createNewCheckBox(newTaste, this);
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
