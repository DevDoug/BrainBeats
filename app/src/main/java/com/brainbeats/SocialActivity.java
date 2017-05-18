package com.brainbeats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;

import com.squareup.picasso.Picasso;

import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.entity.Track;
import com.brainbeats.fragments.SocialFragment;
import com.brainbeats.fragments.UserProfileFragment;

import com.brainbeats.utils.Constants;

public class SocialActivity extends BaseActivity implements SocialFragment.OnFragmentInteractionListener {

    Fragment mSocialFragment;
    Fragment mUserProfileFragment;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mMainActionFab = (FloatingActionButton) findViewById(R.id.main_action_fob);
        mSocialFragment = new SocialFragment();
        mUserProfileFragment = new UserProfileFragment();
        switchToSocialFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hideMainFAB();
    }

    public void switchToSocialFragment() {
        replaceFragment(mSocialFragment, mSocialFragment.getTag());
    }

    public void switchToUserProfileFragment() {
        replaceFragment(mUserProfileFragment, mUserProfileFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_global, menu);
        return true;
    }
}
