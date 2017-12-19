package com.brainbeats.model;

/*
 * Created by Doug on 7/28/2017.
 */

public class FriendRequest {

    public String status;

    public String senderId;

    public String receiverId;

    public FriendRequest() {
    }

    public FriendRequest(String status, String senderId, String receiverId) {
        this.status = status;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
/*    //    BrainBeatsUser sender;
//
//    BrainBeatsUser receiver;

    public FriendRequest() {}

    public FriendRequest(String status, BrainBeatsUser sender, BrainBeatsUser receiver) {
        this.status = status;
//        this.sender = sender;
//        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public BrainBeatsUser getSender() {return sender;}
//
//    public void setSender(BrainBeatsUser sender) {this.sender = sender;}*/


}