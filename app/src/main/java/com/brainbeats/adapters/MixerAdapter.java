package com.brainbeats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.MixerActivity;
import com.brainbeats.R;

import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.data.BrainBeatsDbHelper;
import com.brainbeats.model.Mix;
import com.brainbeats.utils.Constants;

/**
 * Created by douglas on 5/13/2016.
 */
public class MixerAdapter extends RecyclerViewCursorAdapter<MixerAdapter.ViewHolder>{

    Context mAdapterContext;

    public MixerAdapter(Context context, Cursor cursor) {
        super(context,cursor);
        mAdapterContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_TITLE));
        if(title != null)
            viewHolder.mTitleText.setText(title);

        viewHolder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((MixerActivity) mAdapterContext).loadMixerDetailFragment(Constants.buildMixFromCursor(mAdapterContext, getCursor(), viewHolder.getAdapterPosition()));
            }
        });

        viewHolder.mContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //load delete dialog
                Constants.buildActionDialog(mAdapterContext, "Delete Mix", "Do you want to delete this mix from your mixer", "confirm", new Constants.ConfirmDialogActionListener() {
                    @Override
                    public void PerformDialogAction() {
                        Mix selectedMix = Constants.buildMixFromCursor(mAdapterContext,getCursor(), viewHolder.getAdapterPosition()); // get the selected mix item
                        mAdapterContext.getContentResolver().delete(BrainBeatsContract.MixEntry.CONTENT_URI, "_Id" + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL, new String[]{String.valueOf(selectedMix.getMixId())});
                    }
                });
                return false;
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.mixer_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mContainer;
        TextView mTitleText;

        public ViewHolder(View view){
            super(view);
            mContainer =(CardView) view.findViewById(R.id.card_view);
            mTitleText = (TextView) view.findViewById(R.id.album_title);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}