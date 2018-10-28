package udl.menuing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);

        Button button1 = (Button) findViewById(R.id.historyBackButton);
        button1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.historyBackButton:
                //i = new Intent(MainActivity.this, ConfigActivity.class);
                i = new Intent(HistoryActivity.this, MainActivity.class);
                //i.putExtra("Night_mode", nightMode);
                //i.putExtra("Audio", audioEnabled);
                startActivity(i);
                finish();
                break;
        }
    }
}