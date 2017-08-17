package com.brainbeats.model;

import java.util.ArrayList;

/**
 * Created by douglas on 7/28/2016.
 */
public class Playlist {

    public long playlistId;

    public String mPlaylistTitle;

    public ArrayList<Mix> mMixes;

    public long getPlaylistId() {return playlistId;}

    public void setPlaylistId(long Id) {
        this.playlistId = Id;
    }

    public String getPlaylistTitle() {
        return mPlaylistTitle;
    }

    public void setPlaylistTitle(String playlisttitle) {
        this.mPlaylistTitle = playlisttitle;
    }

    public ArrayList<Mix> getMixes() {
        return mMixes;
    }

    public void setMixes(ArrayList<Mix> mixes) {
        this.mMixes = mixes;
    }

}
