package com.brainbeats.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.brainbeats.entity.Track;

/*
 * Created by douglas on 5/31/2016.
 */

public class SearchMusicAdapter extends RecyclerView.Adapter<SearchMusicAdapter.ViewHolder> {

    Context mAdapterContext;
    public List<Track> mTracks;

    public SearchMusicAdapter(Context context, List<Track> data) {
        mAdapterContext = context;
        mTracks = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mAlbumArtCover;
        TextView mTrackTitle;
        TextView mNoCoverArt;

        public ViewHolder(View v) {
            super(v);
            mAlbumArtCover = (ImageView) v.findViewById(R.id.album_cover_art);
            mTrackTitle = (TextView) v.findViewById(R.id.album_title);
            //mNoCoverArt = (TextView) v.findViewById(R.id.no_cover_art);

            v.setOnClickListener(new View.OnClickListener() { //Mix selected load detail screen
                @Override
                public void onClick(View v) {
                    ((MainActivity) mAdapterContext).switchToBeatDetailFragment(mTracks.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mAdapterContext).inflate(R.layout.beat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTrackTitle.setText(mTracks.get(position).getTitle());
        if (mTracks.get(position).getArtworkURL() != null)
            Picasso.with(mAdapterContext).load(mTracks.get(position).getArtworkURL()).into(holder.mAlbumArtCover);
        else {
/*            holder.mAlbumArtCover.setImageResource(R.drawable.dark_logo_transparent_background);
            holder.mAlbumArtCover.setAlpha(45);
            holder.mNoCoverArt.setVisibility(View.VISIBLE);*/
        }
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }
}