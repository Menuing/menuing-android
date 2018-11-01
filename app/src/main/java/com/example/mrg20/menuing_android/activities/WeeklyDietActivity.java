package com.example.mrg20.menuing_android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.mrg20.menuing_android.R;

public class WeeklyDietActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weekly_diet);
    }

    @Override
    public void onClick(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence []items = new CharSequence[2];
        items[0] = "Ver";
        items[1] = "Cancelar";

        builder.setTitle("Selecciona una tarea")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Intent intent = new Intent(WeeklyDietActivity.this,ViewMealActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("dia", ((TextView)view.findViewWithTag("day")).getText().toString());
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
