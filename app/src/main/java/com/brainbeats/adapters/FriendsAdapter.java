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
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.Mix;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by douglas on 5/13/2016.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    Context mAdapterContext;
    ArrayList<BrainBeatsUser> mUserList;

    public FriendsAdapter(Context context, ArrayList<BrainBeatsUser> userList) {
        this.mAdapterContext = context;
        this.mUserList = userList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mArtistContainerCard;
        TextView mUsername;
        TextView mDescription;
        ImageView mArtistThumbnail;

        public ViewHolder(View view){
            super(view);
            mUsername = (TextView) view.findViewById(R.id.artist_name);
            mDescription = (TextView) view.findViewById(R.id.artist_description);
            mArtistThumbnail = (ImageView) view.findViewById(R.id.artist_thumbnail);
            mArtistContainerCard = (CardView) view.findViewById(R.id.artist_name_container);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BrainBeatsUser user = mUserList.get(position);
        holder.mUsername.setText(user.getUserName());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
