package com.brainbeats.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.R;
import com.brainbeats.SocialActivity;
import com.brainbeats.model.BrainBeatsUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

/**
 * Created by douglas.ray on 10/27/2017.
 */

public class FriendsSearchAdapter extends FirebaseRecyclerAdapter<BrainBeatsUser, FriendsSearchAdapter.ViewHolder> {

    Context context;

    public FriendsSearchAdapter(Context context, FirebaseRecyclerOptions<BrainBeatsUser> options) {
        super(options);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, BrainBeatsUser model) {
        holder.mUserNameText.setText(model.getArtistName());

        holder.mArtistContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //populate artist card
                ((SocialActivity) context).populateAddArtistCard(model);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mArtistContainer;
        TextView mUserNameText;

        public ViewHolder(View view) {
            super(view);
            mArtistContainer = view.findViewById(R.id.artist_name_container);
            mUserNameText = view.findViewById(R.id.artist_name);
        }
    }
}
