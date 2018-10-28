package udl.menuing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tastes_screen_layout);

        //Button bButton = (Button)findViewById(R.id.configBackButton);
        //bButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch(view.getId()) {
            case R.id.configBackButton:
                //i = new Intent(MainActivity.this, ConfigActivity.class);
                i = new Intent(ConfigActivity.this, MainActivity.class);
                //i.putExtra("Night_mode", nightMode);
                //i.putExtra("Audio", audioEnabled);
                startActivity(i);
                finish();
                break;
        }
    }
}
