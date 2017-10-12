package com.brainbeats;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.fragments.ArtistProfileFragment;
import com.brainbeats.fragments.SettingFragment;
import com.brainbeats.utils.Constants;

public class SettingsActivity extends BaseActivity implements SettingFragment.OnFragmentInteractionListener, ArtistProfileFragment.OnFragmentInteractionListener {

    public Fragment mSettingsFragment;
    public Fragment mArtistProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (savedInstanceState == null) {
            mSettingsFragment = new SettingFragment();
            mArtistProfileFragment = new ArtistProfileFragment();
            switchToSettingsFragment();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hideMainFAB();
    }

    public void switchToSettingsFragment() {
        replaceFragment(mSettingsFragment, mSettingsFragment.getTag());
    }

    public void switchToArtistProfileFragment(){
        toggleNavDrawerIcon();
        replaceFragment(mArtistProfileFragment, mArtistProfileFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.compareTo(Constants.GO_TO_ARTIST_PROFILE_URI) == 0)
            switchToArtistProfileFragment();
    }
}
