package model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Douglas on 4/15/2016.
 */
public class Mix implements Parcelable {

    private String mBeatTitle;
    private Bitmap mBeatAlbumCoverArt;

    public Mix(){
    }

    public String getBeatTitle() {
        return mBeatTitle;
    }

    public void setBeatTitle(String BeatTitle) {
        this.mBeatTitle = BeatTitle;
    }

    public Bitmap getBeatAlbumCoverArt() {
        return mBeatAlbumCoverArt;
    }

    public void setBeatAlbumCoverArt(Bitmap BeatAlbumCoverArt) {
        this.mBeatAlbumCoverArt = BeatAlbumCoverArt;
    }

    protected Mix(Parcel in) {
        mBeatTitle = in.readString();
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
        dest.writeString(mBeatTitle);
        dest.writeParcelable(mBeatAlbumCoverArt, flags);
    }
}