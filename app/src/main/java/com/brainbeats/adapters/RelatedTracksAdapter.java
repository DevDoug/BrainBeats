package com.brainbeats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.brainbeats.R;
import com.brainbeats.data.BrainBeatsContract;

/**
 * Created by douglas on 4/29/2016.
 * TODO Implement in beta version 2
 */
public class RelatedTracksAdapter extends CursorAdapter {

    Context mAdapterContext;

    public RelatedTracksAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
        mAdapterContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.mixer_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView titleText = (TextView) view.findViewById(R.id.album_title);
        titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_TITLE)));
    }
}