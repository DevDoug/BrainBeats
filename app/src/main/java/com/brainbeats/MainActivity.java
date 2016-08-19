package com.brainbeats;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import architecture.AccountManager;
import architecture.BaseActivity;
import data.BrainBeatsContract;
import entity.Track;
import entity.User;
import fragments.DashboardDetailFragment;
import fragments.DashboardFragment;
import model.Mix;
import utils.Constants;
import sync.SyncManager;

public class MainActivity extends BaseActivity implements DashboardFragment.OnFragmentInteractionListener, DashboardDetailFragment.OnFragmentInteractionListener {

    public Fragment mDashboardFragment;
    public Fragment mDashboardDetailFragment;
    public Bundle mUserSelections;
    public CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content_coordinator_layout);

        mDashboardFragment = new DashboardFragment();
        mDashboardDetailFragment = new DashboardDetailFragment();

        if (savedInstanceState != null) { //If our activity is recreated.
            Track track = savedInstanceState.getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
            boolean orientationChange = savedInstanceState.getBoolean(Constants.OREINTATION_SHIFT);
            if (orientationChange) {
                switchTBeatDetailFragment(track);
            }
        } else {
            mDashboardFragment = new DashboardFragment();
            mDashboardDetailFragment = new DashboardDetailFragment();
            switchToDashboardFragment();
        }

        Bundle intentBundle = getIntent().getExtras(); //If an intent is passed to main activity.
        if (intentBundle != null) {
            if (intentBundle.get(Constants.KEY_EXTRA_SELECTED_MIX) != null) {
                Mix sentMix = (Mix) intentBundle.get(Constants.KEY_EXTRA_SELECTED_MIX);
                Track playTrack = new Track();
                if (sentMix != null) {
                    playTrack.setTitle(sentMix.getMixTitle());
                    playTrack.setArtworkURL(sentMix.getMixAlbumCoverArt());
                    playTrack.setID(sentMix.getSoundCloudId());
                    playTrack.setStreamURL(sentMix.getStreamURL());
                    User scUser = new User();
                    scUser.setUsername(sentMix.getUser().getUserName());
                    playTrack.setUser(scUser);
                    switchTBeatDetailFragment(playTrack);
                }
            }
        }

        if (!AccountManager.getInstance(this).isLoggedIn()) { //if the user has not created an account load login activity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        SyncManager.getInstance().updateAllTables(AccountManager.getInstance(MainActivity.this).getUserId(), mAccount, BrainBeatsContract.CONTENT_AUTHORITY);

/*        if (SyncManager.getInstance().getIsGlobalSyncRequired()) {
            SyncManager.getInstance().updateAllTables(AccountManager.getInstance(MainActivity.this).getUserId(), mAccount, BrainBeatsContract.CONTENT_AUTHORITY);
            SyncManager.mIsGlobalSyncRequired = false;
        }*/
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

        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, track);
        mDashboardDetailFragment.setArguments(args);
        replaceFragment(mDashboardDetailFragment, mDashboardDetailFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
