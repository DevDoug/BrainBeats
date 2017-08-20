package com.brainbeats.model;

import java.util.ArrayList;

/**
 * Created by douglas on 7/28/2016.
 */
public class Playlist {

    public String playlistTitle;

    public ArrayList<Mix> mixes;

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public ArrayList<Mix> getMixes() {
        return mixes;
    }

    public void setMixes(ArrayList<Mix> mixes) {
        this.mixes = mixes;
    }
}
