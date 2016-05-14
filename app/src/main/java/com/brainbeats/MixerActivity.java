package com.brainbeats;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import architecture.BaseActivity;

public class MixerActivity extends BaseActivity implements MixerFragment.OnFragmentInteractionListener {

    Fragment mMixerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mMixerFragment = new MixerFragment();
        switchToMixerFragment();
    }

    public void switchToMixerFragment(){
        replaceFragment(mMixerFragment, mMixerFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
