package com.brainbeats.architecture;

import android.accounts.Account;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brainbeats.InfoActivity;
import com.brainbeats.LibraryActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;
import com.brainbeats.SettingsActivity;
import com.brainbeats.SocialActivity;
import com.brainbeats.fragments.MusicDetailFragment;
import com.squareup.picasso.Picasso;

import com.brainbeats.entity.Track;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.Mix;
import com.brainbeats.service.AudioService;
import com.brainbeats.utils.Constants;

import static com.brainbeats.utils.Constants.KEY_EXTRA_SELECTED_TRACK;

/**
 * Created by Douglas on 4/21/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public static final String ACCOUNT_TYPE = "com.example.android.datasync";
    public static final String ACCOUNT = "dummyaccount";

    public Track mCurrentSong;
    public static boolean mDisplayCurrentSongView = false;

    public DrawerLayout mNavigationDrawer;
    public Toolbar mToolBar;
    public ActionBarDrawerToggle mDrawerToggle;

    public RelativeLayout mCurrentSongPlayingView;
    public TextView mCurrentSongTitle;
    public TextView mCurrentSongArtistName;
    public ImageView mAlbumThumbnail;
    public NavigationView mNavView;
    public Account mAccount;
    public FloatingActionButton mMainActionFab;

    //Audio members for base activity.
    public Thread mUpdateSeekBar;
    private SeekBar mPlayTrackSeekBar;
    int mProgressStatus = 0;
    private volatile boolean mIsAlive = false;


    //Audio com.brainbeats.service members
    public AudioService mAudioService;
    public boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccount = CreateSyncAccount(this);

        // Bind to the com.brainbeats.service
        Bundle intentBundle = getIntent().getExtras(); //If an intent is passed to main activity.
        if (intentBundle != null) {
            if (intentBundle.get(KEY_EXTRA_SELECTED_TRACK) != null) {
                mCurrentSong = (Track) intentBundle.get(KEY_EXTRA_SELECTED_TRACK);
            }
        }

        Intent intent = new Intent(BaseActivity.this, AudioService.class);
        intent.putExtra(KEY_EXTRA_SELECTED_TRACK, mCurrentSong);
        BaseActivity.this.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the com.brainbeats.service
        if (mBound) {
            BaseActivity.this.unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_EXTRA_SELECTED_TRACK, mCurrentSong);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDisplayCurrentSongView)
            mCurrentSongPlayingView.setVisibility(View.VISIBLE);
        else
            mCurrentSongPlayingView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNavDrawer();

        //Current song com.brainbeats.ui  components
        mCurrentSongPlayingView = (RelativeLayout) findViewById(R.id.current_track_container);
        mCurrentSongTitle = (TextView) findViewById(R.id.playing_mix_title);
        mCurrentSongArtistName = (TextView) findViewById(R.id.playing_mix_artist);
        mAlbumThumbnail = (ImageView) findViewById(R.id.album_thumbnail);
        mPlayTrackSeekBar = (SeekBar) findViewById(R.id.playing_mix_seek_bar);
        mMainActionFab = (FloatingActionButton) findViewById(R.id.main_action_fob);

        updateCurrentSongNotificationUI(mCurrentSong);

        mCurrentSongPlayingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dashboardIntent = new Intent(BaseActivity.this, MainActivity.class);
                dashboardIntent.putExtra(Constants.KEY_EXTRA_SELECTED_MIX, new Mix(mCurrentSong));
                dashboardIntent.putExtra(Constants.KEY_EXTRA_SELECTED_USER, new BrainBeatsUser(mCurrentSong.getUser()));
                dashboardIntent.setAction(Constants.INTENT_ACTION_GO_TO_DETAIL_FRAGMENT);
                startActivity(dashboardIntent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void setUpNavDrawer() {
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) findViewById(R.id.navView);
        getToolBar();
        mDrawerToggle = new ActionBarDrawerToggle(this, mNavigationDrawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mNavigationDrawer.addDrawerListener(mDrawerToggle);
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_browse:
                        Intent browseIntent = new Intent(getApplicationContext(), MainActivity.class);
                        browseIntent.putExtra(KEY_EXTRA_SELECTED_TRACK, mCurrentSong);
                        browseIntent.setAction(Constants.INTENT_ACTION_DISPLAY_CURRENT_TRACK);
                        createBackStack(browseIntent);
                        break;
                    case R.id.action_library:
                        Intent libraryIntent = new Intent(getApplicationContext(), LibraryActivity.class);
                        libraryIntent.putExtra(KEY_EXTRA_SELECTED_TRACK, mCurrentSong);
                        libraryIntent.setAction(Constants.INTENT_ACTION_DISPLAY_CURRENT_TRACK);
                        createBackStack(libraryIntent);
                        break;
                    case R.id.action_mixer:
                        Intent mixerIntent = new Intent(getApplicationContext(), MixerActivity.class);
                        mixerIntent.putExtra(KEY_EXTRA_SELECTED_TRACK, mCurrentSong);
                        mixerIntent.setAction(Constants.INTENT_ACTION_DISPLAY_CURRENT_TRACK);
                        createBackStack(mixerIntent);
                        break;
                    case R.id.action_social:
                        Intent socialIntent = new Intent(getApplicationContext(), SocialActivity.class);
                        socialIntent.putExtra(KEY_EXTRA_SELECTED_TRACK, mCurrentSong);
                        socialIntent.setAction(Constants.INTENT_ACTION_DISPLAY_CURRENT_TRACK);
                        createBackStack(socialIntent);
                        break;
                    case R.id.action_settings:
                        Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                        settingsIntent.putExtra(KEY_EXTRA_SELECTED_TRACK, mCurrentSong);
                        settingsIntent.setAction(Constants.INTENT_ACTION_DISPLAY_CURRENT_TRACK);
                        createBackStack(settingsIntent);
                        break;
                    case R.id.action_info:
                        Intent infoIntent = new Intent(getApplicationContext(), InfoActivity.class);
                        infoIntent.putExtra(KEY_EXTRA_SELECTED_TRACK, mCurrentSong);
                        infoIntent.setAction(Constants.INTENT_ACTION_DISPLAY_CURRENT_TRACK);
                        createBackStack(infoIntent);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        mNavigationDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragmentTag);
        if (fm.getFragments() != null) {
            fragmentTransaction.addToBackStack(fragmentTag);
        }
        fragmentTransaction.commit();
    }

    public void createBackStack(Intent backStackIntent) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder taskSstackBuilder = TaskStackBuilder.create(this);
            taskSstackBuilder.addNextIntentWithParentStack(backStackIntent);
            taskSstackBuilder.startActivities();
        } else {
            startActivity(backStackIntent);
            finish();
        }
    }

    public Toolbar getToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        if (mToolBar != null) {
            mToolBar.setNavigationIcon(R.drawable.ic_navigation_drawer);
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigationDrawer.openDrawer(GravityCompat.START);
                }
            });
        }
        return mToolBar;
    }

    protected boolean isNavDrawerOpen() {
        return mNavigationDrawer != null && mNavigationDrawer.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mNavigationDrawer != null) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        }
    }

    public void toggleNavDrawerIcon() {
        if (mDrawerToggle != null) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mDrawerToggle.syncState();
        }
    }

    public void navigateUpOrBack(Activity currentActivity, FragmentManager fm) {
        if (fm.getBackStackEntryCount() >= 1) { //if there are active com.brainbeats.fragments go up if not go back
            fm.popBackStackImmediate();
            if (fm.getBackStackEntryCount() == 0) {
                getToolBar();
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                mDrawerToggle.syncState();
            }
        } else {
            currentActivity.onBackPressed();
        }
    }

    public static Account CreateSyncAccount(Context context) {
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        android.accounts.AccountManager accountManager = (android.accounts.AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            return newAccount;
        } else {
            return accountManager.getAccountsByType(ACCOUNT_TYPE)[0];
        }
    }

    public boolean isAudioServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            AudioService.AudioBinder binder = (AudioService.AudioBinder) service;
            mAudioService = binder.getService();
            mBound = true;
            mIsAlive = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public void updateCurrentSongNotificationUI(Track track) {
        if (mDisplayCurrentSongView) {
            mCurrentSongTitle.setText(track.getTitle());
            Picasso.with(BaseActivity.this).load(track.getArtworkURL()).into(mAlbumThumbnail);
            mCurrentSongArtistName.setText(track.getUser().getUsername());
            startProgressBarThread();
        }
    }

    public void hideMainFAB(){
        mMainActionFab.setVisibility(View.INVISIBLE);
        mMainActionFab.setClickable(false);
    }

    public void startProgressBarThread() {
        int trackDuration = mCurrentSong.getDuration();
        mPlayTrackSeekBar.setMax(trackDuration);
        mPlayTrackSeekBar.setIndeterminate(false);
        mIsAlive = true;

        if(mBound && mAudioService.getPlayerPosition() != 0){ // if already playing set to current player position before thread runs
            mProgressStatus = mAudioService.getPlayerPosition();
            mPlayTrackSeekBar.setProgress(mProgressStatus);
        }

        mUpdateSeekBar = new Thread(new Runnable() {
            @Override
            public void run() {
                while ((mProgressStatus < trackDuration)  && mIsAlive) {
                    try {
                        Thread.sleep(1000); //Update once per second
                        mProgressStatus = mAudioService.getPlayerPosition();
                        mPlayTrackSeekBar.setProgress(mProgressStatus);
                        mPlayTrackSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    mAudioService.seekPlayerTo(progress);
                                }
                            }
                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {}
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {}
                        });

                    } catch (InterruptedException e) {
                        mIsAlive = false;
                        Log.i("Progress bar thread", "Exception occured" + e.toString());
                    }
                }
            }
        });
        mUpdateSeekBar.start();
    }
}