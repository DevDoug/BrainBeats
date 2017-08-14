package com.brainbeats.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.R;
import com.brainbeats.model.Playlist;

import java.util.ArrayList;

/**
 * Created by douglas on 7/28/2016.
 */
public class LibraryPlaylistAdapter extends RecyclerView.Adapter<LibraryPlaylistAdapter.ViewHolder> {

    Context mAdapterContext;
    ArrayList<Playlist> mPlaylist;

    public LibraryPlaylistAdapter(Context context, ArrayList<Playlist> items) {
        mAdapterContext = context;
        mPlaylist = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mContainer;
        TextView mTitleText;
        ImageView mPlay;
        ImageView mMix;

        public ViewHolder(View view) {
            super(view);
            mContainer = (CardView) view.findViewById(R.id.card_view);
            mTitleText = (TextView) view.findViewById(R.id.album_title);
            mPlay = (ImageView) view.findViewById(R.id.play_mix);
            mMix = (ImageView) view.findViewById(R.id.mix_beat);
        }
    }

    @Override
    public LibraryPlaylistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.library_beat_item, parent, false);
        return new LibraryPlaylistAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LibraryPlaylistAdapter.ViewHolder holder, int position) {
        Playlist playlist = mPlaylist.get(position);
        //holder.mTitleText.setText(mix.getMixTitle());
    }

    @Override
    public int getItemCount() {
        return mPlaylist.size();
    }
}
