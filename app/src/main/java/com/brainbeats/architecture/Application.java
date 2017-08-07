package com.brainbeats.architecture;

import com.brainbeats.model.BrainBeatsUser;

/**
 * Created by Doug on 8/1/2017.
 */

public class Application extends android.app.Application {

    private BrainBeatsUser userDetails;

    public BrainBeatsUser getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(BrainBeatsUser userDetails) {
        this.userDetails = userDetails;
    }
}
