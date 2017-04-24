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

import com.brainbeats.fragments.MusicDetailFragment;
import com.brainbeats.fragments.BrowseMusicFragment;

import com.brainbeats.architecture.AccountManager;
import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.entity.Track;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.Mix;

import com.brainbeats.utils.Constants;
import com.brainbeats.sync.SyncManager;

public class MainActivity extends BaseActivity implements View.OnClickListener, BrowseMusicFragment.OnFragmentInteractionListener, MusicDetailFragment.OnFragmentInteractionListener {

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

        if (savedInstanceState == null) {
            mDashboardFragment = new BrowseMusicFragment();
            mDashboardDetailFragment = new MusicDetailFragment();
            switchToDashboardFragment();
        }

        Bundle intentBundle = getIntent().getExtras(); //If an intent is passed to main activity.
        if (intentBundle != null) {
            if (intentBundle.get(Constants.KEY_EXTRA_SELECTED_MIX) != null) {
                Mix sentMix = (Mix) intentBundle.get(Constants.KEY_EXTRA_SELECTED_MIX);
                BrainBeatsUser mixUser = (BrainBeatsUser) intentBundle.get(Constants.KEY_EXTRA_SELECTED_USER);

                if (getIntent().getAction().equalsIgnoreCase(Constants.INTENT_ACTION_GO_TO_DETAIL_FRAGMENT)) {
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

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.SONG_COMPLETE_BROADCAST_ACTION);
        mIntentFilter.addAction(Constants.SONG_LOADING_BROADCAST_ACTION);

        mMainActionFab.setVisibility(View.INVISIBLE);
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
                break;
            case R.id.action_one_fob:
                ((MusicDetailFragment) mDashboardDetailFragment).updateOfflineSyncManager(Constants.SyncDataAction.UpdateMix, null);
                break;
            case R.id.action_two_fob:
                ((MusicDetailFragment) mDashboardDetailFragment).updateOfflineSyncManager(Constants.SyncDataAction.UpdateFavorite, null);
                break;
            case R.id.action_three_fob:
                ((MusicDetailFragment) mDashboardDetailFragment).updateOfflineSyncManager(null, Constants.SyncDataType.Users);
                break;
        }
    }

    public void switchToDashboardFragment() {
        replaceFragment(mDashboardFragment, mDashboardFragment.getTag());
    }

    public void switchToBeatDetailFragment(Track track) {
        toggleNavDrawerIcon();
        mDashboardDetailFragment = MusicDetailFragment.newInstance(track);
        mCurrentSong = track; //set the clicked song to the current playing
        replaceFragment(mDashboardDetailFragment, mDashboardDetailFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.compareTo(Constants.DASHBOARD_DETAIL_LOAD_SONG_URI) == 0) {
            if (mAudioService.mPlayingSong == null)
                mAudioService.mPlayingSong = mCurrentSong;
            mAudioService.playSong(Uri.parse(mAudioService.mPlayingSong.getStreamURL()));
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_LOAD_NEW_SONG_URI) == 0) {
            mAudioService.mPlayingSong = mCurrentSong;
            mAudioService.playSong(Uri.parse(mAudioService.mPlayingSong.getStreamURL()));
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_PLAY_SONG_URI) == 0) {
            mAudioService.resumeSong();
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_PAUSE_SONG_URI) == 0) {
            mAudioService.pauseSong();
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_DOWNVOTE_SONG_URI) == 0) {
            mAudioService.loadNextTrack();
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_SKIP_FORWARD_URI) == 0) {
            mAudioService.loadNextTrack();
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_SET_SONG_REPEAT_URI) == 0) {
            mAudioService.setSongLooping(true);
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_UPDATE_CURRENT_PLAYING_SONG_VIEW) == 0) {
            if (mAudioService.getIsPlaying() || mAudioService.mIsPaused) {
                mDisplayCurrentSongView = true;
                mCurrentSongPlayingView.setVisibility(View.VISIBLE);

                if ((mAudioService.mPlayingSong != null && mAudioService.mPlayingSong.getID() == ((MusicDetailFragment) mDashboardDetailFragment).mSelectedTrack.getID()))
                    updateCurrentSongNotificationUI(((MusicDetailFragment) mDashboardDetailFragment).mSelectedTrack);
            }
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_UPDATE_PROGRESS_BAR_THREAD) == 0) {
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.SONG_COMPLETE_BROADCAST_ACTION)) {
                Track newTrack = (Track) intent.getExtras().getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
                if (mDashboardDetailFragment.isVisible()) {                                                     //if they are on the dashboard detail screen update the detail widgets
                    (((MusicDetailFragment) mDashboardDetailFragment)).updateTrackUI(newTrack);
                } else {
                    mCurrentSong = newTrack;
                    updateCurrentSongNotificationUI(newTrack);
                }
            } else if (intent.getAction().equals(Constants.SONG_LOADING_BROADCAST_ACTION)) {
                if (mDashboardDetailFragment.isVisible()) {
                    ((MusicDetailFragment) mDashboardDetailFragment).showLoadingMusicDialog();
                }
            }
        }
    };
}