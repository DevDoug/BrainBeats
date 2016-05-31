package model;

/**
 * Created by douglas on 5/16/2016.
 */
public class MixItem {

    private long mMixItemId;

    private String mMixItemTitle;

    private int mMixItemLevel;

    public MixItem(){
    }

    public long getMixItemId() {
        return mMixItemId;
    }

    public void setMixItemId(long mixItemId) {
        this.mMixItemId = mixItemId;
    }

    public String getMixItemTitle() {
        return mMixItemTitle;
    }

    public void setMixItemTitle(String mixItemTitle) {
        this.mMixItemTitle = mixItemTitle;
    }

    public int getMixItemLevel() {
        return mMixItemLevel;
    }

    public void setMixItemLevel(int mixItemLevel) {
        this.mMixItemLevel = mixItemLevel;
    }
}
