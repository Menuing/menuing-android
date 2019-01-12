package com.example.mrg20.menuing_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mrg20.menuing_android.MainPageActivity;
import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.RegisterActivity;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

public class PaymentActivity extends GlobalActivity implements View.OnClickListener {

    TextView advantagesList;
    TextView advantagesList2;
    Button pay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        advantagesList = findViewById(R.id.advantages_list);
        advantagesList2 = findViewById(R.id.advantages_list2);
        pay = findViewById(R.id.pay);

        advantagesList.setText(R.string.bullted_advantages);
        advantagesList2.setText(R.string.bullted_advantages_2);

        pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pay){
            pay();
            Intent intent = new Intent(PaymentActivity.this, MainPageActivity.class);
            startActivity(intent);
        }
    }
}
