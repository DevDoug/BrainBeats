package adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brainbeats.LibraryActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;

import data.BrainBeatsContract;
import model.Mix;
import utils.Constants;
import web.OfflineSyncManager;

/**
 * Created by douglas on 5/20/2016.
 */
public class LibraryMixAdapter extends CursorAdapter {

    Context mAdapterContext;

    public LibraryMixAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
        mAdapterContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.library_beat_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView titleText = (TextView) view.findViewById(R.id.album_title);
        titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_TITLE)));
    }
}
