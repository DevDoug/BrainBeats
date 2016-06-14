package entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by douglas on 5/24/2016.
 */
public class User {

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
}
