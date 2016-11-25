package io.github.rhemon.aggregatesieveanalysis;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.rhemon.aggregatesieveanalysis.data.ASADBContract;

import static android.R.attr.data;

/**
 * Created by rhemon on 9/21/16.
 */
public class DataEntryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    final String LOG_TAG = getClass().getSimpleName();
    Uri savedUri;
    Uri recievedUri;
    private int backButtonCount;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ASADBContract.ASADBEntry._ID,
                ASADBContract.ASADBEntry.COLUMN_ASADATA_TITLE,
                ASADBContract.ASADBEntry.COLUMN_ASADATA_DESC,
                ASADBContract.ASADBEntry.COLUMN_ASDADATA_JSONOBJECT
        };
        return new CursorLoader(this,
                recievedUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            setTitle("Edit - " + data.getString(data.getColumnIndex(ASADBContract.ASADBEntry.COLUMN_ASADATA_TITLE)));
            ((EditText) findViewById(R.id.dataTitle)).setText(data.getString(data.getColumnIndex(ASADBContract.ASADBEntry.COLUMN_ASADATA_TITLE)));
            ((EditText) findViewById(R.id.dataDesc)).setText(data.getString(data.getColumnIndex(ASADBContract.ASADBEntry.COLUMN_ASADATA_DESC)));
            try {
                JSONObject wtRtData = new JSONObject(data.getString(data.getColumnIndex(ASADBContract.ASADBEntry.COLUMN_ASDADATA_JSONOBJECT)));
                ((EditText) findViewById(R.id.wtRtatSn4)).setText(String.valueOf(wtRtData.getDouble("SIEVE_NO_4")));
                ((EditText) findViewById(R.id.wtRtatSn8)).setText(String.valueOf(wtRtData.getDouble("SIEVE_NO_8")));
                ((EditText) findViewById(R.id.wtRtatSn16)).setText(String.valueOf(wtRtData.getDouble("SIEVE_NO_16")));
                ((EditText) findViewById(R.id.wtRtatSn30)).setText(String.valueOf(wtRtData.getDouble("SIEVE_NO_30")));
                ((EditText) findViewById(R.id.wtRtatSn50)).setText(String.valueOf(wtRtData.getDouble("SIEVE_NO_50")));
                ((EditText) findViewById(R.id.wtRtatSn100)).setText(String.valueOf(wtRtData.getDouble("SIEVE_NO_100")));
                ((EditText) findViewById(R.id.wtRtatSn200)).setText(String.valueOf(wtRtData.getDouble("SIEVE_NO_200")));
                ((EditText) findViewById(R.id.wtRtatPan)).setText(String.valueOf(wtRtData.getDouble("PAN")));
            } catch (JSONException je) {
                Toast.makeText(this, "Unexpected error", Toast.LENGTH_SHORT);
                finish();
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((EditText) findViewById(R.id.dataTitle)).setText("");
        ((EditText) findViewById(R.id.dataDesc)).setText("");
        ((EditText) findViewById(R.id.wtRtatSn4)).setText(String.valueOf(""));
        ((EditText) findViewById(R.id.wtRtatSn8)).setText(String.valueOf(""));
        ((EditText) findViewById(R.id.wtRtatSn16)).setText(String.valueOf(""));
        ((EditText) findViewById(R.id.wtRtatSn30)).setText(String.valueOf(""));
        ((EditText) findViewById(R.id.wtRtatSn50)).setText(String.valueOf(""));
        ((EditText) findViewById(R.id.wtRtatSn100)).setText(String.valueOf(""));
        ((EditText) findViewById(R.id.wtRtatSn200)).setText(String.valueOf(""));
        ((EditText) findViewById(R.id.wtRtatPan)).setText(String.valueOf(""));
    }

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

        return wtRtData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (recievedUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    // Check all data entered
    private boolean checkedAndSaved() {

        String title = ((EditText) findViewById(R.id.dataTitle)).getText().toString();
        if (title == null) {
            Toast.makeText(this, "Title Required", Toast.LENGTH_LONG).show();
            return false;
        }
        String jsonObjection;
        try {
            jsonObjection = collectWtRetainedData().toString();
        } catch (JSONException je) {
            Log.e(LOG_TAG, "Something wrong in json");
            Toast.makeText(this, "Sorry, unexpected error.", Toast.LENGTH_LONG).show();
            return false;
        } catch (NumberFormatException ne) {
            Toast.makeText(this, "Please enter valid data", Toast.LENGTH_LONG).show();
            return false;
        }

        String desc = ((EditText) findViewById(R.id.dataDesc)).getText().toString();

        ContentValues values = new ContentValues();
        values.put(ASADBContract.ASADBEntry.COLUMN_ASADATA_TITLE, title);
        values.put(ASADBContract.ASADBEntry.COLUMN_ASADATA_DESC, desc);
        values.put(ASADBContract.ASADBEntry.COLUMN_ASDADATA_JSONOBJECT, jsonObjection);

        if (recievedUri == null) {
            savedUri = getContentResolver().insert(ASADBContract.ASADBEntry.CONTENT_URI, values);

            if (savedUri != null) {
                return true;
            } else {
                return false;
            }
        } else {
            int updatedRows = getContentResolver().update(recievedUri, values, null, null);
            savedUri = recievedUri;
            if (updatedRows <= 0) {
                return false;
            }
            return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (checkedAndSaved()) {
                    Intent resultIntent = new Intent(this, ResultActivity.class);
                    resultIntent.setData(savedUri);
                    startActivity(resultIntent);
                }
                return true;
            case R.id.action_delete:
                if (recievedUri != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Are you sure you want to delete this data? It will remove access permanently.");
                    builder.setTitle("Deleting data?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getContentResolver().delete(recievedUri, null, null);
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
            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Any unsaved changes will be discarded.");
                builder.setTitle("WARNING!");
                builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                builder.create().show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        recievedUri = getIntent().getData();

        if (recievedUri == null) {
            setTitle("Add Data");
        } else {
            Log.d(LOG_TAG, "HERE");
            getLoaderManager().initLoader(0, null, this);

            Log.d(LOG_TAG, "AFTER");
        }
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Any unsaved changes will be discarded.");
        builder.setTitle("Leaving?");
        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.create().show();
    }
}
