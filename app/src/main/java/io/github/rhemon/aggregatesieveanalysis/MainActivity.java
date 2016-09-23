package io.github.rhemon.aggregatesieveanalysis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newDataButton = (Button) findViewById(R.id.newDataButton);
        newDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent entryIntent = new Intent(v.getContext(), DataEntryActivity.class);
                startActivity(entryIntent);
            }
        });
    }
}
