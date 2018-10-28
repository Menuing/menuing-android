package udl.menuing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button)findViewById(R.id.mainSearchButton);
        button1.setOnClickListener(this);
        Button button2 = (Button)findViewById(R.id.mainHistoryButton);
        button2.setOnClickListener(this);
        Button button3 = (Button)findViewById(R.id.mainExitButton);
        button3.setOnClickListener(this);
        Button button4 = (Button)findViewById(R.id.mainConfigButton);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch(view.getId()){
            case R.id.mainSearchButton:
                //i = new Intent(MainActivity.this, ConfigActivity.class);
                i = new Intent(MainActivity.this, SearchActivity.class);
                //i.putExtra("Night_mode", nightMode);
                //i.putExtra("Audio", audioEnabled);
                startActivity(i);
                finish();
                break;
            case R.id.mainHistoryButton:
                i = new Intent(MainActivity.this, HistoryActivity.class);
                //i.putExtra("Night_mode", nightMode);
                //i.putExtra("Audio", audioEnabled);
                startActivity(i);
                finish();
                break;
            case R.id.mainConfigButton:
                //i = new Intent(MainActivity.this, ConfigActivity.class);
                i = new Intent(MainActivity.this, ConfigActivity.class);
                //i.putExtra("Night_mode", nightMode);
                //i.putExtra("Audio", audioEnabled);
                startActivity(i);
                finish();
                break;
            case R.id.mainExitButton:
                finish();
                break;
        }
    }
}
