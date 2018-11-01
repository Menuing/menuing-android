package com.example.mrg20.menuing_android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.ViewMealActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonthlyDietActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {

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

        calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(CalendarView view, final int year, final int month, final int dayOfMonth) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence []items = new CharSequence[2];
        items[0] = "Ver";
        items[1] = "Cancelar";

        builder.setTitle("Selecciona una tarea")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Intent intent = new Intent(MonthlyDietActivity.this,ViewMealActivity.class);
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
