package com.brainbeats;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.fragments.InfoFragment;

public class InfoActivity extends BaseActivity implements InfoFragment.OnFragmentInteractionListener {

    public Fragment mInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mInfoFragment = new InfoFragment();

        switchToInfoFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hideMainFAB();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        navigateUpOrBack(this, fm);
    }

    public void switchToInfoFragment() {
        replaceFragment(mInfoFragment, mInfoFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
