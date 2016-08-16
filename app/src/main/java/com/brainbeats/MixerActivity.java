package com.brainbeats;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;

import architecture.BaseActivity;
import fragments.MixerDetailFragment;
import fragments.MixerFragment;
import model.Mix;
import utils.Constants;

public class MixerActivity extends BaseActivity implements MixerFragment.OnFragmentInteractionListener {

    Fragment mMixerFragment;
    Fragment mMixerDetailFragment;
    Bundle mUserSelections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mMixerFragment = new MixerFragment();
        mMixerDetailFragment = new MixerDetailFragment();
        switchToMixerFragment();

        if (mUserSelections == null) {
            mUserSelections = new Bundle();
        }

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Mix mix = (Mix) intent.getExtras().get(Constants.KEY_EXTRA_SELECTED_MIX);
            loadMixerDetailFragment(mix);
        }
    }

    public void switchToMixerFragment() {
        replaceFragment(mMixerFragment, mMixerFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void loadMixerDetailFragment(Mix mix) {
        if (mDrawerToggle != null)
            toggleNavDrawerIcon();

        mUserSelections.putParcelable(Constants.KEY_EXTRA_SELECTED_MIX, mix);
        mMixerDetailFragment.setArguments(mUserSelections);
        replaceFragment(mMixerDetailFragment, mMixerDetailFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_global, menu);
        return true;
    }
}
