package com.brainbeats.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.brainbeats.entity.Track;

/**
 * Created by Douglas on 4/15/2016.
 */
public class Mix implements Parcelable {

    public Mix() {
    }

    public Mix(String title) {
        this.mMixTitle = title;
    }

    public Mix(Track track) {
        mMixTitle = track.getTitle();
        mBeatAlbumCoverArtSource = track.getArtworkURL();
        mMixSoundCloudId = track.getID();
        mStreamURL = track.getStreamURL();
        mDuration = track.getDuration();
    }

    private long mId;

    private String mMixTitle;

    private String mBeatAlbumCoverArtSource;

    private int mMixFavorite;

    private int mMixRating;

    private int mMixSoundCloudId;

    private int mRelatedTracksId;

    private int mIsInLibrary;

    private int mIsinMixer;

    private long mMixUserId;

    private long mMixItemsId;

    private String mStreamURL;

    private String mMixTagList;

    private int mDuration;

    private int mIsDownvoted;

    public long getMixId() {
        return mId;
    }

    public void setMixId(long mixId) {
        this.mId = mixId;
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

    public int getIsInLibrary() {
        return mIsInLibrary;
    }

    public void setIsInLibrary(int isinlibrary) {
        this.mIsInLibrary = isinlibrary;
    }

    public int getIsInMixer() {
        return mIsinMixer;
    }

    public void setIsInMixer(int isinMixer) {
        this.mIsinMixer = isinMixer;
    }

    public long getMixUserId() {
        return mMixUserId;
    }

    public void setMixUserId(long mixUserId) {
        this.mMixUserId = mixUserId;
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

    public long getMixItemsId() {
        return mMixItemsId;
    }

    public void setMixItemsUserId(long mixItemsUserId) {
        this.mMixItemsId = mixItemsUserId;
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
        dest.writeLong(mId);
        dest.writeString(mMixTitle);
        dest.writeString(mBeatAlbumCoverArtSource);
        dest.writeInt(mMixFavorite);
        dest.writeInt(mMixRating);
        dest.writeInt(mMixSoundCloudId);
        dest.writeInt(mRelatedTracksId);
        dest.writeInt(mIsInLibrary);
        dest.writeInt(mIsinMixer);
        dest.writeLong(mMixUserId);
        dest.writeLong(mMixItemsId);
        dest.writeString(mStreamURL);
        dest.writeString(mMixTagList);
        dest.writeInt(mDuration);
        dest.writeParcelable(mBrainBeatsUser,0);
    }

    protected Mix(Parcel in) {
        mId = in.readLong();
        mMixTitle = in.readString();
        mBeatAlbumCoverArtSource = in.readString();
        mMixFavorite = in.readInt();
        mMixRating = in.readInt();
        mMixSoundCloudId = in.readInt();
        mRelatedTracksId = in.readInt();
        mIsInLibrary = in.readInt();
        mIsinMixer = in.readInt();
        mMixUserId = in.readLong();
        mMixItemsId = in.readLong();
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