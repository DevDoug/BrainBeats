package com.brainbeats;

import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import architecture.AccountManager;
import architecture.BaseActivity;
import data.MixContract;
import data.MixDbHelper;
import entity.Track;
import fragments.DashboardDetailFragment;
import fragments.DashboardFragment;
import fragments.DashboardSongListFragment;
import utils.Constants;

public class MainActivity extends BaseActivity implements DashboardFragment.OnFragmentInteractionListener, DashboardSongListFragment.OnFragmentInteractionListener, DashboardDetailFragment.OnFragmentInteractionListener {

    public Fragment mDashboardFragment;
    public Fragment mDashboardSongListFragment;
    public Fragment mDashboardDetailFragment;
    Bundle mUserSelections;
    private CoordinatorLayout mCoordinatorLayout;
    public ContentObserver mDataObserver;

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

        if (!AccountManager.getInstance(this).isLoggedIn()) { //if the user has not created an account load login activity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content_coordinator_layout);

        mDataObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean selfChange) {
                Snackbar createdSnack = Snackbar.make(mCoordinatorLayout, R.string.error_favoriting_message, Snackbar.LENGTH_LONG);
                createdSnack.show();
            }
        };
        getContentResolver().registerContentObserver(MixContract.MixEntry.CONTENT_URI, false, mDataObserver);
    }

    public void switchToDashboardFragment() {
        replaceFragment(mDashboardFragment, mDashboardFragment.getTag());
    }

    public void loadRefinedBeatList(String searchParam) {
        toggleNavDrawerIcon();
        mUserSelections.putString(Constants.KEY_EXTRA_SEARCH_KEYWORD, searchParam);
        mDashboardSongListFragment.setArguments(mUserSelections);
        replaceFragment(mDashboardSongListFragment, mDashboardSongListFragment.getTag());
    }

    public void loadBeatDetailFragment(Track track) {
        toggleNavDrawerIcon();
        mUserSelections.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, track);
        mDashboardDetailFragment.setArguments(mUserSelections);
        replaceFragment(mDashboardDetailFragment, mDashboardDetailFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}

