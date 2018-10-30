package com.example.mrg20.menuing_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.ActionBar.LayoutParams;

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
            CheckBox ckbx = createNewCheckBox(newAllergy);
            otherLayout.addView(ckbx);
            otherText.setText("");
        }else{
            Toast.makeText(this, "I'm not able to add void text", Toast.LENGTH_SHORT).show();
        }

    }


    private CheckBox createNewCheckBox(String text) {
        final LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        final CheckBox checkBox = new CheckBox(this);
        checkBox.setLayoutParams(lparams);
        checkBox.setText(text);
        checkBox.setChecked(true);
        checkBox.setTextAppearance(this, R.style.SmallText);
        return checkBox;
    }
}
