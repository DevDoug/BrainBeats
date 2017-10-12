package com.brainbeats.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by douglas on 6/6/2016.
 */

public class UserPlaylistsResponse {

    @SerializedName("duration")
    private Integer duration;

    @SerializedName("release_day")
    private Object releaseDay;

    @SerializedName("permalink_url")
    private String permalinkUrl;

    @SerializedName("genre")
    private String genre;

    @SerializedName("permalink")
    private String permalink;

    @SerializedName("purchase_url")
    private Object purchaseUrl;

    @SerializedName("release_month")
    private Object releaseMonth;

    @SerializedName("description")
    private String description;

    @SerializedName("uri")
    private String uri;

    @SerializedName("label_name")
    private String labelName;

    @SerializedName("tag_list")
    private String tagList;

    @SerializedName("release_year")
    private Object releaseYear;

    @SerializedName("track_count")
    private Integer trackCount;

    @SerializedName("user_id")
    private Integer userId;

    @SerializedName("last_modified")
    private String lastModified;

    @SerializedName("license")
    private String license;

    @SerializedName("tracks")
    private List<Track> tracks = new ArrayList<Track>();

    @SerializedName("playlist_type")
    private Object playlistType;

    @SerializedName("id")
    private Integer id;

    @SerializedName("downloadable")
    private Boolean downloadable;

    @SerializedName("sharing")
    private String sharing;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("release")
    private String release;

    @SerializedName("kind")
    private String kind;

    @SerializedName("title")
    private String title;

    @SerializedName("type")
    private Object type;

    @SerializedName("purchase_title")
    private Object purchaseTitle;

    @SerializedName("created_with")
    private CreatedWith createdWith;

    @SerializedName("artwork_url")
    private String artworkUrl;

    @SerializedName("ean")
    private String ean;

    @SerializedName("streamable")
    private Boolean streamable;

    @SerializedName("soundCloudUser")
    private SoundCloudUser soundCloudUser;

    @SerializedName("embeddable_by")
    private String embeddableBy;

    @SerializedName("label_id")
    private Object labelId;


    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Object getReleaseDay() {
        return releaseDay;
    }

    public void setReleaseDay(Object releaseDay) {
        this.releaseDay = releaseDay;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public Object getPurchaseUrl() {
        return purchaseUrl;
    }

    public void setPurchaseUrl(Object purchaseUrl) {
        this.purchaseUrl = purchaseUrl;
    }

    public Object getReleaseMonth() {
        return releaseMonth;
    }

    public void setReleaseMonth(Object releaseMonth) {
        this.releaseMonth = releaseMonth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public Object getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Object releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public Object getPlaylistType() {
        return playlistType;
    }

    public void setPlaylistType(Object playlistType) {
        this.playlistType = playlistType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDownloadable() {
        return downloadable;
    }

    public void setDownloadable(Boolean downloadable) {
        this.downloadable = downloadable;
    }

    public String getSharing() {
        return sharing;
    }

    public void setSharing(String sharing) {
        this.sharing = sharing;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getPurchaseTitle() {
        return purchaseTitle;
    }

    public void setPurchaseTitle(Object purchaseTitle) {
        this.purchaseTitle = purchaseTitle;
    }

    public CreatedWith getCreatedWith() {
        return createdWith;
    }

    public void setCreatedWith(CreatedWith createdWith) {
        this.createdWith = createdWith;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public Boolean getStreamable() {
        return streamable;
    }

    public void setStreamable(Boolean streamable) {
        this.streamable = streamable;
    }

    public SoundCloudUser getSoundCloudUser() {
        return soundCloudUser;
    }

    public void setSoundCloudUser(SoundCloudUser soundCloudUser) {
        this.soundCloudUser = soundCloudUser;
    }

    public String getEmbeddableBy() {
        return embeddableBy;
    }

    public void setEmbeddableBy(String embeddableBy) {
        this.embeddableBy = embeddableBy;
    }

    public Object getLabelId() {
        return labelId;
    }

    public void setLabelId(Object labelId) {
        this.labelId = labelId;
    }
}
