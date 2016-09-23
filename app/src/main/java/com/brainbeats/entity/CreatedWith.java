package com.brainbeats.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by douglas on 6/6/2016.
 */
public class CreatedWith {

    @SerializedName("permalink_url")
    private String permalinkUrl;

    @SerializedName("name")
    private String name;

    @SerializedName("external_url")
    private String externalUrl;

    @SerializedName("uri")
    private String uri;

    @SerializedName("creator")
    private String creator;

    @SerializedName("id")
    private Integer id;

    @SerializedName("kind")
    private String kind;
}
