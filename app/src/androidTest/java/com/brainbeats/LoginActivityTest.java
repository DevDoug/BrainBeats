package com.brainbeats;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import utils.Constants;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;


/**
 * Created by douglas on 8/15/2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest {

    @Mock
    Context mMockContext;

    public LoginActivityTest() {
        MockitoAnnotations.initMocks(LoginActivityTest.class);

        try {
            testAttemptSoundCloudLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAttemptSoundCloudLogin() throws Exception {

        ConnectivityManager connectivityManager = (ConnectivityManager) mMockContext. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean network =  (activeNetworkInfo != null && activeNetworkInfo.isConnected());

        if(network){

        } else {
            onView(withText(mMockContext.getString(R.string.enable_wifi_in_settings_message))).check(matches(isDisplayed()));
        }
    }
}