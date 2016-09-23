package io.github.rhemon.aggregatesieveanalysis;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rhemon on 9/23/16.
 */
public class ResultActivity extends AppCompatActivity{

    final String LOG_TAG = getClass().getSimpleName();
    JSONObject WeightRetained;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String wtRt = getIntent().getExtras().getString("wtRtData");
        try {
            WeightRetained = new JSONObject(wtRt);
            Log.d(LOG_TAG, WeightRetained.toString());
            JSONObject calculatedDatas = Utility.doAllCalcs(WeightRetained);
        } catch (JSONException je) {
            Log.d(LOG_TAG, "JSONException occurred in line 29 of ResultActivity.java");
        }
    }
}
