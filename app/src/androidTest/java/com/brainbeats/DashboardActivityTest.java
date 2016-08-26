package com.brainbeats;
import android.app.Instrumentation;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.IdlingPolicies;

import android.support.test.espresso.InjectEventSecurityException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.UiController;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.LayoutAssertions.noEllipsizedText;
import static android.support.test.espresso.assertion.LayoutAssertions.noMultilineButtons;
import static android.support.test.espresso.assertion.LayoutAssertions.noOverlaps;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.assertion.ViewAssertions.selectedDescendantsMatch;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;


/**
 * Created by douglas on 8/24/2016.
 * Should test dashboard activity
 */

@RunWith(AndroidJUnit4.class)
public class DashboardActivityTest extends Instrumentation {

    private long mWaitingTime = 10000;

    public DashboardActivityTest(){
        super();
    }

    @org.junit.Before
    public void setUp() throws Exception {}

    @org.junit.After
    public void tearDown() throws Exception {}

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

/*    @Test
    public void testDashboardAppears() throws Exception {
        IdlingPolicies.setMasterPolicyTimeout(mWaitingTime * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(mWaitingTime * 2, TimeUnit.MILLISECONDS);

        //Set an idle resource to run while our network data is being called.
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(mWaitingTime);
        Espresso.registerIdlingResources(idlingResource);

        ConnectivityManager connectivityManager = (ConnectivityManager) mActivityRule.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isActiveNetworkInfo = (activeNetworkInfo != null && activeNetworkInfo.isConnected());

        //Check results.
        if(!isActiveNetworkInfo) //if the network is inactive correct message should be displayed
            onView(withText(mActivityRule.getActivity().getString(R.string.go_to_settings_message))).check(matches(isDisplayed()));
        else
            onView(withId(R.id.category_grid)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // Clean up.
        Espresso.unregisterIdlingResources(idlingResource);
    }*/

    @Test
    public void testAddTrackToUserLibrary(){
        //Set an idle resource to run while our network data is being called.
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(mWaitingTime);
        Espresso.registerIdlingResources(idlingResource);

        //Click on the first item.
        onView(withId(R.id.category_grid)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        //Scroll to the bottom of the view
        onView(withId(R.id.floating_action_button_track_options)).perform(scrollTo());

        //Add the first item to the user library.
        onView(withId(R.id.floating_action_button_track_options)).perform(click());

        IdlingResource idlingResourceTwo = new ElapsedTimeIdlingResource(mWaitingTime);
        Espresso.registerIdlingResources(idlingResource);

        //Click on add to add this item to the users item's
        onView(withId(R.id.floating_action_button_add_to_library)).perform(click());

        //Go back to dashboard.
        mActivityRule.getActivity().onBackPressed();

        onView(withId(R.id.navView)).perform(click());
    }
}
