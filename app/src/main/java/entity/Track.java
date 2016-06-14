package entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by douglas on 5/24/2016.
 */
public class Track implements Parcelable {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private int mID;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;

    @SerializedName("duration")
    private int mDuration;

    public Track(){
    }

    protected Track(Parcel in) {
        mTitle = in.readString();
        mID = in.readInt();
        mStreamURL = in.readString();
        mArtworkURL = in.readString();
        mDuration = in.readInt();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title){
        this.mTitle = title;
    }

    public int getID() {
        return mID;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public void setID(int id) {
        this.mID = id;
    }

    public void setStreamURL(String streamURL) {
        this.mStreamURL = streamURL;
    }

    public void setArtworkURL(String artworkURL) {
        this.mArtworkURL = artworkURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeInt(mID);
        dest.writeString(mStreamURL);
        dest.writeString(mArtworkURL);
        dest.writeInt(mDuration);
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}