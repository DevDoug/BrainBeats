package com.brainbeats.model;

/*
 * Created by Doug on 7/28/2017.
 */

public class FriendRequest {

    public String status;

    BrainBeatsUser sender;

    BrainBeatsUser receiver;

    public FriendRequest() {}

    public FriendRequest(String status, BrainBeatsUser sender, BrainBeatsUser receiver) {
        this.status = status;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BrainBeatsUser getSender() {return sender;}

    public void setSender(BrainBeatsUser sender) {this.sender = sender;}


}