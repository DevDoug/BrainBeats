package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by douglas on 5/13/2016.
 */
public class User implements Parcelable {

    private long mUserId;
    private String mUserName;
    private String mDescription;
    private String mPassword;
    private String mUserProfileImage;
    private int mSoundCloudUserId;

    public User(){}

    public User(entity.User user){
        this.mUserId = user.getId();
        this.mUserName = user.getUsername();
        this.mDescription = user.getDescription();
        this.mUserProfileImage = user.getAvatarUrl();
        this.mSoundCloudUserId = user.getId();
    }

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
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
        dest.writeLong(mUserId);
        dest.writeString(mUserName);
        dest.writeInt(mSoundCloudUserId);
    }

    protected User(Parcel in) {
        mUserId = in.readLong();
        mUserName = in.readString();
        mSoundCloudUserId = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
