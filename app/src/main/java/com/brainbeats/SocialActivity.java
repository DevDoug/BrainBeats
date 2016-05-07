package com.brainbeats;

import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import architecture.BaseActivity;

public class SocialActivity extends BaseActivity {

    Fragment mSocialFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mSocialFragment = new SocialFragment();
        switchToLibraryFragment();
    }

    public void switchToLibraryFragment(){
        replaceFragment(mSocialFragment, mSocialFragment.getTag());
    }
}
