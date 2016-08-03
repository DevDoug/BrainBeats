package model;

/**
 * Created by douglas on 5/13/2016.
 */
public class User {

    private String mUserName;
    private int mSoundCloudUserId;

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String UserName) {
        this.mUserName = UserName;
    }

    public int getSoundCloudUserId() {
        return mSoundCloudUserId;
    }

    public void setSoundCloudUserId(int soundCloudUserId) {
        this.mSoundCloudUserId = soundCloudUserId;
    }
}
