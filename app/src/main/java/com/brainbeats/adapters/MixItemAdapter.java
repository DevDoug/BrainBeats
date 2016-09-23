package com.brainbeats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainbeats.R;

import com.brainbeats.data.BrainBeatsContract;

/**
 * Created by douglas on 5/16/2016.
 */
public class MixItemAdapter extends RecyclerViewCursorAdapter<MixItemAdapter.ViewHolder> {

    Context mAdapterContext;

    public MixItemAdapter(Context context, Cursor cursor) {
        super(context,cursor);
        mAdapterContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        viewHolder.mMixerItemTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_TITLE)));
        viewHolder.mProgressBar.setProgress(cursor.getInt(cursor.getColumnIndexOrThrow(BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_LEVEL)));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mAdapterContext).inflate(R.layout.beat_mixer_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mMixerItemTitle;
        ImageView mAddItemIcon;
        ImageView mIncreaseBeatLevelIcon;
        ImageView mSubtractBeatLevelIcon;
        ImageView mRemoveBeatItemIcon;
        ProgressBar mProgressBar;

        public ViewHolder(View view){
            super(view);
            mMixerItemTitle = (TextView) view.findViewById(R.id.mix_item_title);
            mAddItemIcon = (ImageView) view.findViewById(R.id.plus_icon);
            mIncreaseBeatLevelIcon = (ImageView) view.findViewById(R.id.increase_icon);
            mSubtractBeatLevelIcon = (ImageView) view.findViewById(R.id.minus_icon);
            mRemoveBeatItemIcon = (ImageView) view.findViewById(R.id.clear_icon);
            mProgressBar = (ProgressBar) view.findViewById(R.id.beat_level_bar);

            mAddItemIcon.setOnClickListener(this);
            mIncreaseBeatLevelIcon.setOnClickListener(this);
            mSubtractBeatLevelIcon.setOnClickListener(this);
            mRemoveBeatItemIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        /*@Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.plus_icon:
                    fragment.showAddBeatItemDialog();
                    break;
                case R.id.increase_icon:
                    mMixItems.get(getAdapterPosition()).setMixItemLevel(mMixItems.get(getAdapterPosition()).getMixItemLevel() + Constants.BEAT_LEVEL_INCREASE_DIFFERENCE);
                    mProgressBar.incrementProgressBy(Constants.BEAT_LEVEL_INCREASE_DIFFERENCE);
                    break;
                case R.id.minus_icon:
                    mMixItems.get(getAdapterPosition()).setMixItemLevel(mProgressBar.getProgress() - Constants.BEAT_LEVEL_INCREASE_DIFFERENCE);
                    mProgressBar.setProgress(mProgressBar.getProgress() - Constants.BEAT_LEVEL_INCREASE_DIFFERENCE);
                    break;
                case R.id.clear_icon:
                    int position = getAdapterPosition();
                    long removeMixID = mMixItems.get(position).getMixItemId();
                    removeItem(position);
                    mAdapterContext.getContentResolver().delete(
                            BrainBeatsContract.MixItemsEntry.CONTENT_URI,
                            BrainBeatsContract.MixItemsEntry._ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                            new String[]{String.valueOf(removeMixID)});
                    break;
            }
        }*/
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}