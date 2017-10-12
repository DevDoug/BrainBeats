package com.brainbeats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brainbeats.R;
import com.brainbeats.data.BrainBeatsContract;

/*
 * Created by douglas on 8/18/2016.
 */

public class MixTagAdapter extends RecyclerViewCursorAdapter<MixTagAdapter.ViewHolder> {

    Context mAdapterContext;

    public MixTagAdapter(Context context, Cursor cursor) {
        super(context,cursor);
        mAdapterContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.MixTagEntry.COLUMN_NAME_TAG_TITLE));
        if(title != null)
            viewHolder.mSoundCloudTag.setText(title);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button mSoundCloudTag;

        public ViewHolder(View view){
            super(view);
            mSoundCloudTag = (Button) view.findViewById(R.id.sound_cloud_tag);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.tag_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
