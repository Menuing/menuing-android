package com.example.mrg20.menuing_android.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;
import com.example.mrg20.menuing_android.utils.CheckboxUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TastesActivity extends GlobalActivity implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    private LinearLayout checkBoxLayout;

    private ListView tastesList;

    private List<String> tastesListString;

    ArrayAdapter<String> arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tastes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        checkBoxLayout = findViewById(R.id.selectedTastesCheckboxListLayout);

        fillTastesList();
        tastesList = (ListView) findViewById(R.id.tastesScrollView);
        populateList();
        tastesList.setOnItemClickListener(this);

    }

    /*@Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = settings.edit();
        for(int i = 0; i < tastesListString.size(); i++){
            editor.putString("Text"+i, tastesListString.get(i));
        }
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //fillTastesList();
        List<String> currentList = new ArrayList<>();
        boolean b = false;
        int i = 0;
        String s = null;

        while (!b){
            s = settings.getString("Text"+i, "");
            if(s.equals(""))
                b = true;
            else
                currentList.add(s);
            i++;
        }

        for(i = 0; i < currentList.size();i++){
            addElementToList(currentList.get(i));
        }

        for(i = 0; i < tastesListString.size();i++){
            if(currentList.contains(tastesListString.get(i))){
                tastesListString.remove(i);
            }
        }

        populateList();
    }*/

    private void populateList(){
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                tastesListString
        );
        tastesList.setAdapter(arrayAdapter);

    }

    private void fillTastesList() {
        tastesListString = new ArrayList<>();
        tastesListString.add(getString(R.string.celery));
        tastesListString.add(getString(R.string.crustaceans));
        tastesListString.add(getString(R.string.fish));
        tastesListString.add(getString(R.string.gluten));
        tastesListString.add(getString(R.string.milk));
        tastesListString.add(getString(R.string.molluscs));
        tastesListString.add(getString(R.string.mustard));
        tastesListString.add(getString(R.string.nuts));
        tastesListString.add(getString(R.string.peanuts));
        tastesListString.add(getString(R.string.potatoes));
        tastesListString.add(getString(R.string.salmon));
        tastesListString.add(getString(R.string.sesame));
        tastesListString.add(getString(R.string.soy));
        tastesListString.add(getString(R.string.vegetables));
        tastesListString.add(getString(R.string.monday));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        vibrate();
        addElementToList(tastesListString.get(position));
        tastesListString.remove(position);
        populateList();
    }

    private void addElementToList(String element){
        CheckBox ckbx = CheckboxUtils.createNewCheckBox(element, this);
        ckbx.setOnCheckedChangeListener(this);
        checkBoxLayout.addView(ckbx);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked){
            tastesListString.add(buttonView.getText().toString());

            Collections.sort(tastesListString, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            checkBoxLayout.removeView(buttonView);
            populateList();
        }
    }

    //DE MOMENT NO SERVEIX DE RES, AMB LA BD POT SERVIR
    @Override
    public boolean onSupportNavigateUp() {
        vibrate();

        final ProgressDialog dialog = new ProgressDialog(this);
        //dialog.setMessage(getString(R.string.login_logging));
        dialog.setMessage("SAVING...");
        dialog.setCancelable(false);
        dialog.show();

        for(int i = 0; i < 1000; i++){
            dialog.setProgress((i/10) * 0);
        }

        finish();
        return true;
    }
}
