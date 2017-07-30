package com.brainbeats.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.brainbeats.entity.Track;

/**
 * Created by Douglas on 4/15/2016.
 */
public class Mix implements Parcelable {

    public Mix() {}

    public Mix(String title) {
        this.mMixTitle = title;
    }

    public   Mix(Track track) {
        mMixId = "";
        mArtistId = "";
        mMixTitle = track.getTitle();
        mBeatAlbumCoverArtSource = track.getArtworkURL();
        mMixSoundCloudId = track.getID();
        mStreamURL = track.getStreamURL();
        mDuration = track.getDuration();
    }

    private String mMixId;

    public String mArtistId;

    private String mMixTitle;

    private String mBeatAlbumCoverArtSource;

    private int mMixFavorite;

    private int mMixRating;

    private int mMixSoundCloudId;

    private int mRelatedTracksId;

    private String mStreamURL;

    private String mMixTagList;

    private int mDuration;

    private int mIsDownvoted;

    public String getMixId() {
        return mMixId;
    }

    public void setMixId(String mixId) {
        this.mMixId = mixId;
    }

    public String getArtistId() {
        return mArtistId;
    }

    public void setArtistId(String artistId) {
        this.mArtistId = artistId;
    }

    public String getMixTitle() {
        return mMixTitle;
    }

    public void setMixTitle(String mixTitle) {
        this.mMixTitle = mixTitle;
    }

    public String getMixAlbumCoverArt() {
        return mBeatAlbumCoverArtSource;
    }

    public void setMixAlbumCoverArt(String beatAlbumCoverArtSource) {
        this.mBeatAlbumCoverArtSource = beatAlbumCoverArtSource;
    }

    public int getMixFavorite() {
        return mMixFavorite;
    }

    public void setMixFavorite(int mixItemFavorite) {
        this.mMixFavorite = mixItemFavorite;
    }

    public int getMixRating() {
        return mMixRating;
    }

    public void setMixRating(int rating) {
        this.mMixRating = rating;
    }

    public int getSoundCloudId() {
        return mMixSoundCloudId;
    }

    public void setSoundCloudId(int soundCloudId) {
        this.mMixSoundCloudId = soundCloudId;
    }

    public int getRelatedTracksId() {
        return mRelatedTracksId;
    }

    public void setRelatedTracksId(int relatedTracksId) {
        this.mRelatedTracksId = relatedTracksId;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public void setStreamURL(String streamURL) {
        this.mStreamURL = streamURL;
    }

    public BrainBeatsUser getUser() {
        return mBrainBeatsUser;
    }

    public void setUser(BrainBeatsUser brainBeatsUser) {
        this.mBrainBeatsUser = brainBeatsUser;
    }

    public String getMixTagList() {
        return mMixTagList;
    }

    public void setMixTagList(String mixertaglist) {
        this.mMixTagList = mixertaglist;
    }

    public int getIsDownvoted() {
        return mIsDownvoted;
    }

    public void setIsDownvoted(int isDownvoted) {
        this.mIsDownvoted = isDownvoted;
    }

    private BrainBeatsUser mBrainBeatsUser;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMixId);
        dest.writeString(mMixTitle);
        dest.writeString(mBeatAlbumCoverArtSource);
        dest.writeInt(mMixFavorite);
        dest.writeInt(mMixRating);
        dest.writeInt(mMixSoundCloudId);
        dest.writeInt(mRelatedTracksId);
        dest.writeString(mStreamURL);
        dest.writeString(mMixTagList);
        dest.writeInt(mDuration);
        dest.writeParcelable(mBrainBeatsUser,0);
    }

    protected Mix(Parcel in) {
        mMixId = in.readString();
        mMixTitle = in.readString();
        mBeatAlbumCoverArtSource = in.readString();
        mMixFavorite = in.readInt();
        mMixRating = in.readInt();
        mMixSoundCloudId = in.readInt();
        mRelatedTracksId = in.readInt();
        mStreamURL = in.readString();
        mMixTagList = in.readString();
        mDuration = in.readInt();
        mBrainBeatsUser = (BrainBeatsUser) in.readParcelable(BrainBeatsUser.class.getClassLoader());
    }

    public static final Creator<Mix> CREATOR = new Creator<Mix>() {
        @Override
        public Mix createFromParcel(Parcel in) {
            return new Mix(in);
        }

        @Override
        public Mix[] newArray(int size) {
            return new Mix[size];
        }
    };

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }
}