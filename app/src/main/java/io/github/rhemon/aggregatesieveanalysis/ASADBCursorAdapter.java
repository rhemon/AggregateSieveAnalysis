package io.github.rhemon.aggregatesieveanalysis;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import io.github.rhemon.aggregatesieveanalysis.data.ASADBContract;

/**
 * Created by rhemon on 11/25/16.
 */

public class ASADBCursorAdapter extends CursorAdapter {


    public ASADBCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_title_and_desc, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView descView = (TextView) view.findViewById(R.id.desc);

        int titleIndex = cursor.getColumnIndex(ASADBContract.ASADBEntry.COLUMN_ASADATA_TITLE);
        String title = cursor.getString(titleIndex);
        int descIndex = cursor.getColumnIndex(ASADBContract.ASADBEntry.COLUMN_ASADATA_DESC);
        String desc = cursor.getString(descIndex);

        titleView.setText(title);
        descView.setText(desc);
    }
}
