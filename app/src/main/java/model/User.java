package model;

/**
 * Created by douglas on 5/13/2016.
 */
public class User {

    private long mUserId;
    private String mUserName;
    private String mPassword;
    private int mSoundCloudUserId;

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

    public long getSoundCloudUserId() {
        return mSoundCloudUserId;
    }

    public void setSoundCloudUserId(int soundCloudUserId) {
        this.mSoundCloudUserId = soundCloudUserId;
    }
}
