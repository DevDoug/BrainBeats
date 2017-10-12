package com.brainbeats.model;

/*
 * Created by Doug on 7/28/2017.
 */

public class FriendRequest {

    public String status;

    BrainBeatsUser user;

    public FriendRequest() {}

    public FriendRequest(String status, BrainBeatsUser user) {
        this.status = status;
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BrainBeatsUser getUser() {return user;}

    public void setUser(BrainBeatsUser user) {this.user = user;}
}