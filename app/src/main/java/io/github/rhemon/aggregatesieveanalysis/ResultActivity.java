package io.github.rhemon.aggregatesieveanalysis;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
            JSONObject xyData  = new JSONObject();
            xyData.put("xData", new JSONArray(Arrays.asList(4.75f, 2.36f, 1.18f, 0.6f, 0.3f, 0.15f, 0.075f)));
            xyData.put("yData", calculatedDatas.getJSONArray("PERCENT_FINER"));
            Bundle data = new Bundle();
            data.putString("data", xyData.toString());
            GraphFragment gf = new GraphFragment();
            gf.setArguments(data);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.fragmentHolder, gf).commit();
        } catch (JSONException je) {
            Log.d(LOG_TAG, "JSONException occurred in ResultActivity.java");
            je.printStackTrace();
        }
    }
}
