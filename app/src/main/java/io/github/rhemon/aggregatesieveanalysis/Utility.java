package io.github.rhemon.aggregatesieveanalysis;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rhemon on 9/23/16.
 */
public class Utility {

    public static JSONObject doAllCalcs(JSONObject data) throws JSONException{

        JSONObject result = new JSONObject();
        double totalWeight = data.getDouble("SIEVE_NO_4") + data.getDouble("SIEVE_NO_8") + data.getDouble("SIEVE_NO_16") +
                data.getDouble("SIEVE_NO_30") + data.getDouble("SIEVE_NO_50") + data.getDouble("SIEVE_NO_100") +
                data.getDouble("SIEVE_NO_200") + data.getDouble("PAN");

        // First find all the percent retained
        double[] percentRetainedValues = new double[8];
        percentRetainedValues[0] = SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_4"), totalWeight);
        percentRetainedValues[1] = SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_8"), totalWeight);
        percentRetainedValues[2] = SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_16"), totalWeight);
        percentRetainedValues[3] = SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_30"), totalWeight);
        percentRetainedValues[4] = SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_50"), totalWeight);
        percentRetainedValues[5] = SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_100"), totalWeight);
        percentRetainedValues[6] = SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("SIEVE_NO_200"), totalWeight);
        percentRetainedValues[7] = SieveAnalysisCalc.CalcPerecentRetained(data.getDouble("PAN"), totalWeight);
        


        return result;
    }

}
