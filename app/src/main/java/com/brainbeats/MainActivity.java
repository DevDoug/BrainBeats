package com.brainbeats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import architecture.AccountManager;
import architecture.BaseActivity;
import data.BrainBeatsContract;
import entity.Track;
import entity.User;
import fragments.DashboardDetailFragment;
import fragments.DashboardFragment;
import model.BrainBeatsUser;
import model.Mix;
import service.AudioService;
import utils.Constants;
import sync.SyncManager;

public class MainActivity extends BaseActivity implements DashboardFragment.OnFragmentInteractionListener, DashboardDetailFragment.OnFragmentInteractionListener {

    public Fragment mDashboardFragment;
    public Fragment mDashboardDetailFragment;
    public CoordinatorLayout mCoordinatorLayout;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content_coordinator_layout);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.SONG_COMPLETE_BROADCAST_ACTION);

        mDashboardFragment = new DashboardFragment();
        mDashboardDetailFragment = new DashboardDetailFragment();

        if (savedInstanceState != null) { //If our activity is recreated.
            Track track = savedInstanceState.getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
            boolean orientationChange = savedInstanceState.getBoolean(Constants.OREINTATION_SHIFT);
            if (orientationChange) {
                switchToBeatDetailFragment(track);
            }
        } else {
            mDashboardFragment = new DashboardFragment();
            mDashboardDetailFragment = new DashboardDetailFragment();
            switchToDashboardFragment();
        }

        Bundle intentBundle = getIntent().getExtras(); //If an intent is passed to main activity.
        if (intentBundle != null) {
            if (intentBundle.get(Constants.KEY_EXTRA_SELECTED_MIX) != null) {
                if(getIntent().getAction().equalsIgnoreCase(Constants.INTENT_ACTION_GO_TO_DETAIL_FRAGMENT)) {
                    Mix sentMix = (Mix) intentBundle.get(Constants.KEY_EXTRA_SELECTED_MIX);
                    BrainBeatsUser mixUser = (BrainBeatsUser) intentBundle.get(Constants.KEY_EXTRA_SELECTED_USER);
                    if (sentMix != null) {
                        Track playTrack = new Track(sentMix);
                        playTrack.setUser(new entity.User(mixUser));
                        switchToBeatDetailFragment(playTrack);
                    }
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
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SyncManager.getInstance().getIsGlobalSyncRequired()) {
            SyncManager.getInstance().updateAllTables(AccountManager.getInstance(MainActivity.this).getUserId(), mAccount, BrainBeatsContract.CONTENT_AUTHORITY);
            SyncManager.mIsGlobalSyncRequired = false;
        }
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AccountManager.getInstance(MainActivity.this).setGlobalSyncRequired(true);
    }

    public void switchToDashboardFragment() {
        replaceFragment(mDashboardFragment, mDashboardFragment.getTag());
    }

    public void switchToBeatDetailFragment(Track track) {
        toggleNavDrawerIcon();
        Bundle args = new Bundle();
        if(mBound && mCurrentSong != null) //if another song is selected reset our player
            resetPlayer();

        if(mCurrentSongPlayingView != null)
            mCurrentSongPlayingView.setVisibility(View.INVISIBLE);

        args.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, track);
        mDashboardDetailFragment.setArguments(args);
        replaceFragment(mDashboardDetailFragment, mDashboardDetailFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.SONG_COMPLETE_BROADCAST_ACTION)) {
                Track newTrack = (Track) intent.getExtras().getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
                if(mDashboardDetailFragment.isVisible()){ //if they are on the dashboard detail screen update the detail widgets
                    (((DashboardDetailFragment) mDashboardDetailFragment)).updateTrackUI(newTrack);
                } else { // else update the current playing notification view
                    mCurrentSongTitle.setText(newTrack.getTitle());
                    if (newTrack.getArtworkURL() == null)
                        mAlbumThumbnail.setImageResource(R.drawable.placeholder);
                    else
                        Picasso.with(MainActivity.this).load(newTrack.getArtworkURL()).into(mAlbumThumbnail);

                    mCurrentSongArtistName.setText(newTrack.getUser().getUsername());
                }
            }
        }
    };
}
