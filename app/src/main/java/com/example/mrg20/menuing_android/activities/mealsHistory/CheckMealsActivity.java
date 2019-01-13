package com.example.mrg20.menuing_android.activities.mealsHistory;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mrg20.menuing_android.DatabaseHelper;
import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.MealDetails;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CheckMealsActivity extends GlobalActivity implements View.OnClickListener {

    private ListView lv;
    private List<String> historicReceptes;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mockup_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.listDB);

        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getData();

        historicReceptes = new ArrayList<>();
        while (cursor.moveToNext()) {
            addElementToList(cursor);
        }

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                historicReceptes
                );

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                  Intent in = new Intent(CheckMealsActivity.this, HistoryMealDetail.class);
                  in.putExtra("position", i);
                  vibrate();
                  startActivity(in);
              }
          }

        );
        db.close();
    }

    @Override
    public void onClick(View view) {
        vibrate();
    }


    private void addElementToList(Cursor cursor){
        historicReceptes.add(cursor.getString(1) + " " + cursor.getString(9) + "/5.0");
    }

}
