package model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Douglas on 4/15/2016.
 */
public class Beat implements Parcelable {

    private String mBeatTitle;
    private Bitmap mBeatAlbumCoverArt;

    public Beat(){
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

    protected Beat(Parcel in) {
        mBeatTitle = in.readString();
        mBeatAlbumCoverArt = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Beat> CREATOR = new Creator<Beat>() {
        @Override
        public Beat createFromParcel(Parcel in) {
            return new Beat(in);
        }

        @Override
        public Beat[] newArray(int size) {
            return new Beat[size];
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