package com.brainbeats.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.LibraryActivity;
import com.brainbeats.R;
import com.brainbeats.entity.Track;
import com.brainbeats.model.Mix;
import com.brainbeats.model.Playlist;
import com.brainbeats.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

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
        holder.mTitleText.setText(playlist.getPlaylistTitle());

        holder.mContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Constants.buildActionDialog(mAdapterContext, "Delete Playlist", "Do you want to delete this playlist from your library", "Confirm", new Constants.ConfirmDialogActionListener() {
                    @Override
                    public void PerformDialogAction() {
                        FirebaseDatabase Database = FirebaseDatabase.getInstance();
                        Query reference = Database
                                .getReference("playlists/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .orderByChild("artistId")
                                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());


                        reference.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    Playlist playlistToDelete = childSnapshot.getValue(Playlist.class);
                                    if(playlistToDelete.getPlaylistTitle().equals(playlistToDelete.getPlaylistTitle()))
                                        childSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }
                });
                return false;
            }
        });

        holder.mPlay.setOnClickListener(v -> {
            Queue<Track> playListQue = new PriorityQueue<>();
            for (int i = 0; i < playlist.getMixes().size(); i++) {
                Track playListTrack = new Track(playlist.getMixes().get(i));
                playListQue.add(playListTrack);
            }
            ((LibraryActivity) mAdapterContext).mAudioService.setPlaylist(playListQue);
        });
    }

    @Override
    public int getItemCount() {
        return mPlaylist.size();
    }
}
