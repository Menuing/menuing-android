package com.example.mrg20.menuing_android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mrg20.menuing_android.MealHour;
import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class WeeklyDietActivity extends GlobalActivity implements TableLayout.OnClickListener {
    TableLayout tlFirst;
    TableLayout tlSecond;
    TableLayout tlThird;
    TableLayout tlForth;
    TableLayout tlFifth;
    TableLayout tlSixth;
    TableLayout tlSeventh;

    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtForth;
    TextView txtFifth;
    TextView txtSixth;
    TextView txtSeventh;

    private SharedPreferences settings;

    Date date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_diet);

        date = (Date)getIntent().getSerializableExtra("DAY");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tlFirst = findViewById(R.id.first_column);
        tlSecond = findViewById(R.id.second_column);
        tlThird = findViewById(R.id.third_column);
        tlForth = findViewById(R.id.forth_column);
        tlFifth = findViewById(R.id.fifth_column);
        tlSixth = findViewById(R.id.sixth_column);
        tlSeventh = findViewById(R.id.seventh_column);

        txtFirst = findViewById(R.id.first);
        txtSecond = findViewById(R.id.second);
        txtThird = findViewById(R.id.third);
        txtForth = findViewById(R.id.forth);
        txtFifth = findViewById(R.id.fifth);
        txtSixth = findViewById(R.id.sixth);
        txtSeventh = findViewById(R.id.seventh);

        tlFirst.setOnClickListener(this);
        tlSecond.setOnClickListener(this);
        tlThird.setOnClickListener(this);
        tlForth.setOnClickListener(this);
        tlFifth.setOnClickListener(this);
        tlSixth.setOnClickListener(this);
        tlSeventh.setOnClickListener(this);

        DateFormat df = new SimpleDateFormat("dd/MM");
        Calendar cal = Calendar. getInstance();
        cal.setTime(date);

        txtFirst.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        txtSecond.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        txtThird.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        txtForth.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        txtFifth.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        txtSixth.setText(df.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        txtSeventh.setText(df.format(cal.getTime()));

    }

    @Override
    public void onClick(final View view) {
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
                            Intent intent = new Intent(WeeklyDietActivity.this,MealHour.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("dia", ((TextView)view.findViewWithTag("day")).getText().toString());
                            intent.putExtra("data", bundle);
                            startActivity(intent);
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
