package com.brainbeats.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by douglas on 6/2/2016.
 */

public class RelatedTracksResponse {

    @SerializedName("collection")
    private List<Collection> collection = new ArrayList<Collection>();

    @SerializedName("next_href")
    String mNextHref;

    @SerializedName("query_urn")
    String mQueryUrn;

    @SerializedName("variant")
    String mVariant;

    public List<Collection> getCollection() {
        return collection;
    }

    public void setCollection(List<Collection> collection) {
        this.collection = collection;
    }
}
