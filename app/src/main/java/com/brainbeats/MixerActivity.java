package com.brainbeats;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.brainbeats.R;

import architecture.BaseActivity;
import fragments.MixerDetailFragment;
import fragments.MixerFragment;

public class MixerActivity extends BaseActivity implements MixerFragment.OnFragmentInteractionListener {

    Fragment mMixerFragment;
    Fragment mMixerDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mMixerFragment = new MixerFragment();
        mMixerDetailFragment = new MixerDetailFragment();
        switchToMixerFragment();
    }

    public void switchToMixerFragment(){
        replaceFragment(mMixerFragment, mMixerFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void loadMixerDetailFragment(){
        toggleNavDrawerIcon();
        replaceFragment(mMixerDetailFragment, mMixerDetailFragment.getTag());
    }
}
