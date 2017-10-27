package com.brainbeats.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.R;
import com.brainbeats.model.Mix;

import java.util.ArrayList;

/*
 * Created by douglas on 5/13/2016.
 */

public class MixerAdapter extends RecyclerView.Adapter<MixerAdapter.ViewHolder> {

    Context mAdapterContext;
    ArrayList<Mix> mMixList;

    public MixerAdapter(Context context, ArrayList<Mix> items) {
        mAdapterContext = context;
        mMixList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.mixer_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mix mix = mMixList.get(position);
        holder.mTitleText.setText(mix.getMixTitle());
    }

    @Override
    public int getItemCount() {
        return mMixList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mContainer;
        TextView mTitleText;

        public ViewHolder(View view){
            super(view);
            mContainer = view.findViewById(R.id.card_view);
            mTitleText = view.findViewById(R.id.album_title);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}