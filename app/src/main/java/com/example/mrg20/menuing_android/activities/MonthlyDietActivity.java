package com.example.mrg20.menuing_android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import com.example.mrg20.menuing_android.MealHour;
import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import java.util.Calendar;

public class MonthlyDietActivity extends GlobalActivity implements CalendarView.OnDateChangeListener {

    CalendarView calendarView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_diet);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        CalendarView calendarView = findViewById(R.id.monthlyView);

        Calendar calendarMin = Calendar.getInstance();
        calendarMin.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DATE));
        long dateMin = calendarMin.getTime().getTime();

        Calendar calendarMax = Calendar.getInstance();
        calendarMax.set(Calendar.DATE,Calendar.getInstance().getActualMaximum(Calendar.DATE));
        long dateMax = calendarMax.getTime().getTime();

        calendarView.setMinDate(dateMin);
        calendarView.setMaxDate(dateMax);

        calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(CalendarView view, final int year, final int month, final int dayOfMonth) {
        vibrate();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence []items = new CharSequence[2];
        items[0] = getString(R.string.see_meal);
        items[1] = getString(R.string.cancel);

        builder.setTitle(getString(R.string.select_option))
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Intent intent = new Intent(MonthlyDietActivity.this,MealHour.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("dia", dayOfMonth);
                            bundle.putInt("mes", month);
                            bundle.putInt("any", year);
                            intent.putExtra("data", bundle);
                            startActivity(intent);
                        }else{
                            return;
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
