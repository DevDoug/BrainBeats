package com.brainbeats.entity;

import com.google.gson.annotations.SerializedName;

/*
 * Created by douglas on 6/2/2016.
 */

public class Collection {

    @SerializedName("artwork_url")
    private String artworkUrl;

    @SerializedName("commentable")
    private Boolean commentable;

    @SerializedName("comment_count")
    private Integer commentCount;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("description")
    private String description;

    @SerializedName("downloadable")
    private Boolean downloadable;

    @SerializedName("download_count")
    private Integer downloadCount;

    @SerializedName("download_url")
    private Object downloadUrl;

    @SerializedName("duration")
    private Integer duration;

    @SerializedName("full_duration")
    private Integer fullDuration;

    @SerializedName("embeddable_by")
    private String embeddableBy;

    @SerializedName("genre")
    private String genre;

    @SerializedName("has_downloads_left")
    private Boolean hasDownloadsLeft;

    @SerializedName("id")
    private Integer id;

    @SerializedName("kind")
    private String kind;

    @SerializedName("label_name")
    private String labelName;

    @SerializedName("last_modified")
    private String lastModified;

    @SerializedName("license")
    private String license;

    @SerializedName("likes_count")
    private Integer likesCount;

    @SerializedName("permalink")
    private String permalink;

    @SerializedName("permalink_url")
    private String permalinkUrl;

    @SerializedName("playback_count")
    private Integer playbackCount;

    @SerializedName("public")
    private Boolean _public;

    @SerializedName("publisher_metadata")
    private Object publisherMetadata;

    @SerializedName("purchase_title")
    private Object purchaseTitle;

    @SerializedName("purchase_url")
    private Object purchaseUrl;

    @SerializedName("release_date")
    private Object releaseDate;

    @SerializedName("reposts_count")
    private Integer repostsCount;

    @SerializedName("secret_token")
    private Object secretToken;

    @SerializedName("sharing")
    private String sharing;

    @SerializedName("state")
    private String state;

    @SerializedName("streamable")
    private Boolean streamable;

    @SerializedName("tag_list")
    private String tagList;

    @SerializedName("title")
    private String title;

    @SerializedName("uri")
    private String uri;

    @SerializedName("urn")
    private String urn;

    @SerializedName("user_id")
    private Integer userId;

    @SerializedName("visuals")
    private Object visuals;

    @SerializedName("waveform_url")
    private String waveformUrl;

    @SerializedName("monetization_model")
    private String monetizationModel;

    @SerializedName("policy")
    private String policy;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("user")
    private User user;

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public Boolean getCommentable() {
        return commentable;
    }

    public void setCommentable(Boolean commentable) {
        this.commentable = commentable;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDownloadable() {
        return downloadable;
    }

    public void setDownloadable(Boolean downloadable) {
        this.downloadable = downloadable;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Object getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(Object downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getFullDuration() {
        return fullDuration;
    }

    public void setFullDuration(Integer fullDuration) {
        this.fullDuration = fullDuration;
    }

    public String getEmbeddableBy() {
        return embeddableBy;
    }

    public void setEmbeddableBy(String embeddableBy) {
        this.embeddableBy = embeddableBy;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Boolean getHasDownloadsLeft() {
        return hasDownloadsLeft;
    }

    public void setHasDownloadsLeft(Boolean hasDownloadsLeft) {
        this.hasDownloadsLeft = hasDownloadsLeft;
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

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public Integer getPlaybackCount() {
        return playbackCount;
    }

    public void setPlaybackCount(Integer playbackCount) {
        this.playbackCount = playbackCount;
    }

    public Boolean getPublic() {
        return _public;
    }

    public void setPublic(Boolean _public) {
        this._public = _public;
    }

    public Object getPublisherMetadata() {
        return publisherMetadata;
    }

    public void setPublisherMetadata(Object publisherMetadata) {
        this.publisherMetadata = publisherMetadata;
    }

    public Object getPurchaseTitle() {
        return purchaseTitle;
    }

    public void setPurchaseTitle(Object purchaseTitle) {
        this.purchaseTitle = purchaseTitle;
    }

    public Object getPurchaseUrl() {
        return purchaseUrl;
    }

    public void setPurchaseUrl(Object purchaseUrl) {
        this.purchaseUrl = purchaseUrl;
    }

    public Object getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Object releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(Integer repostsCount) {
        this.repostsCount = repostsCount;
    }

    public Object getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(Object secretToken) {
        this.secretToken = secretToken;
    }

    public String getSharing() {
        return sharing;
    }

    public void setSharing(String sharing) {
        this.sharing = sharing;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getStreamable() {
        return streamable;
    }

    public void setStreamable(Boolean streamable) {
        this.streamable = streamable;
    }

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Object getVisuals() {
        return visuals;
    }

    public void setVisuals(Object visuals) {
        this.visuals = visuals;
    }

    public String getWaveformUrl() {
        return waveformUrl;
    }

    public void setWaveformUrl(String waveformUrl) {
        this.waveformUrl = waveformUrl;
    }

    public String getMonetizationModel() {
        return monetizationModel;
    }

    public void setMonetizationModel(String monetizationModel) {
        this.monetizationModel = monetizationModel;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public void setStreamURL(String streamURL) {
        this.mStreamURL = streamURL;
    }
}
