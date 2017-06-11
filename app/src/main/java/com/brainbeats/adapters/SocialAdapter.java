package com.brainbeats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.R;
import com.brainbeats.data.BrainBeatsContract;
import com.squareup.picasso.Picasso;

/**
 * Created by douglas on 5/13/2016.
 */
public class SocialAdapter extends RecyclerViewCursorAdapter<SocialAdapter.ViewHolder> {

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
        Picasso.with(mAdapterContext).load(cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_PROFILE_IMG))).into(viewHolder.mArtistThumbnail);
        viewHolder.mDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_DESCRIPTION)));

        viewHolder.mArtistContainerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO - implement in version 2.0 beta version
                //((SocialActivity) mAdapterContext).switchToUserProfileFragment();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mArtistContainerCard;
        TextView mUsername;
        TextView mDescription;
        ImageView mArtistThumbnail;

        public ViewHolder(View view){
            super(view);
            mUsername = (TextView) view.findViewById(R.id.user_name);
            mDescription = (TextView) view.findViewById(R.id.artist_description);
            mArtistThumbnail = (ImageView) view.findViewById(R.id.artist_thumbnail);
            mArtistContainerCard = (CardView) view.findViewById(R.id.card_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
