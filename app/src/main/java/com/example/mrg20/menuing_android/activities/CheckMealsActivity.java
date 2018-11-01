package com.example.mrg20.menuing_android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mrg20.menuing_android.R;

public class CheckMealsActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_previous_meals);

        Button b = (Button) findViewById(R.id.check_previous_meals_button);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.termsconditions_CrossButton:
                finish();
        }
    }
}
