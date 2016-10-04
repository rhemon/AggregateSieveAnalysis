package io.github.rhemon.aggregatesieveanalysis;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
    JSONObject _tableData;
    JSONObject _xyData;
    JSONObject _resultData;
    int TAB_SELECTED = R.id.graphTab;
    FragmentManager _fm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String wtRt = getIntent().getExtras().getString("wtRtData");
        try {
            JSONObject WeightRetained = new JSONObject(wtRt);
            Log.d(LOG_TAG, WeightRetained.toString());
            _tableData = Utility.doAllCalcs(WeightRetained);
            _xyData  = new JSONObject();
            _xyData.put("xData", new JSONArray(Arrays.asList(4.75f, 2.36f, 1.18f, 0.6f, 0.3f, 0.15f, 0.075f)));
            _xyData.put("yData", _tableData.getJSONArray("PERCENT_FINER"));
            Bundle data = new Bundle();
            data.putString("data", _xyData.toString());
            GraphFragment gf = new GraphFragment();
            gf.setArguments(data);
            _fm = getSupportFragmentManager();
            _fm.beginTransaction().add(R.id.fragmentHolder, gf).commit();
        } catch (JSONException je) {
            Log.d(LOG_TAG, "JSONException occurred in ResultActivity.java");
            je.printStackTrace();
        }
    }

    // Listener that will handle the fragments in result activity
    public void onTabSelection(View v){
        switch(v.getId()){
            case R.id.graphTab:
                if (TAB_SELECTED != R.id.graphTab){
                    // Changing tab color
                    findViewById(R.id.graphTab).setBackgroundColor(getResources().getColor(R.color.colorLightBlue700));
                    findViewById(R.id.tableTab).setBackgroundColor(getResources().getColor(R.color.colorLightBlue500));
                    findViewById(R.id.resultTab).setBackgroundColor(getResources().getColor(R.color.colorLightBlue500));
                    // Changing fragment
                    Bundle data = new Bundle();
                    data.putString("data", _xyData.toString());
                    GraphFragment gf = new GraphFragment();
                    gf.setArguments(data);
                    _fm.beginTransaction().replace(R.id.fragmentHolder, gf).commit();
                }
                break;
            case R.id.tableTab:
                if (TAB_SELECTED != R.id.tableTab){
                    // Changing tab color
                    findViewById(R.id.graphTab).setBackgroundColor(getResources().getColor(R.color.colorLightBlue500));
                    findViewById(R.id.tableTab).setBackgroundColor(getResources().getColor(R.color.colorLightBlue700));
                    findViewById(R.id.resultTab).setBackgroundColor(getResources().getColor(R.color.colorLightBlue500));
                    // Changing fragment
                    Bundle data = new Bundle();
                    data.putString("data", _tableData.toString());
                    TableFragment tf = new TableFragment();
                    tf.setArguments(data);
                    _fm.beginTransaction().replace(R.id.fragmentHolder, tf).commit();
                }
                break;
            case R.id.resultTab:
                if (TAB_SELECTED != R.id.resultTab) {
                    // Changing tab color
                    findViewById(R.id.graphTab).setBackgroundColor(getResources().getColor(R.color.colorLightBlue500));
                    findViewById(R.id.tableTab).setBackgroundColor(getResources().getColor(R.color.colorLightBlue500));
                    findViewById(R.id.resultTab).setBackgroundColor(getResources().getColor(R.color.colorLightBlue700));

                }
                break;
        }
    }
 }
