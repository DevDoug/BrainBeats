package model;

/**
 * Created by douglas on 5/16/2016.
 */
public class MixerItem {

    String mMixItemTitle;

    int mMixItemLevel;

    public MixerItem(){
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
