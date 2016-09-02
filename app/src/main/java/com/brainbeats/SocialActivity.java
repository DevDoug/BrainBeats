package com.brainbeats;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;

import architecture.BaseActivity;
import entity.Track;
import fragments.SocialFragment;
import fragments.UserProfileFragment;
import model.Mix;
import utils.Constants;

public class SocialActivity extends BaseActivity implements SocialFragment.OnFragmentInteractionListener {

    Fragment mSocialFragment;
    Fragment mUserProfileFragment;
    Track mPlayingTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mSocialFragment = new SocialFragment();
        mUserProfileFragment = new UserProfileFragment();
        switchToSocialFragment();

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String intentAction = intent.getAction();

            if (intentAction.equalsIgnoreCase(Constants.INTENT_ACTION_DISPLAY_CURRENT_TRACK)){
                mPlayingTrack = (Track) intent.getExtras().get(Constants.KEY_EXTRA_SELECTED_TRACK);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(mPlayingTrack != null){
            mCurrentSong = mPlayingTrack;
            updateCurrentSongNotificationUI();
        }
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
