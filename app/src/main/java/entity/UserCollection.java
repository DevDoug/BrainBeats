package entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by douglas on 8/2/2016.
 */
public class UserCollection {

    @SerializedName("collection")
    private List<UserCollectionEntry> collection = new ArrayList<UserCollectionEntry>();

    @SerializedName("next_href")
    private Object nextHref;

    public List<UserCollectionEntry> getCollection() {
        return collection;
    }

    public void setCollection(List<UserCollectionEntry> collection) {
        this.collection = collection;
    }

    public Object getNextHref() {
        return nextHref;
    }

    public void setNextHref(Object nextHref) {
        this.nextHref = nextHref;
    }
}
