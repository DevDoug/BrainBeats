package model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Douglas on 4/15/2016.
 */
public class Mix implements Parcelable {

    private int mId;
    private String mMixTitle;
    private int mAlphaLevel;
    private int mBetaLevel;
    private int mGammaLevel;
    private int mThetaLevel;
    private Bitmap mBeatAlbumCoverArt;

    public Mix(){
    }

    public int getBeatId() {
        return mId;
    }

    public void setBeatId(int beatId) {
        this.mId = beatId;
    }

    public String getBeatTitle() {
        return mMixTitle;
    }

    public void setMixTitle(String BeatTitle) {
        this.mMixTitle = BeatTitle;
    }

    public Bitmap getBeatAlbumCoverArt() {
        return mBeatAlbumCoverArt;
    }

    public void setBeatAlbumCoverArt(Bitmap BeatAlbumCoverArt) {
        this.mBeatAlbumCoverArt = BeatAlbumCoverArt;
    }

    public int getAlphaLevel() {
        return mAlphaLevel;
    }

    public void setAlphaLevel(int AlphaLevel) {
        this.mAlphaLevel = AlphaLevel;
    }

    public int getBetaLevel() {
        return mBetaLevel;
    }

    public void setBetaLevel(int BetaLevel) {
        this.mBetaLevel = BetaLevel;
    }

    public int getGammaLevel() {
        return mGammaLevel;
    }

    public void setGammaLevel(int GammaLevel) {
        this.mGammaLevel = GammaLevel;
    }

    public int getThetaLevel() {
        return mThetaLevel;
    }

    public void setThetaLevel(int ThetaLevel) {
        this.mThetaLevel = ThetaLevel;
    }

    protected Mix(Parcel in) {
        mMixTitle = in.readString();
        mBeatAlbumCoverArt = in.readParcelable(Bitmap.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMixTitle);
        dest.writeParcelable(mBeatAlbumCoverArt, flags);
    }
}