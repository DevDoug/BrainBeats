package com.brainbeats;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.brainbeats.R;

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
    }

    public void switchToMixerFragment(){
        replaceFragment(mMixerFragment, mMixerFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void loadMixerDetailFragment(Mix mix){
        toggleNavDrawerIcon();
        mUserSelections.putParcelable(Constants.KEY_EXTRA_SELECTED_MIX,mix);
        mMixerDetailFragment.setArguments(mUserSelections);
        replaceFragment(mMixerDetailFragment, mMixerDetailFragment.getTag());
    }
}
