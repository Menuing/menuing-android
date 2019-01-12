package com.example.mrg20.menuing_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.MonthlyDietActivity;
import com.example.mrg20.menuing_android.activities.PaymentActivity;
import com.example.mrg20.menuing_android.activities.WeeklyDietActivity;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import java.util.Calendar;

public class MealScheduleActivity extends GlobalActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_schedule);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button day = (Button) findViewById(R.id.day);
        Button week = (Button) findViewById(R.id.week);
        Button month = (Button) findViewById(R.id.month);

        day.setOnClickListener(this);
        week.setOnClickListener(this);
        month.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        vibrate();
        Intent intent = null;
        switch(view.getId()) {
            case R.id.day:
                intent = new Intent(MealScheduleActivity.this, MealHour.class);
                intent.putExtra("DAY", Calendar.getInstance().getTime());
                startActivity(intent);
                break;
            case R.id.week:
                if(!premiumSettings.contains(settings.getString("UserMail", ""))){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    CharSequence []items = new CharSequence[2];
                    items[0] = getString(R.string.be_premium);
                    items[1] = getString(R.string.cancel);
                    builder.setTitle(getString(R.string.no_premium))
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which==0){
                                        Intent intent_popup = new Intent(MealScheduleActivity.this, PaymentActivity.class);
                                        startActivity(intent_popup);
                                    }
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else {
                    intent = new Intent(MealScheduleActivity.this, WeeklyDietActivity.class);
                    intent.putExtra("DAY", Calendar.getInstance().getTime());
                    startActivity(intent);
                }
                break;
            case R.id.month:
                intent = new Intent(MealScheduleActivity.this, MonthlyDietActivity.class);
                intent.putExtra("DAY", Calendar.getInstance().getTime());
                startActivity(intent);
                break;
        }
    }
}
