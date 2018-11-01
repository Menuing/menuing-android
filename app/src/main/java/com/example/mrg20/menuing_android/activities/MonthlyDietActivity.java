package com.example.mrg20.menuing_android.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import com.example.mrg20.menuing_android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonthlyDietActivity extends AppCompatActivity {

    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_diet);

        CalendarView calendarView = findViewById(R.id.monthlyView);

        Calendar calendarMin = Calendar.getInstance();
        calendarMin.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DATE));
        long dateMin = calendarMin.getTime().getTime();

        Calendar calendarMax = Calendar.getInstance();
        calendarMax.set(Calendar.DATE,Calendar.getInstance().getActualMaximum(Calendar.DATE));
        long dateMax = calendarMax.getTime().getTime();

        calendarView.setMinDate(dateMin);
        calendarView.setMaxDate(dateMax);

    }
}
