package entity;

import com.google.gson.annotations.SerializedName;

import model.BrainBeatsUser;

/**
 * Created by douglas on 8/2/2016.
 */
public class User {

    @SerializedName("id")
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
