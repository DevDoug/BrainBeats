package adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brainbeats.R;

import java.util.ArrayList;
import java.util.List;

import data.MixContract;
import entity.Track;
import model.Mix;
import utils.MixManager;

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
        titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MixContract.MixEntry.COLUMN_NAME_MIX_TITLE)));
    }
}