package com.brainbeats.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import com.brainbeats.model.BrainBeatsUser;

/**
 * Created by douglas on 8/2/2016.
 */
public class User implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("permalink")
    private String permalink;

    @SerializedName("username")
    private String username;

    @SerializedName("uri")
    private String uri;

    @SerializedName("permalink_url")
    private String permalinkUrl;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("description")
    private String description;

    public User(){}

    public User(BrainBeatsUser modelBrainBeatsUser){
        id = (int) modelBrainBeatsUser.getSoundCloudUserId();
        username = modelBrainBeatsUser.getUserName();
        avatarUrl = modelBrainBeatsUser.getUserProfileImage();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(permalink);
        parcel.writeString(username);
        parcel.writeString(uri);
        parcel.writeString(permalinkUrl);
        parcel.writeString(avatarUrl);
        parcel.writeString(description);
    }

    protected User(Parcel in) {
        permalink = in.readString();
        username = in.readString();
        uri = in.readString();
        permalinkUrl = in.readString();
        avatarUrl = in.readString();
        description = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
