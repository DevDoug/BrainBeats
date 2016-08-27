package entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import model.Mix;

/**
 * Created by douglas on 5/24/2016.
 */
public class Track implements Parcelable {

    @SerializedName("id")
    public int mID;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;

    @SerializedName("duration")
    private int mDuration;

    @SerializedName("tag_list")
    private String mTagList;

    @SerializedName("downloadable")
    private boolean mDownloadable;

    @SerializedName("download_url")
    private String mDownloadUrl;

    @SerializedName("user_favorite")
    private boolean mUserFavorite;

    @SerializedName("user")
    private User mUser;

    public Track() {
    }

    public Track(Mix mix) {
        this.mID = mix.getSoundCloudId();
        this.mTitle = mix.getMixTitle();
        this.mArtworkURL = mix.getMixAlbumCoverArt();
        this.mStreamURL = mix.getStreamURL();
        this.mUserFavorite = mix.getMixFavorite() == 1;
        this.mUser = new User(mix.getUser());
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
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

    public boolean getDownloadable() {
        return mDownloadable;
    }

    public void setDownloadable(boolean isDownloadable) {
        this.mDownloadable = isDownloadable;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String mDownloadUrl) {
        this.mDownloadUrl = mDownloadUrl;
    }

    public boolean getIsFavorite() {
        return mUserFavorite;
    }

    public void setUserFavorite(boolean favorite) {
        mUserFavorite = favorite;
    }

    public String getTagList() {
        return mTagList;
    }

    public void setTagList(String tagList) {
        this.mTagList = tagList;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Track(Parcel in) {
        mTitle = in.readString();
        mID = in.readInt();
        mStreamURL = in.readString();
        mArtworkURL = in.readString();
        mDuration = in.readInt();
        mDownloadable = in.readByte() != 0;
        mDownloadUrl = in.readString();
        // mUserFavorite = in.read();
        mTagList = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeInt(mID);
        dest.writeString(mStreamURL);
        dest.writeString(mArtworkURL);
        dest.writeInt(mDuration);
        dest.writeByte((byte) (mDownloadable ? 1 : 0));
        dest.writeString(mDownloadUrl);
        dest.writeString(mTagList);
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