package model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Douglas on 4/15/2016.
 */
public class Mix implements Parcelable {

    private long mId;
    private String mMixTitle;
    private String mBeatAlbumCoverArtSource;
    private ArrayList<MixItem> mMixItems;

    public Mix(){
    }

    public long getMixId() {
        return mId;
    }

    public void setMixId(long mixId) {
        this.mId = mixId;
    }

    public String getBeatTitle() {
        return mMixTitle;
    }

    public void setMixTitle(String beatTitle) {
        this.mMixTitle = beatTitle;
    }

    public String getMixAlbumCoverArt() {
        return mBeatAlbumCoverArtSource;
    }

    public void setMixAlbumCoverArt(String beatAlbumCoverArtSource) {
        this.mBeatAlbumCoverArtSource = beatAlbumCoverArtSource;
    }

    public ArrayList<MixItem> getMixItems() {
        return mMixItems;
    }

    public void setMixItems(ArrayList<MixItem> mixItems) {
        this.mMixItems = mixItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mMixTitle);
        dest.writeString(mBeatAlbumCoverArtSource);
    }

    protected Mix(Parcel in) {
        mId = in.readLong();
        mMixTitle = in.readString();
        mBeatAlbumCoverArtSource = in.readString();
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
}