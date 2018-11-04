package com.example.mrg20.menuing_android.activities;

import android.content.DialogInterface;
import android.content.Intent;
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


public class WeeklyDietActivity extends AppCompatActivity implements TableLayout.OnClickListener {
    TableLayout tlMonday;
    TableLayout tlTuesday;
    TableLayout tlWednesday;
    TableLayout tlThursday;
    TableLayout tlFriday;
    TableLayout tlSaturday;
    TableLayout tlSunday;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_diet);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tlMonday = findViewById(R.id.monday_column);
        tlTuesday = findViewById(R.id.tuesday_column);
        tlWednesday = findViewById(R.id.wednesday_column);
        tlThursday = findViewById(R.id.thursday_column);
        tlFriday = findViewById(R.id.friday_column);
        tlSaturday = findViewById(R.id.saturday_column);
        tlSunday = findViewById(R.id.sunday_column);

        tlMonday.setOnClickListener(this);
        tlTuesday.setOnClickListener(this);
        tlWednesday.setOnClickListener(this);
        tlThursday.setOnClickListener(this);
        tlFriday.setOnClickListener(this);
        tlSaturday.setOnClickListener(this);
        tlSunday.setOnClickListener(this);

    }

    @Override
    public void onClick(final View view) {
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
