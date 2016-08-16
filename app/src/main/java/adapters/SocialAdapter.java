package adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainbeats.MixerActivity;
import com.brainbeats.R;
import com.squareup.picasso.Picasso;

import data.BrainBeatsContract;
import utils.Constants;

/**
 * Created by douglas on 5/13/2016.
 */
public class SocialAdapter extends RecyclerViewCursorAdapter<SocialAdapter.ViewHolder> {

//    Context mAdapterContext;
//
//    public SocialAdapter(Context context, Cursor cursor, int flags) {
//        super(context, cursor, 0);
//        mAdapterContext = context;
//    }
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        View v = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
///*        ImageView stopFollow = (ImageView) v.findViewById(R.id.stop_following_text);
//            stopFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int followerId = cursor.getInt(cursor.getColumnIndexOrThrow(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID));
//                int deleted = context.getContentResolver().delete(BrainBeatsContract.UserFollowersEntry.CONTENT_URI, BrainBeatsContract.UserFollowersEntry.COLUMN_NAME_USER_FOLLOWER_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,new String[]{String.valueOf(followerId)});
//                Log.i("Deleted Count", String.valueOf(deleted));
//            }
//        });*/
//        return v;
//    }

    Context mAdapterContext;

    public SocialAdapter(Context context, Cursor cursor) {
        super(context,cursor);
        mAdapterContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME));
        if(title != null)
            viewHolder.mUsername.setText(title);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.user_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mUsername;

        public ViewHolder(View view){
            super(view);
            mUsername = (TextView) view.findViewById(R.id.user_name);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
