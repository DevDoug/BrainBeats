package com.brainbeats;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;

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

    public void switchToSocialFragment() {
        replaceFragment(mSocialFragment, mSocialFragment.getTag());
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
