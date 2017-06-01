package com.brainbeats.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by douglas on 7/28/2016.
 */
public class Playlist {

    public long mID;

    public String mPlaylistTitle;

    private int mSoundCloudId;

    public long getPlaylistId() {return mID;}

    public void setPlaylistId(long Id) {
        this.mID = Id;
    }

    public String getPlaylistTitle() {
        return mPlaylistTitle;
    }

    public void setPlaylistTitle(String playlisttitle) {
        this.mPlaylistTitle = playlisttitle;
    }

    public int getSoundCloudId() {
        return mSoundCloudId;
    }

    public void setSoundCloudId(int soundCloudId) {
        this.mSoundCloudId = soundCloudId;
    }
}
