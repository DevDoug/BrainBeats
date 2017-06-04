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
import com.brainbeats.model.Playlist;
import com.brainbeats.utils.Constants;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by douglas on 7/28/2016.
 */
public class LibraryPlaylistAdapter extends RecyclerViewCursorAdapter<LibraryPlaylistAdapter.ViewHolder> {

    Context mAdapterContext;

    public LibraryPlaylistAdapter(Context context, Cursor cursor) {
        super(context,cursor);
        mAdapterContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.PlaylistEntry.COLUMN_NAME_PLAYLIST_TITLE));
        if(title != null)
            viewHolder.mTitleText.setText(title);

        viewHolder.mContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //load delete dialog
                Constants.buildActionDialog(mAdapterContext, "Delete Playlist", "Do you want to delete this playlist from your library", "confirm", new Constants.ConfirmDialogActionListener() {
                    @Override
                    public void PerformDialogAction() {
                        Playlist selectedPlaylist = Constants.buildPlaylistFromCursor(mAdapterContext, getCursor(), viewHolder.getAdapterPosition()); // get the selected mix item
                        mAdapterContext.getContentResolver().delete(BrainBeatsContract.PlaylistEntry.CONTENT_URI, "_Id" + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL, new String[]{String.valueOf(selectedPlaylist.getPlaylistId())});
                    }
                });
                return false;
            }
        });

        viewHolder.mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO fix user is null when navigating to this mix after selecting from lib
                Playlist selectedPlaylist = Constants.buildPlaylistFromCursor(mAdapterContext, getCursor(), viewHolder.getAdapterPosition()); // get the selected mix item
                getAllTracksInPlaylist(selectedPlaylist.getPlaylistId());
            }
        });
    }

    public void getAllTracksInPlaylist(long playListId) {

        String selectQuery = "SELECT  * FROM " + BrainBeatsContract.MixEntry.TABLE_NAME + " M, "
                + BrainBeatsContract.PlaylistEntry.TABLE_NAME + " P, " + BrainBeatsContract.MixPlaylistEntry.TABLE_NAME  + " MP WHERE M."
                + BrainBeatsContract.MixEntry._ID + " = " + "MP." + BrainBeatsContract.MixPlaylistEntry.COLUMN_NAME_MIX_ID
                + " AND P." + BrainBeatsContract.PlaylistEntry._ID + " = " + "MP." + BrainBeatsContract.MixPlaylistEntry.COLUMN_NAME_PLAYLIST_ID
                + " AND P." + BrainBeatsContract.PlaylistEntry._ID + " = " + playListId;


        Cursor mixPlaylistCursor = mAdapterContext.getContentResolver().query(BrainBeatsContract.CONTENT_URI_RAW_QUERY, null, selectQuery, null, null);

/*        Cursor mixPlaylistCursor = mAdapterContext.getContentResolver().query(
                BrainBeatsContract.MixEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );*/

        Queue<Track> playListQue = new PriorityQueue<>();
        for (int i = 0; i < mixPlaylistCursor.getCount(); i++) {
            Mix tempMix = Constants.buildMixFromCursor(mAdapterContext, mixPlaylistCursor, i);
            Track playListTrack = new Track(tempMix);

            try {
                playListQue.add(playListTrack);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        mixPlaylistCursor.close();

        ((LibraryActivity) mAdapterContext).mAudioService.mIsPlayingPlaylist = true;
        ((LibraryActivity) mAdapterContext).mAudioService.setPlayingSong(playListQue.peek());
        ((LibraryActivity) mAdapterContext).mAudioService.setPlaylist(playListQue);
        ((LibraryActivity) mAdapterContext).mAudioService.playSong(Uri.parse(playListQue.peek().getStreamURL()));
        ((LibraryActivity) mAdapterContext).mAudioService.mPlaylistSongs.remove();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.library_beat_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
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

            mPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO open this mix in player
                }
            });

            mMix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO send to mixer

                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
