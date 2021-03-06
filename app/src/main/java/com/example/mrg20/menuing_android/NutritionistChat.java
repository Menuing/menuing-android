package com.example.mrg20.menuing_android;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mrg20.menuing_android.activities.MealDetails;
import com.example.mrg20.menuing_android.global_activities.GlobalActivity;

public class NutritionistChat extends GlobalActivity implements View.OnClickListener{

    Button sendButton;
    LinearLayout mainLayout;
    EditText userMsg;
    ScrollView scroll;
    TextView title;

    int i = 0;
    String[] msgs = {"Hello, how are you?", "What do you need?",
            "OK, I recommend you drink more water and take a B12 pill once a day. They are sold in pharmacies.",
            "Have a good week. Do you want to talk in 7 days to see how you are doing?",
            "Bye!"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritionist_chat);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        String name = getIntent().getStringExtra("name");


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sendButton = findViewById(R.id.send_button);
        mainLayout = findViewById(R.id.main_layout);
        userMsg = findViewById(R.id.user_msg);
        scroll = findViewById(R.id.Scroll);
        title = findViewById(R.id.nutritionistsListTittle);
        title.setText(name);

        sendButton.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        vibrate();
        switch(view.getId()) {
            case R.id.send_button:
                scroll.fullScroll(View.FOCUS_DOWN);
                String actualMsg = userMsg.getText().toString();
                if(!(actualMsg==null || actualMsg.trim().equals(""))){
                userMsg.setText("");
                userMsg.clearFocus();
                RelativeLayout new_nut_msg = new RelativeLayout(mainLayout.getContext());
                //new_nut_msg.setLayoutParams(nut_msg.getLayoutParams());

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(100, 20, 20, 20);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                new_nut_msg.setLayoutParams(layoutParams);
                new_nut_msg.setBackground(ContextCompat.getDrawable(this, R.color.colorPrimaryDark));

                mainLayout.addView(new_nut_msg);

                RelativeLayout.LayoutParams layoutParamstv = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParamstv.setMargins(100, 20, 20, 20);
                    layoutParamstv.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                TextView tv = new TextView(new_nut_msg.getContext());
                tv.setLayoutParams(layoutParamstv);
                tv.setBackground(ContextCompat.getDrawable(this, android.R.color.transparent));
                tv.setText(actualMsg);
                new_nut_msg.addView(tv);

                layoutParams.setMargins(20, 20, 100, 20);
                LinearLayout new_nut_msg2 = new LinearLayout(mainLayout.getContext());
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                new_nut_msg2.setLayoutParams(layoutParams);
                new_nut_msg2.setBackground(ContextCompat.getDrawable(this, R.color.colorBigText));
                new_nut_msg2.setOrientation(LinearLayout.HORIZONTAL);

                mainLayout.addView(new_nut_msg2);

                TextView tv2 = new TextView(new_nut_msg2.getContext());
                tv2.setTextColor(getResources().getColor(R.color.colorWhite));
                tv2.setLayoutParams(layoutParams);
                tv2.setBackground(ContextCompat.getDrawable(this, android.R.color.transparent));
                tv2.setText(msgs[i++]);
                if (i >= msgs.length) {
                    i = 0;
                }
                new_nut_msg2.addView(tv2);
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                scroll.fullScroll(View.FOCUS_DOWN);
                }
                 break;
        }
    }
}
