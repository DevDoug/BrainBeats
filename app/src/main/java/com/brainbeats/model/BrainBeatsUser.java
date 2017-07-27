package com.brainbeats.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by douglas on 5/13/2016.
 */
public class BrainBeatsUser implements Parcelable {

    private String mUserId;
    private String mUserName;
    private String mDescription;
    private String mPassword;
    private String mUserProfileImage;
    private int mSoundCloudUserId;

    public BrainBeatsUser(){}

    public BrainBeatsUser(String userId, String username){
        this.mUserId = userId;
        this.mUserName = username;
    }

    public BrainBeatsUser(com.brainbeats.entity.User user){
        this.mUserName = user.getUsername();
        this.mDescription = user.getDescription();
        this.mUserProfileImage = user.getAvatarUrl();
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getUserProfileImage() {
        return mUserProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.mUserProfileImage = userProfileImage;
    }

    public long getSoundCloudUserId() {
        return mSoundCloudUserId;
    }

    public void setSoundCloudUserId(int soundCloudUserId) {
        this.mSoundCloudUserId = soundCloudUserId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserId);
        dest.writeString(mUserName);
        dest.writeString(mDescription);
        dest.writeString(mUserProfileImage);
        dest.writeString(mPassword);
        dest.writeInt(mSoundCloudUserId);
    }

    protected BrainBeatsUser(Parcel in) {
        mUserId = in.readString();
        mUserName = in.readString();
        mDescription = in.readString();
        mUserProfileImage = in.readString();
        mPassword = in.readString();
        mSoundCloudUserId = in.readInt();
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
