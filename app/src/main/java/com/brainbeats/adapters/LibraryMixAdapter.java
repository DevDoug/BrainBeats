package com.brainbeats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.LibraryActivity;
import com.brainbeats.R;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.data.BrainBeatsDbHelper;
import com.brainbeats.entity.Track;
import com.brainbeats.model.Mix;
import com.brainbeats.utils.Constants;

import java.util.ArrayList;

/**
 * Created by douglas on 5/20/2016.
 */
public class LibraryMixAdapter extends RecyclerView.Adapter<LibraryMixAdapter.ViewHolder> {

    private Context mAdapterContext;
    private ArrayList<Mix> mMixList;

    public LibraryMixAdapter(Context context, ArrayList<Mix> items) {
        mAdapterContext = context;
        mMixList = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mContainer;
        TextView mTitleText;
        ImageView mPlay;
        ImageView mMix;

        public ViewHolder(View view){
            super(view);
            mContainer = (CardView) view.findViewById(R.id.card_view);
            mTitleText = (TextView) view.findViewById(R.id.album_title);
            mPlay = (ImageView) view.findViewById(R.id.play_mix);
            mMix = (ImageView) view.findViewById(R.id.mix_beat);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.library_beat_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mix mix = mMixList.get(position);
        holder.mTitleText.setText(mix.getMixTitle());
        holder.mPlay.setOnClickListener(v -> {
            Track playTrack = new Track(mix);
            ((LibraryActivity) mAdapterContext).mCurrentSong = playTrack;
            ((LibraryActivity) mAdapterContext).mAudioService.setPlayingSong(playTrack);
            ((LibraryActivity) mAdapterContext).mAudioService.playSong(Uri.parse(mix.getStreamURL()));
        });
    }

    @Override
    public int getItemCount() {
        return mMixList.size();
    }
}