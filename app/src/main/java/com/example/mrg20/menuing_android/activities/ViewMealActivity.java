package com.example.mrg20.menuing_android.activities;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mrg20.menuing_android.R;

public class ViewMealActivity extends AppCompatActivity implements View.OnLongClickListener {

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_meal);

        listView = (ListView)findViewById(R.id.mealsList);
        listView.setOnLongClickListener(this);

        Bundle bundle = getIntent().getBundleExtra("data");
        int dia = bundle.getInt("dia");
        int mes = bundle.getInt("mes");
        int any = bundle.getInt("any");

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_daily_meal);
        String meal = "Lunch";
        String first = "Fish and chips";
        String acompaigment = "Salad";
        arrayAdapter.add(meal + "\n" + first + "\n" +acompaigment);

        meal = "Dinner";
        first = "Soup";
        acompaigment = "Grilled chicken";
        arrayAdapter.add(meal + "\n" + first + "\n" +acompaigment);
        listView.setAdapter(arrayAdapter);
        this.finish();
    }

    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence[] items = new CharSequence[2];
        items[0] = "Eliminar evento";
        items[1] = "Cancelar";
        builder.setTitle("title")
            .setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0){

                    }
                }
            });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }
}
