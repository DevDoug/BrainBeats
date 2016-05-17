package com.brainbeats;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import architecture.BaseActivity;
import fragments.SocialFragment;

public class SocialActivity extends BaseActivity implements SocialFragment.OnFragmentInteractionListener {

    Fragment mSocialFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mSocialFragment = new SocialFragment();
        switchToSocialFragment();
    }

    public void switchToSocialFragment(){
        replaceFragment(mSocialFragment, mSocialFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
