package io.github.rhemon.aggregatesieveanalysis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rhemon on 9/23/16.
 */
public class Utility {

    public static JSONObject doAllCalcs(JSONObject data) throws JSONException{

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

}
