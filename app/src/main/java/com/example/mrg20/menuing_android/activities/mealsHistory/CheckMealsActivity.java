package com.example.mrg20.menuing_android.activities.mealsHistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.MealDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CheckMealsActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv;
    private List<String> historicReceptes;
    ArrayAdapter<String> arrayAdapter;

    int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mockup_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        Button b = (Button) findViewById(R.id.add_previous_meals_button);
        b.setOnClickListener(this);
        b = (Button) findViewById(R.id.delete_previous_meals_button);
        b.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.listDB);

        historicReceptes = new ArrayList<String>();
        for(int i = 0; i < 5; i++){
            addElementToList();
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
                  Intent in = new Intent(CheckMealsActivity.this, MealDetails.class);
                  startActivity(in);
              }
          }

        );
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.add_previous_meals_button:
                addElementToList();
                arrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        historicReceptes
                );

                lv.setAdapter(arrayAdapter);
                break;

            case R.id.delete_previous_meals_button:
                removeElementFromList();
                arrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        historicReceptes
                );

                lv.setAdapter(arrayAdapter);
                break;
        }
    }

    private void removeElementFromList() {
        if(!historicReceptes.isEmpty()) {
            historicReceptes.remove(counter - 2);
            counter--;
        }else{
            Toast.makeText(this, "Pa que borres si ja esta buit", Toast.LENGTH_LONG).show();
        }
    }

    private void addElementToList(){
        if(counter%2 == 0)
            historicReceptes.add(counter + ": " + getRandomFood() + " " + getSecondHour() + ":" + getMinutes());
        else
            historicReceptes.add(counter + ": " + getRandomFood() + " " + getFirstHour() + ":" + getMinutes());
        counter++;
    }

    private String getRandomFood(){
        Random r = new Random();
        String result = "";
        switch(r.nextInt(5)) {
            case 0:
                result = "Cuttlefish";
                break;
            case 1:
                result = "Potatoes";
                break;
            case 2:
                result = "Salad";
                break;
            case 3:
                result = "Steak";
                break;
            case 4:
                result = "Pizza";
                break;
            default:
                result = "Nothing";
                break;
        }
        return result;
    }

    private int getFirstHour(){
        Random r = new Random();
        return r.nextInt(3)+12;
    }

    private int getSecondHour(){
        Random r = new Random();
        return r.nextInt(3)+20;
    }

    private int getMinutes(){
        Random r = new Random();
        return r.nextInt(5)*10;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
