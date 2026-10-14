package io.github.rhemon.aggregatesieveanalysis;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class PrivacyPolicyActivity extends CompatibleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.privacy_policy_title);

        TextView text = new TextView(this);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        text.setPadding(padding, padding, padding, padding);
        text.setText(R.string.privacy_policy_text);
        text.setTextSize(16);
        Linkify.addLinks(text, Linkify.WEB_URLS);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(text, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(scroll);
    }
}
