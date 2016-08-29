package adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.LibraryActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;

import data.BrainBeatsContract;
import model.Mix;
import utils.Constants;
import sync.OfflineSyncManager;

/**
 * Created by douglas on 5/20/2016.
 */
public class LibraryMixAdapter extends RecyclerViewCursorAdapter<LibraryMixAdapter.ViewHolder> {

    Context mAdapterContext;

    public LibraryMixAdapter(Context context, Cursor cursor) {
        super(context,cursor);
        mAdapterContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_TITLE));
        if(title != null)
            viewHolder.mTitleText.setText(title);

        viewHolder.mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO open this mix in player
                Mix selectedMix = Constants.buildMixFromCursor(mAdapterContext,getCursor(), viewHolder.getAdapterPosition()); // get the selected mix item
                //start intent to send user to play this mix in player
                Intent dashboardIntent = new Intent(mAdapterContext, MainActivity.class);
                dashboardIntent.putExtra(Constants.KEY_EXTRA_SELECTED_MIX, selectedMix);
                dashboardIntent.putExtra(Constants.KEY_EXTRA_SELECTED_USER, selectedMix.getUser());
                mAdapterContext.startActivity(dashboardIntent);
            }
        });

        viewHolder.mMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO send to mixer
                Mix selectedMix = Constants.buildMixFromCursor(mAdapterContext,getCursor(), viewHolder.getAdapterPosition()); // get the selected mix item

                //update local db with change
                Bundle settingsBundle = new Bundle();
                settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE, Constants.SyncDataType.Mixes.getCode());
                settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_ACTION, Constants.SyncDataAction.UpdateAddToMixer.getCode());
                settingsBundle.putParcelable(Constants.KEY_EXTRA_SELECTED_MIX, selectedMix);
                OfflineSyncManager.getInstance(mAdapterContext).performSyncOnLocalDb(((LibraryActivity) mAdapterContext).mCoordinatorLayout, settingsBundle, mAdapterContext.getContentResolver());

                //start intent to send user to their new mix for them to add/sub mix items.
                Intent mixerIntent = new Intent(mAdapterContext, MixerActivity.class);
                mAdapterContext.startActivity(mixerIntent);

            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.library_beat_item, parent, false);
        return new ViewHolder(itemView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleText;
        ImageView mPlay;
        ImageView mMix;

        public ViewHolder(View view){
            super(view);
            mTitleText = (TextView) view.findViewById(R.id.album_title);
            mPlay = (ImageView) view.findViewById(R.id.play_mix);
            mMix = (ImageView) view.findViewById(R.id.mix_beat);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}