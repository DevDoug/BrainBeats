package entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by douglas on 8/2/2016.
 */
public class UserCollectionEntry {


    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("city")
    private String city;

    @SerializedName("description")
    private String description;

    @SerializedName("country")
    private String country;

    @SerializedName("track_count")
    private Integer trackCount;

    @SerializedName("public_favorites_count")
    private Integer publicFavoritesCount;

    @SerializedName("followers_count")
    private Integer followersCount;

    @SerializedName("followings_count")
    private Integer followingsCount;

    @SerializedName("plan")
    private String plan;

    @SerializedName("myspace_name")
    private String myspaceName;

    @SerializedName("discogs_name")
    private Object discogsName;

    @SerializedName("website_title")
    private String websiteTitle;

    @SerializedName("website")
    private String website;

    @SerializedName("reposts_count")
    private Integer repostsCount;

    @SerializedName("comments_count")
    private Integer commentsCount;

    @SerializedName("online")
    private Boolean online;

    @SerializedName("likes_count")
    private Integer likesCount;

    @SerializedName("playlist_count")
    private Integer playlistCount;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("id")
    private Integer id;

    @SerializedName("kind")
    private String kind;

    @SerializedName("permalink_url")
    private String permalinkUrl;

    @SerializedName("uri")
    private String uri;

    @SerializedName("username")
    private String username;

    @SerializedName("permalink")
    private String permalink;

    @SerializedName("last_modified")
    private String lastModified;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }

    public Integer getPublicFavoritesCount() {
        return publicFavoritesCount;
    }

    public void setPublicFavoritesCount(Integer publicFavoritesCount) {
        this.publicFavoritesCount = publicFavoritesCount;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFollowingsCount() {
        return followingsCount;
    }

    public void setFollowingsCount(Integer followingsCount) {
        this.followingsCount = followingsCount;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getMyspaceName() {
        return myspaceName;
    }

    public void setMyspaceName(String myspaceName) {
        this.myspaceName = myspaceName;
    }

    public Object getDiscogsName() {
        return discogsName;
    }

    public void setDiscogsName(Object discogsName) {
        this.discogsName = discogsName;
    }

    public String getWebsiteTitle() {
        return websiteTitle;
    }

    public void setWebsiteTitle(String websiteTitle) {
        this.websiteTitle = websiteTitle;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(Integer repostsCount) {
        this.repostsCount = repostsCount;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getPlaylistCount() {
        return playlistCount;
    }

    public void setPlaylistCount(Integer playlistCount) {
        this.playlistCount = playlistCount;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

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

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

}
