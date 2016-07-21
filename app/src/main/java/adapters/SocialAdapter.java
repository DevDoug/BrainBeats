package adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.brainbeats.R;
import data.MixContract;

/**
 * Created by douglas on 5/13/2016.
 */
public class SocialAdapter extends CursorAdapter {

    Context mAdapterContext;

    public SocialAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
        mAdapterContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView titleText = (TextView) view.findViewById(R.id.user_name);
        titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MixContract.UserEntry.COLUMN_NAME_USER_NAME)));
    }
}
