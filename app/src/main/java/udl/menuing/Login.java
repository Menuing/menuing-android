package udl.menuing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity implements View.OnClickListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button loginButton = (Button)findViewById(R.id.loginButton);
       /* Button infoButton = (Button)findViewById(R.id.button2);
        Button dataButton = (Button)findViewById(R.id.button3);
        Button exitButton = (Button)findViewById(R.id.button4);*/
        loginButton.setOnClickListener(this);
        /*infoButton.setOnClickListener(this);
        dataButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);*/
    }

//    @Override
    public void onClick(View view) {
        //vibrate();
        Intent i;
        switch(view.getId()){
            case R.id.loginButton:
                //i = new Intent(MainActivity.this, ConfigActivity.class);
                i = new Intent(Login.this, MainActivity.class);
                //i.putExtra("Night_mode", nightMode);
                //i.putExtra("Audio", audioEnabled);
                startActivity(i);
                finish();
                break;
            /*case R.id.button2:
                i = new Intent(MainActivity.this, InfoActivity.class);
                i.putExtra("Night_mode", nightMode);
                i.putExtra("Audio", audioEnabled);
                startActivity(i);
                finish();
                break;
            case R.id.button3:
                i = new Intent(MainActivity.this, DataActivity.class);
                i.putExtra("Night_mode", nightMode);
                i.putExtra("Audio", audioEnabled);
                startActivity(i);
                break;
            case R.id.button4:
                finish();
                break;*/
        }
    }
}
