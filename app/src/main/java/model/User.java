package model;

/**
 * Created by douglas on 5/13/2016.
 */
public class User {

    private int mUserId;
    private String mUserName;

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public User(){

    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String UserName) {
        this.mUserName = UserName;
    }
}
