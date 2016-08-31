package com.brainbeats;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import data.BrainBeatsContentProvider;
import data.BrainBeatsContract;
import data.BrainBeatsDbHelper;
import utils.Constants;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Created by douglas on 8/15/2016.
 * Should test the login activity with incorrect username pass and incorrect format. Should also test create new user
 */

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest extends ProviderTestCase2 {

    public String testIncorrectEmail = "fake.com";
    public String testCorrectEmail = "Doug4less@gmail.com";
    public String testIncorrectPassword = "le";
    public String testCorrectPassword = "tacos";

    public LoginActivityTest(){
        super(BrainBeatsContentProvider.class,"com.brainbeats");
    }

    @org.junit.Before
    public void setUp() throws Exception {
        setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testEmptyCredentialsLogin() throws Exception{
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.email_text_input)).check(matches(hasErrorText("This field is required.")));
    }

    @Test
    public void testIncorrectEmailFormatLogin() throws Exception{
        onView(withId(R.id.email_text_input)).perform(typeText(testIncorrectEmail), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.email_text_input)).check(matches(hasErrorText("This email address is invalid.")));
    }

    @Test
    public void testIncorrectPassword() throws Exception {
        onView(withId(R.id.email_text_input)).perform(typeText(testCorrectEmail), closeSoftKeyboard());
        onView(withId(R.id.password_text_input)).perform(click());
        onView(withId(R.id.password_text_input)).perform(typeText(testIncorrectPassword), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.password_text_input)).check(matches(hasErrorText("This password is too short.")));
    }

    @Test
    public void testBrainBeatsUserCreatedSuccessfully() throws Exception {
        onView(withId(R.id.email_text_input)).perform(typeText(testCorrectEmail), closeSoftKeyboard());
        onView(withId(R.id.password_text_input)).perform(click());
        onView(withId(R.id.password_text_input)).perform(typeText(testCorrectPassword), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());

        //If we have reached this point and not returned a false this user username does not exist so create a new account.
        ContentValues values = new ContentValues();
        values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME, testCorrectEmail);
        values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_PASSWORD, testCorrectPassword);
        Uri returnRow = getMockContentResolver().insert(BrainBeatsContract.UserEntry.CONTENT_URI, values);

        //Add check to database here to see if account was created successfully
        Cursor userCursor = getMockContentResolver().query(
                BrainBeatsContract.UserEntry.CONTENT_URI, //Get users.
                null,  //Return everything.
                BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                new String[]{testCorrectEmail},
                null);

        if (userCursor != null) {
            assertTrue(userCursor.getCount() > 0); //assert that we entered our record
            userCursor.close();
        }
    }
}