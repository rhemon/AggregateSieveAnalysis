package io.github.rhemon.aggregatesieveanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rhemon on 9/21/16.
 */
public class DataEntryActivity extends AppCompatActivity {

    final String LOG_TAG = getClass().getSimpleName();

    private class WeightRetainedValues {
        final double SIEVE_NO_4 = Double.parseDouble(((EditText) findViewById(R.id.wtRtatSn4)).getText().toString());
        final double SIEVE_NO_8 = Double.parseDouble(((EditText) findViewById(R.id.wtRtatSn8)).getText().toString());
        final double SIEVE_NO_16 = Double.parseDouble(((EditText) findViewById(R.id.wtRtatSn16)).getText().toString());
        final double SIEVE_NO_30 = Double.parseDouble(((EditText) findViewById(R.id.wtRtatSn30)).getText().toString());
        final double SIEVE_NO_50 = Double.parseDouble(((EditText) findViewById(R.id.wtRtatSn50)).getText().toString());
        final double SIEVE_NO_100 =  Double.parseDouble(((EditText) findViewById(R.id.wtRtatSn100)).getText().toString());
        final double SIEVE_NO_200 = Double.parseDouble(((EditText) findViewById(R.id.wtRtatSn200)).getText().toString());
        final double PAN = Double.parseDouble(((EditText) findViewById(R.id.wtRtatPan)).getText().toString());

    }


    private JSONObject collectWtRetainedData() throws JSONException, NumberFormatException{
        JSONObject wtRtData = new JSONObject();

        WeightRetainedValues fields = new WeightRetainedValues();
        wtRtData.put("SIEVE_NO_4", fields.SIEVE_NO_4);
        wtRtData.put("SIEVE_NO_8", fields.SIEVE_NO_8);
        wtRtData.put("SIEVE_NO_16", fields.SIEVE_NO_16);
        wtRtData.put("SIEVE_NO_30", fields.SIEVE_NO_30);
        wtRtData.put("SIEVE_NO_50", fields.SIEVE_NO_50);
        wtRtData.put("SIEVE_NO_100", fields.SIEVE_NO_100);
        wtRtData.put("SIEVE_NO_200", fields.SIEVE_NO_200);
        wtRtData.put("PAN", fields.PAN);
        findViewById(R.id.errorMsg).setVisibility(View.INVISIBLE);

        return wtRtData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        Button enterDataButton = (Button) findViewById(R.id.enterDataButton);
        enterDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String values = collectWtRetainedData().toString();
                    Intent resultActivity = new Intent(v.getContext(), ResultActivity.class);
                    resultActivity.putExtra("wtRtData", values);
                    startActivity(resultActivity);
                } catch (JSONException je) {
                    Log.d(LOG_TAG, "Some kind of error occurred during collecting data (at line 66 of DataEntryActivity.java");
                } catch (NumberFormatException ne) {
                    findViewById(R.id.errorMsg).setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
