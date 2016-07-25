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
    private int mMixItemFavorite;
    private int mMixRating;
    private ArrayList<MixItem> mMixItems;
    private int mSoundCloudId;

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

    public int getMixItemFavorite() {
        return mMixItemFavorite;
    }

    public void setMixItemFavorite(int mixItemFavorite) {
        this.mMixItemFavorite = mixItemFavorite;
    }

    public int getMixRating(){
        return mMixRating;
    }

    public void setMixRating(int rating){
        this.mMixRating = rating;
    }

    public ArrayList<MixItem> getMixItems() {
        return mMixItems;
    }

    public void setMixItems(ArrayList<MixItem> mixItems) {
        this.mMixItems = mixItems;
    }

    public int getSoundCloudId() {
        return mSoundCloudId;
    }

    public void setSoundCloudId(int soundCloudId) {
        this.mSoundCloudId = soundCloudId;
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
        dest.writeInt(mSoundCloudId);
    }

    protected Mix(Parcel in) {
        mId = in.readLong();
        mMixTitle = in.readString();
        mBeatAlbumCoverArtSource = in.readString();
        mSoundCloudId = in.readInt();
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