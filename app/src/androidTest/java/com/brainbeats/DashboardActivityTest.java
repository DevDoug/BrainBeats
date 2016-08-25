package com.brainbeats;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * Created by douglas on 8/24/2016.
 * Should test dashboard activity
 */

@RunWith(AndroidJUnit4.class)
public class DashboardActivityTest {

    public DashboardActivityTest(){
        super();
    }

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
}
