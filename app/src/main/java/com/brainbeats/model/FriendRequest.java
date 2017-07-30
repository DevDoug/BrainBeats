package com.brainbeats.model;

/**
 * Created by Doug on 7/28/2017.
 */

public class FriendRequest {

    public enum FriendRequestStatus{
        Pending,
        Accepted,
        Rejected,
        Blocked
    }

    public String userId;

    public String friendId;

    public FriendRequestStatus status;

    public FriendRequest(String userId, String friendId, FriendRequestStatus pending) {
        this.userId = userId;
        this.friendId = friendId;
        this.status = pending;
    }
}
