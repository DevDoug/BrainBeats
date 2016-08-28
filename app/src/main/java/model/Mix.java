package model;

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
    private int mMixFavorite;
    private int mMixRating;
    private ArrayList<MixItem> mMixItems;
    private int mMixSoundCloudId;
    private int mRelatedTracksId;
    private int mIsInLibrary;
    private int mIsinMixer;
    private long mMixUserId;
    private String mStreamURL;
    private String mMixTagList;
    private BrainBeatsUser mBrainBeatsUser;

    public Mix() {
    }

    public long getMixId() {
        return mId;
    }

    public void setMixId(long mixId) {
        this.mId = mixId;
    }

    public String getMixTitle() {
        return mMixTitle;
    }

    public void setMixTitle(String mixTitle) {
        this.mMixTitle = mixTitle;
    }

    public String getMixAlbumCoverArt() {
        return mBeatAlbumCoverArtSource;
    }

    public void setMixAlbumCoverArt(String beatAlbumCoverArtSource) {
        this.mBeatAlbumCoverArtSource = beatAlbumCoverArtSource;
    }

    public int getMixFavorite() {
        return mMixFavorite;
    }

    public void setMixFavorite(int mixItemFavorite) {
        this.mMixFavorite = mixItemFavorite;
    }

    public int getMixRating() {
        return mMixRating;
    }

    public void setMixRating(int rating) {
        this.mMixRating = rating;
    }

    public ArrayList<MixItem> getMixItems() {
        return mMixItems;
    }

    public void setMixItems(ArrayList<MixItem> mixItems) {
        this.mMixItems = mixItems;
    }

    public int getSoundCloudId() {
        return mMixSoundCloudId;
    }

    public void setSoundCloudId(int soundCloudId) {
        this.mMixSoundCloudId = soundCloudId;
    }

    public int getRelatedTracksId() {
        return mRelatedTracksId;
    }

    public void setRelatedTracksId(int relatedTracksId) {
        this.mRelatedTracksId = relatedTracksId;
    }

    public int getIsInLibrary() {
        return mIsInLibrary;
    }

    public void setIsInLibrary(int isinlibrary) {
        this.mIsInLibrary = isinlibrary;
    }

    public int getIsInMixer() {
        return mIsinMixer;
    }

    public void setIsInMixer(int isinMixer) {
        this.mIsinMixer = isinMixer;
    }

    public long getMixUserId() {
        return mMixUserId;
    }

    public void setMixUserId(long mixUserId) {
        this.mMixUserId = mixUserId;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public void setStreamURL(String streamURL) {
        this.mStreamURL = streamURL;
    }

    public BrainBeatsUser getUser() {
        return mBrainBeatsUser;
    }

    public void setUser(BrainBeatsUser brainBeatsUser) {
        this.mBrainBeatsUser = brainBeatsUser;
    }

    public String getMixTagList() {
        return mMixTagList;
    }

    public void setMixTagList(String mixertaglist) {
        this.mMixTagList = mixertaglist;
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
        dest.writeInt(mMixSoundCloudId);
        dest.writeInt(mRelatedTracksId);
        dest.writeInt(mIsInLibrary);
        dest.writeInt(mIsinMixer);
        dest.writeLong(mMixUserId);
        dest.writeString(mStreamURL);
        dest.writeParcelable(mBrainBeatsUser,0);
    }

    protected Mix(Parcel in) {
        mId = in.readLong();
        mMixTitle = in.readString();
        mBeatAlbumCoverArtSource = in.readString();
        mMixSoundCloudId = in.readInt();
        mRelatedTracksId = in.readInt();
        mIsInLibrary = in.readInt();
        mIsinMixer = in.readInt();
        mMixUserId = in.readInt();
        mStreamURL = in.readString();
        mBrainBeatsUser = (BrainBeatsUser) in.readParcelable(BrainBeatsUser.class.getClassLoader());
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