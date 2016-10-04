package io.github.rhemon.aggregatesieveanalysis;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by rhemon on 9/23/16.
 */
public class TableFragment extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.table_fragment, container, false);
        TableLayout tl = (TableLayout) view.findViewById(R.id.tableLayout);

        try {
            JSONObject data = new JSONObject(getArguments().getString("data"));

            for (int i = 0; i < 8; i++) {
                View tableRow = LayoutInflater.from(view.getContext()).inflate(R.layout.table_item_view, tl, false);

                String SieveNumberText = getSieveNumber(i);
                String SieveOpening = getSieveOpening(SieveNumberText);
                String WeightRetained;

                if (i < 7) {
                    WeightRetained = Double.toString(data.getJSONObject("WEIGHT_RETAINED").getDouble("SIEVE_NO_"+SieveNumberText.substring(9)));
                } else {
                    WeightRetained = Double.toString(data.getJSONObject("WEIGHT_RETAINED").getDouble("PAN"));
                }

                String PercentRetained = String.format("%.2f", Double.parseDouble(data.getJSONArray("PERCENT_RETAINED_VALUES").get(i).toString()));
                String CumulativePercent = String.format("%.2f", Double.parseDouble(data.getJSONArray("CUMULATIVE_PERCENTS").get(i).toString()));
                String PercentFiner = String.format("%.2f", Double.parseDouble(data.getJSONArray("PERCENT_FINER").get(i).toString()));
                ((TextView)tableRow.findViewById(R.id.SIEVE_NO)).setText(SieveNumberText);
                ((TextView)tableRow.findViewById(R.id.SIEVE_OPENING)).setText(SieveOpening);
                ((TextView)tableRow.findViewById(R.id.WEIGHT_RETAINED)).setText(WeightRetained);
                ((TextView)tableRow.findViewById(R.id.PERCENT_RETAINED)).setText(PercentRetained);
                ((TextView)tableRow.findViewById(R.id.CUMULATIVE_PERCENT)).setText(CumulativePercent);
                ((TextView)tableRow.findViewById(R.id.PERCENT_FINER)).setText(PercentFiner);
                tl.addView(tableRow);
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, "JSONException in file TableFragment.java");
        }



        return view;
    }

    private String getSieveNumber(int i) {
        String result;
        switch (i) {
            case 0:
                result = "Sieve No.4";
                break;
            case 1:
                result = "Sieve No.8";
                break;
            case 2:
                result = "Sieve No.16";
                break;
            case 3:
                result = "Sieve No.30";
                break;
            case 4:
                result = "Sieve No.50";
                break;
            case 5:
                result = "Sieve No.100";
                break;
            case 6:
                result = "Sieve No.200";
                break;
            case 7:
                result = "Pan";
                break;
            default:
                result = "-";
                break;
        }
        return result;
    }


    private String getSieveOpening(String sieve_no) {
        String result = "";
        switch(sieve_no) {
            case "Sieve No.4":
                result = "4.750";
                break;
            case "Sieve No.8":
                result = "2.360";
                break;
            case "Sieve No.16":
                result = "1.180";
                break;
            case "Sieve No.30":
                result = "0.600";
                break;
            case "Sieve No.50":
                result = "0.300";
                break;
            case "Sieve No.100":
                result = "0.150";
                break;
            case "Sieve No.200":
                result = "0.075";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }


}
