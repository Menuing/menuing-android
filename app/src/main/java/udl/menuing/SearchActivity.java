package udl.menuing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        Button button1 = (Button)findViewById(R.id.searchLayoutButton);
        button1.setOnClickListener(this);
        Button button2 = (Button)findViewById(R.id.search_backButton);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch(view.getId()) {
            case R.id.search_backButton:
                //i = new Intent(MainActivity.this, ConfigActivity.class);
                i = new Intent(SearchActivity.this, MainActivity.class);
                //i.putExtra("Night_mode", nightMode);
                //i.putExtra("Audio", audioEnabled);
                startActivity(i);
                finish();
                break;
        }
    }
}
