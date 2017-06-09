package com.brainbeats.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by douglas.ray on 6/8/2017.
 */

public class TrackCollection {

    @SerializedName("collection")
    ArrayList<Track> mTracks;

    @SerializedName("next_href")
    String mNextHref;

    public ArrayList<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.mTracks = tracks;
    }

    public String getNextHref() {
        return mNextHref;
    }

    public void setNextHref(String nextHref) {
        this.mNextHref = nextHref;
    }
}
