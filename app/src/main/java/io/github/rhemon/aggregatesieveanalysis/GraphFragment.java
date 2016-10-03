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

import java.util.jar.Attributes;

/**
 * Created by rhemon on 9/23/16.
 */
public class GraphFragment extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.graph_fragment, container, false);
        SemiLogGraph slg = new SemiLogGraph(this.getContext());
        slg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        try {
            slg.setData(new JSONObject(getArguments().getString("data")));
            ((FrameLayout) view.findViewById(R.id.graphHolder)).addView(slg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }




}
