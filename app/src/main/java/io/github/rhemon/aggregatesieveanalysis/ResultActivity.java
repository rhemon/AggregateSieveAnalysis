package io.github.rhemon.aggregatesieveanalysis;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.rhemon.aggregatesieveanalysis.data.ASADBContract;


/**
 * Created by rhemon on 9/23/16.
 */
public class ResultActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    final String LOG_TAG = getClass().getSimpleName();
    JSONObject _tableData;
    JSONObject _xyData= new JSONObject();
    JSONObject _resultData = new JSONObject();
    int TAB_SELECTED = R.id.graphTab;
    FragmentManager _fm = getSupportFragmentManager();
    Uri contentUri;
    private int backButtonCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        contentUri = getIntent().getData();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.result_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent editIntent = new Intent(this, DataEntryActivity.class);
                editIntent.setData(contentUri);
                startActivity(editIntent);
                return true;
            case R.id.delete:
                if (contentUri != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Are you sure you want to delete this data? It will remove access permanently.");
                    builder.setTitle("Deleting data?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getContentResolver().delete(contentUri, null, null);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            return;
                        }
                    });
                    builder.create().show();
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void set_resultData (double d60, double d10) throws JSONException{
        _resultData.put("FINENESS_MODULUS", _tableData.getDouble("FINENESS_MODULUS"));
        _resultData.put("D60", d60);
        _resultData.put("D10", d10);
        Log.d(LOG_TAG, String.valueOf(d60) + " / " + String.valueOf(d10) + " = " + String.valueOf(d60/d10));
        _resultData.put("CU", SieveAnalysisCalc.CalcUniformityCoefficient(Double.parseDouble(String.format("%.2f", d60)), Double.parseDouble(String.format("%.2f", d10))));
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
                    ((FrameLayout) findViewById(R.id.fragmentHolder)).removeAllViews();
                    _fm.beginTransaction().replace(R.id.fragmentHolder, gf).commit();
                }
                TAB_SELECTED = R.id.graphTab;
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
                    ((FrameLayout) findViewById(R.id.fragmentHolder)).removeAllViews();
                    _fm.beginTransaction().replace(R.id.fragmentHolder, tf).commit();
                }
                TAB_SELECTED = R.id.tableTab;
                break;
            case R.id.resultTab:
                if (TAB_SELECTED != R.id.resultTab) {
                    // Changing tab color
                    findViewById(R.id.graphTab).setBackgroundColor(getResources().getColor(R.color.colorLightBlue500));
                    findViewById(R.id.tableTab).setBackgroundColor(getResources().getColor(R.color.colorLightBlue500));
                    findViewById(R.id.resultTab).setBackgroundColor(getResources().getColor(R.color.colorLightBlue700));
                    // Changing fragment
                    Bundle data = new Bundle();
                    data.putString("data", _resultData.toString());
                    ResultFragment rf = new ResultFragment();
                    rf.setArguments(data);
                    ((FrameLayout) findViewById(R.id.fragmentHolder)).removeAllViews();
                    _fm.beginTransaction().replace(R.id.fragmentHolder, rf).commit();
                }
                TAB_SELECTED = R.id.resultTab;
                break;
        }
    }

    private static JSONObject doAllCalcs(JSONObject data) throws JSONException{

        JSONObject result = new JSONObject();

        result.put("WEIGHT_RETAINED", data);

        double totalWeight = data.getDouble("SIEVE_NO_4") + data.getDouble("SIEVE_NO_8") + data.getDouble("SIEVE_NO_16") +
                data.getDouble("SIEVE_NO_30") + data.getDouble("SIEVE_NO_50") + data.getDouble("SIEVE_NO_100") +
                data.getDouble("SIEVE_NO_200") + data.getDouble("PAN");

        result.put("TOTAL_WEIGHT", totalWeight);

        // First find all the percent retained
        List<Double> percentRetainedValues = new ArrayList<Double>();
        percentRetainedValues.add(SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_4"), totalWeight));
        percentRetainedValues.add(SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_8"), totalWeight));
        percentRetainedValues.add(SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_16"), totalWeight));
        percentRetainedValues.add(SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_30"), totalWeight));
        percentRetainedValues.add(SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_50"), totalWeight));
        percentRetainedValues.add(SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_100"), totalWeight));
        percentRetainedValues.add(SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_200"), totalWeight));
        percentRetainedValues.add(SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("PAN"), totalWeight));
        result.put("PERCENT_RETAINED_VALUES", new JSONArray(percentRetainedValues));

        // find cumulative percent retained
        List<Double> cumulativePercents = new ArrayList<Double>();
        cumulativePercents.add(SieveAnalysisCalc.CaclCumulativePercentRetained((percentRetainedValues.subList(0, 1))));
        cumulativePercents.add(SieveAnalysisCalc.CaclCumulativePercentRetained(percentRetainedValues.subList(0, 2)));
        cumulativePercents.add(SieveAnalysisCalc.CaclCumulativePercentRetained(percentRetainedValues.subList(0, 3)));
        cumulativePercents.add(SieveAnalysisCalc.CaclCumulativePercentRetained(percentRetainedValues.subList(0, 4)));
        cumulativePercents.add(SieveAnalysisCalc.CaclCumulativePercentRetained(percentRetainedValues.subList(0, 5)));
        cumulativePercents.add(SieveAnalysisCalc.CaclCumulativePercentRetained(percentRetainedValues.subList(0, 6)));
        cumulativePercents.add(SieveAnalysisCalc.CaclCumulativePercentRetained(percentRetainedValues.subList(0, 7)));
        cumulativePercents.add(SieveAnalysisCalc.CaclCumulativePercentRetained(percentRetainedValues.subList(0, 8)));
        result.put("CUMULATIVE_PERCENTS", new JSONArray(cumulativePercents));

        // find percent finers
        List<Double> percentFiners = new ArrayList<Double>();
        percentFiners.add(SieveAnalysisCalc.CalcPercentFiner(cumulativePercents.get(0)));
        percentFiners.add(SieveAnalysisCalc.CalcPercentFiner(cumulativePercents.get(1)));
        percentFiners.add(SieveAnalysisCalc.CalcPercentFiner(cumulativePercents.get(2)));
        percentFiners.add(SieveAnalysisCalc.CalcPercentFiner(cumulativePercents.get(3)));
        percentFiners.add(SieveAnalysisCalc.CalcPercentFiner(cumulativePercents.get(4)));
        percentFiners.add(SieveAnalysisCalc.CalcPercentFiner(cumulativePercents.get(5)));
        percentFiners.add(SieveAnalysisCalc.CalcPercentFiner(cumulativePercents.get(6)));
        percentFiners.add(SieveAnalysisCalc.CalcPercentFiner(cumulativePercents.get(7)));
        result.put("PERCENT_FINER", new JSONArray(percentFiners));

        // find fineness modulus
        double finenessModulus = SieveAnalysisCalc.CalcFinenessModulus(cumulativePercents.subList(0, 6));
        result.put("FINENESS_MODULUS", finenessModulus);

        return result;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ASADBContract.ASADBEntry.COLUMN_ASADATA_TITLE,
                ASADBContract.ASADBEntry.COLUMN_ASDADATA_JSONOBJECT};
        return new CursorLoader(this,
                contentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()) {
            setTitle("Analysis - " + data.getString(data.getColumnIndex(ASADBContract.ASADBEntry.COLUMN_ASADATA_TITLE)));
            String wtRt = data.getString(data.getColumnIndex(ASADBContract.ASADBEntry.COLUMN_ASDADATA_JSONOBJECT));
            try {
                JSONObject WeightRetained = new JSONObject(wtRt);
                Log.d(LOG_TAG, WeightRetained.toString());
                _tableData = doAllCalcs(WeightRetained);
                Log.d(LOG_TAG, _xyData.toString());
                _xyData.put("xData", new JSONArray(Arrays.asList(4.75f, 2.36f, 1.18f, 0.6f, 0.3f, 0.15f, 0.075f)));
                _xyData.put("yData", _tableData.getJSONArray("PERCENT_FINER"));
                Bundle bundle_data = new Bundle();
                bundle_data.putString("data", _xyData.toString());
                GraphFragment gf = new GraphFragment();
                gf.setArguments(bundle_data);
                _fm.beginTransaction().add(R.id.fragmentHolder, gf).commit();
            } catch (JSONException je) {
                Log.d(LOG_TAG, "JSONException occurred in ResultActivity.java");
                je.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        finish();
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, MainActivity.class));
    }
}
