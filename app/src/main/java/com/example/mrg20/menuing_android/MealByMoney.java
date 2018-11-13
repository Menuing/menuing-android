package com.example.mrg20.menuing_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.MealDetails;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

public class MealByMoney extends GlobalActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_by_money);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button saveMoney = (Button) findViewById(R.id.saveByMoney);

        saveMoney.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.saveByMoney:
                EditText eT = (EditText) findViewById(R.id.moneyText);
                if(eT.getText().toString().isEmpty()) {
                    eT.setError(getString(R.string.err_empty_field));
                }else {
                    intent = new Intent(MealByMoney.this, MealDetails.class);
                    startActivity(intent);
                    break;
                }
        }
    }
}
