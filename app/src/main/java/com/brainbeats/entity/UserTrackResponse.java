package com.brainbeats.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by douglas on 6/6/2016.
 */
public class UserTrackResponse {

    @SerializedName("kind")
    public String mKind;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private int mID;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;

    @SerializedName("duration")
    private int mDuration;

    @SerializedName("user_favorite")
    private boolean mUserFavorite;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title){
        this.mTitle = title;
    }

    public int getID() {
        return mID;
    }

    public void setID(int id){
        this.mID = id;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public boolean getIsFavorite(){
        return mUserFavorite;
    }

    public void setUserFavorite(boolean favorite){
        mUserFavorite = favorite;
    }
}
