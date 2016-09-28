package com.brainbeats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.squareup.picasso.Picasso;

import com.brainbeats.architecture.AccountManager;
import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.entity.Track;
import com.brainbeats.fragments.DashboardDetailFragment;
import com.brainbeats.fragments.DashboardFragment;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.Mix;

import com.brainbeats.utils.Constants;
import com.brainbeats.sync.SyncManager;
import com.brainbeats.web.WebApiManager;

public class MainActivity extends BaseActivity implements View.OnClickListener, DashboardFragment.OnFragmentInteractionListener, DashboardDetailFragment.OnFragmentInteractionListener {

    public Fragment mDashboardFragment;
    public Fragment mDashboardDetailFragment;
    public CoordinatorLayout mCoordinatorLayout;
    private IntentFilter mIntentFilter;

    public FloatingActionButton mMainActionFab;
    public FloatingActionButton mExtraActionOneFab;
    public FloatingActionButton mExtraActionTwoFab;
    public FloatingActionButton mExtraActionThreeFab;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    public boolean mIsFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content_coordinator_layout);

        mMainActionFab = (FloatingActionButton) findViewById(R.id.main_action_fob);
        mExtraActionOneFab = (FloatingActionButton) findViewById(R.id.action_one_fob);
        mExtraActionTwoFab = (FloatingActionButton) findViewById(R.id.action_two_fob);
        mExtraActionThreeFab = (FloatingActionButton) findViewById(R.id.action_three_fob);

        fab_open = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_backward);

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
                        playTrack.setUser(new com.brainbeats.entity.User(mixUser));
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

        mMainActionFab.setOnClickListener(this);
        mExtraActionOneFab.setOnClickListener(this);
        mExtraActionTwoFab.setOnClickListener(this);
        mExtraActionThreeFab.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_action_fob:
                animateFAB();
                break;
            case R.id.action_one_fob:
                animateFAB();
                if(mDashboardFragment.isVisible())
                    ((DashboardFragment)mDashboardFragment).getTracks(WebApiManager.SOUND_CLOUD_QUERY_FILTER_PARAM_POPULAR);
                else if(mDashboardDetailFragment.isVisible())
                    ((DashboardDetailFragment)mDashboardDetailFragment).updateOfflineSyncManager(Constants.SyncDataAction.UpdateMix, null);
                    break;
            case R.id.action_two_fob:
                animateFAB();
                if(mDashboardFragment.isVisible())
                    ((DashboardFragment)mDashboardFragment).getTracks(WebApiManager.SOUND_CLOUD_QUERY_FILTER_PARAM_RECENT);
                else if(mDashboardDetailFragment.isVisible())
                    ((DashboardDetailFragment)mDashboardDetailFragment).updateOfflineSyncManager(Constants.SyncDataAction.UpdateFavorite, null);
                break;
            case R.id.action_three_fob:
                animateFAB();
                if(mDashboardFragment.isVisible())
                    ((DashboardFragment)mDashboardFragment).getTracks(WebApiManager.SOUND_CLOUD_QUERY_FILTER_PARAM_A_TO_Z);
                else if(mDashboardDetailFragment.isVisible())
                    ((DashboardDetailFragment)mDashboardDetailFragment).updateOfflineSyncManager(null, Constants.SyncDataType.Users);
                break;
        }
    }

    public void switchToDashboardFragment() {
        replaceFragment(mDashboardFragment, mDashboardFragment.getTag());
    }

    public void switchToBeatDetailFragment(Track track) {
        toggleNavDrawerIcon();
        Bundle args = new Bundle();
        if(mBound && mCurrentSong != null) //if another song is selected reset our player
            resetPlayer();

        args.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, track);
        mDashboardDetailFragment.setArguments(args);

        mMainActionFab.setImageDrawable(getDrawable(R.drawable.ic_android_white));
        mExtraActionOneFab.setImageDrawable(getDrawable(R.drawable.ic_library_add_white));
        mExtraActionTwoFab.setImageDrawable(getDrawable(R.drawable.ic_favorite_white));
        mExtraActionThreeFab.setImageDrawable(getDrawable(R.drawable.ic_person_add_white));

        replaceFragment(mDashboardDetailFragment, mDashboardDetailFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

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

    public void animateFAB() {
        if (mIsFabOpen) {
            mMainActionFab.startAnimation(rotate_backward);
            mExtraActionOneFab.startAnimation(fab_close);
            mExtraActionTwoFab.startAnimation(fab_close);
            mExtraActionOneFab.setClickable(false);
            mExtraActionTwoFab.setClickable(false);
            mExtraActionThreeFab.startAnimation(fab_close);
            mExtraActionThreeFab.setClickable(false);

            mIsFabOpen = false;
        } else {
            mMainActionFab.startAnimation(rotate_forward);
            mExtraActionOneFab.startAnimation(fab_open);
            mExtraActionTwoFab.startAnimation(fab_open);
            mExtraActionOneFab.setClickable(true);
            mExtraActionTwoFab.setClickable(true);
            mExtraActionThreeFab.startAnimation(fab_open);
            mExtraActionThreeFab.setClickable(true);

            mIsFabOpen = true;
        }
    }
}
