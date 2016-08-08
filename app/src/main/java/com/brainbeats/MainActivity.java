package com.brainbeats;

import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import architecture.AccountManager;
import architecture.BaseActivity;
import data.BrainBeatsContract;
import entity.Track;
import fragments.DashboardDetailFragment;
import fragments.DashboardFragment;
import utils.Constants;
import web.SyncManager;

public class MainActivity extends BaseActivity implements DashboardFragment.OnFragmentInteractionListener, DashboardDetailFragment.OnFragmentInteractionListener {

    public Fragment mDashboardFragment;
    public Fragment mDashboardDetailFragment;
    Bundle mUserSelections;
    public CoordinatorLayout mCoordinatorLayout;
    public ContentObserver mDataObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content_coordinator_layout);

        mDashboardFragment = new DashboardFragment();
        mDashboardDetailFragment = new DashboardDetailFragment();

        if (savedInstanceState != null) {
            Track track = savedInstanceState.getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
            boolean orientationChange = savedInstanceState.getBoolean("LayoutShiftDetail");
            if(orientationChange) {
                switchTBeatDetailFragment(track);
            }
        } else {
            mDashboardFragment = new DashboardFragment();
            mDashboardDetailFragment = new DashboardDetailFragment();
            switchToDashboardFragment();
        }

        if (mUserSelections == null) {
            mUserSelections = new Bundle();
        }

        if (!AccountManager.getInstance(this).isLoggedIn()) { //if the user has not created an account load login activity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        mDataObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean selfChange) {}

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                Log.i("",uri.toString());
            }
        };
        getContentResolver().registerContentObserver(BrainBeatsContract.MixEntry.CONTENT_URI, false, mDataObserver);
        getContentResolver().registerContentObserver(BrainBeatsContract.MixPlaylistEntry.CONTENT_URI, false, mDataObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AccountManager.getInstance(MainActivity.this).getGlobalSyncRequired()){
            SyncManager.getInstance().updateAllTables(AccountManager.getInstance(MainActivity.this).getUserId(),mAccount, BrainBeatsContract.CONTENT_AUTHORITY);
            AccountManager.getInstance(MainActivity.this).setGlobalSyncRequired(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AccountManager.getInstance(MainActivity.this).setGlobalSyncRequired(true);
    }

    public void switchToDashboardFragment() {
        replaceFragment(mDashboardFragment, mDashboardFragment.getTag());
    }

    public void switchTBeatDetailFragment(Track track) {
        toggleNavDrawerIcon();
        mUserSelections.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, track);
        mDashboardDetailFragment.setArguments(mUserSelections);
        replaceFragment(mDashboardDetailFragment, mDashboardDetailFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
