package adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.brainbeats.R;

import data.BrainBeatsContract;

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
        View v = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
/*        ImageView stopFollow = (ImageView) v.findViewById(R.id.stop_following_text);
        stopFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int followerId = cursor.getInt(cursor.getColumnIndexOrThrow(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID));
                int deleted = context.getContentResolver().delete(BrainBeatsContract.UserFollowersEntry.CONTENT_URI, BrainBeatsContract.UserFollowersEntry.COLUMN_NAME_USER_FOLLOWER_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,new String[]{String.valueOf(followerId)});
                Log.i("Deleted Count", String.valueOf(deleted));
            }
        });*/
        return v;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView titleText = (TextView) view.findViewById(R.id.user_name);
        titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME)));
    }
}
