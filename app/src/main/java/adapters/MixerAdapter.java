package adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainbeats.MixerActivity;
import com.brainbeats.R;
import java.util.List;

import data.MixContract;
import model.Mix;

/**
 * Created by douglas on 5/13/2016.
 */
public class MixerAdapter extends CursorAdapter {

    Context mAdapterContext;

    public MixerAdapter(Context context, Cursor cursor, int flags) {
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
        RelativeLayout container = (RelativeLayout) view.findViewById(R.id.mixer_item_container);
        titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MixContract.MixEntry.COLUMN_NAME_MIX_TITLE)));
    }
}