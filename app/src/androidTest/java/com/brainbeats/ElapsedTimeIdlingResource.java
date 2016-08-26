package com.brainbeats;

import android.support.test.espresso.IdlingResource;

/**
 * Created by douglas on 8/25/2016.
 */
public class ElapsedTimeIdlingResource implements IdlingResource {
    private long startTime;
    private long waitingTime;
    private ResourceCallback resourceCallback;

    public ElapsedTimeIdlingResource (long waitingTime){
        this.startTime = System.currentTimeMillis();
        this.waitingTime = waitingTime;
    }

    @Override
    public String getName() {
        return ElapsedTimeIdlingResource.class.getName() + ":" + waitingTime;
    }

    @Override
    public boolean isIdleNow() {
        long elapsed = System.currentTimeMillis() - startTime;
        boolean idle = (elapsed >= waitingTime);
        if (idle) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }
}
