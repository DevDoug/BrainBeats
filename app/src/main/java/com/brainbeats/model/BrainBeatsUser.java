package com.brainbeats.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by douglas on 5/13/2016.
 */
public class BrainBeatsUser implements Parcelable {

    private String mUserId;
    private String mUserName;
    private String artistProfileImage;
    private String artistName;
    private String artistDescription;
    private ArrayList<BrainBeatsUser> mFriends;

    private int mSoundCloudUserId;

    public BrainBeatsUser(){}

    public BrainBeatsUser(String userName) {
        this.mUserName = userName;
    }

    public BrainBeatsUser(String userId, String username){
        this.mUserId = userId;
        this.mUserName = username;
    }

    public BrainBeatsUser(com.brainbeats.entity.User user){
        this.mUserName = user.getUsername();
        this.artistProfileImage = user.getAvatarUrl();
        this.mSoundCloudUserId = user.getId();
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String UserName) {
        this.mUserName = UserName;
    }

    public String getArtistProfileImage() {
        return artistProfileImage;
    }

    public void setArtistProfileImage(String artistProfileImage) {
        this.artistProfileImage = artistProfileImage;
    }

    public long getSoundCloudUserId() {
        return mSoundCloudUserId;
    }

    public void setSoundCloudUserId(int soundCloudUserId) {
        this.mSoundCloudUserId = soundCloudUserId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistDescription() {
        return artistDescription;
    }

    public void setArtistDescription(String artistDescription) {
        this.artistDescription = artistDescription;
    }

    public ArrayList<BrainBeatsUser> getFriends() {
        return mFriends;
    }

    public void setFriends(ArrayList<BrainBeatsUser> mFriends) {
        this.mFriends = mFriends;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserId);
        dest.writeString(mUserName);
        dest.writeString(artistProfileImage);
        dest.writeInt(mSoundCloudUserId);
        dest.writeString(artistName);
        dest.writeString(artistDescription);
    }

    protected BrainBeatsUser(Parcel in) {
        mUserId = in.readString();
        mUserName = in.readString();
        artistProfileImage = in.readString();
        mSoundCloudUserId = in.readInt();
        artistName = in.readString();
        artistDescription = in.readString();
    }

    public static final Creator<BrainBeatsUser> CREATOR = new Creator<BrainBeatsUser>() {
        @Override
        public BrainBeatsUser createFromParcel(Parcel in) {
            return new BrainBeatsUser(in);
        }

        @Override
        public BrainBeatsUser[] newArray(int size) {
            return new BrainBeatsUser[size];
        }
    };
}
