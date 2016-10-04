package io.github.rhemon.aggregatesieveanalysis;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.jar.Attributes;

/**
 * Created by rhemon on 9/23/16.
 */
public class ResultFragment extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.result_fragment, container, false);
        try {
            JSONObject data = new JSONObject(getArguments().getString("data"));
            ((TextView) view.findViewById(R.id.fm)).setText("Fineness Modulus = " + String.format("%.2f", data.getDouble("FINENESS_MODULUS")));
            ((TextView) view.findViewById(R.id.d60)).setText("D60 = " + String.format("%.2f", data.getDouble("D60")));
            ((TextView) view.findViewById(R.id.d10)).setText("D10 = " + String.format("%.2f", data.getDouble("D10")));
            ((TextView) view.findViewById(R.id.uc)).setText("Uniform Coefficient = " + String.format("%.2f", data.getDouble("CU")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }




}
