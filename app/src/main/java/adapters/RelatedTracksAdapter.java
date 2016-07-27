package adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.brainbeats.MainActivity;
import com.brainbeats.R;
import java.util.List;

import data.MixContract;
import entity.Collection;
import entity.Track;
import fragments.DashboardDetailFragment;

/**
 * Created by douglas on 4/29/2016.
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
        titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MixContract.MixRelatedEntry.COLUMN_NAME_MIX_TITLE)));
    }
}
