package com.brainbeats.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.brainbeats.model.Mix;

import java.util.ArrayList;

/**
 * Created by Doug on 12/25/2017.
 */

public class BrowseMusicAdapter extends RecyclerView.Adapter<BrowseMusicAdapter.ViewHolder> {

    Context mAdapterContext;
    ArrayList<Mix> mMixList;

    public BrowseMusicAdapter(Context context, ArrayList<Mix> items) {
        mAdapterContext = context;
        mMixList = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mContainer;
        TextView mTitleText;
        ImageView mPlay;
        ImageView mMix;

        public ViewHolder(View view) {
            super(view);
            mContainer = view.findViewById(R.id.card_view);
            mTitleText = view.findViewById(R.id.album_title);
            mPlay = view.findViewById(R.id.play_mix);
            mMix = view.findViewById(R.id.mix_beat);

            view.setOnClickListener(new View.OnClickListener() { //Mix selected load detail screen
                @Override
                public void onClick(View v) {
                    ((MainActivity) mAdapterContext).switchToBeatDetailFragment(mMixList.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public BrowseMusicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mAdapterContext).inflate(R.layout.beat_item, parent, false);
        return new BrowseMusicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BrowseMusicAdapter.ViewHolder holder, int position) {
        Mix mix = mMixList.get(position);
        holder.mTitleText.setText(mix.getMixTitle());


    }

    @Override
    public int getItemCount() {
        return mMixList.size();
    }
}
