package entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by douglas on 5/24/2016.
 */
public class SoundCloudUser {

    @SerializedName("id")
    private Integer id;

    @SerializedName("kind")
    private String kind;

    @SerializedName("permalink")
    private String permalink;

    @SerializedName("username")
    private String username;

    @SerializedName("last_modified")
    private String lastModified;

    @SerializedName("uri")
    private String uri;

    @SerializedName("permalink_url")
    private String permalinkUrl;

    @SerializedName("avatar_url")
    private String avatarUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
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

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
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
}
