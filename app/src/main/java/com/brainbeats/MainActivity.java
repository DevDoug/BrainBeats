package com.brainbeats;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import architecture.BaseActivity;
import entity.Track;
import fragments.DashboardDetailFragment;
import fragments.DashboardFragment;
import fragments.DashboardSongListFragment;
import utils.Constants;

public class MainActivity extends BaseActivity implements DashboardFragment.OnFragmentInteractionListener, DashboardSongListFragment.OnFragmentInteractionListener, DashboardDetailFragment.OnFragmentInteractionListener{

    Fragment mDashboardFragment;
    Fragment mDashboardSongListFragment;
    Fragment mDashboardDetailFragment;
    Bundle mUserSelections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mDashboardFragment = new DashboardFragment();
        mDashboardSongListFragment = new DashboardSongListFragment();
        mDashboardDetailFragment = new DashboardDetailFragment();
        switchToDashboardFragment();

        if (mUserSelections == null) {
            mUserSelections = new Bundle();
        }
    }

    public void switchToDashboardFragment(){
        replaceFragment(mDashboardFragment, mDashboardFragment.getTag());
    }

    public void loadRefinedBeatList(String searchParam){
        toggleNavDrawerIcon();
        mUserSelections.putString(Constants.KEY_EXTRA_SEARCH_KEYWORD,searchParam);
        mDashboardSongListFragment.setArguments(mUserSelections);
        replaceFragment(mDashboardSongListFragment, mDashboardSongListFragment.getTag());
    }

    public void loadBeatDetailFragment(Track track){
        toggleNavDrawerIcon();
        mUserSelections.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK,track);
        mDashboardDetailFragment.setArguments(mUserSelections);
        replaceFragment(mDashboardDetailFragment, mDashboardDetailFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
