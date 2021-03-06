package com.brainbeats.architecture;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brainbeats.InfoActivity;
import com.brainbeats.LibraryActivity;
import com.brainbeats.LoginActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.MusicAnalyticsActivity;
import com.brainbeats.R;
import com.brainbeats.SettingsActivity;
import com.brainbeats.SocialActivity;
import com.brainbeats.entity.Track;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.Mix;
import com.brainbeats.service.AudioService;
import com.brainbeats.utils.BrainBeatsGlideModule;
import com.brainbeats.utils.Constants;
import com.brainbeats.utils.GlideApp;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static com.brainbeats.utils.Constants.KEY_EXTRA_SELECTED_TRACK;

/*
 * Created by Douglas on 4/21/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public static final String ACCOUNT_TYPE = "com.example.android.datasync";
    public static final String ACCOUNT = "dummyaccount";

    public Mix mCurrentSong;
    public static boolean mDisplayCurrentSongView = false;

    public DrawerLayout mNavigationDrawer;
    public Toolbar mToolBar;
    public ActionBarDrawerToggle mDrawerToggle;

    public LinearLayout mCurrentSongPlayingView;
    public TextView mCurrentSongTitle;
    public TextView mCurrentSongArtistName;
    public ImageView mAlbumThumbnail;
    public NavigationView mNavView;
    public Account mAccount;
    public ImageView mArtistCoverImage;
    public FloatingActionButton mMainActionFab;

    //Audio members for base activity.
    public Thread mUpdateSeekBar;
    private SeekBar mPlayTrackSeekBar;
    int mProgressStatus = 0;
    private volatile boolean mIsAlive = false;

    private IntentFilter mIntentFilter;

    //Audio com.brainbeats.service members
    public AudioService mAudioService;
    public boolean mBound = false;

    public FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccount = CreateSyncAccount(this);

        // Bind to the com.brainbeats.service
        Bundle intentBundle = getIntent().getExtras(); //If an intent is passed to main activity.
        if (intentBundle != null) {
            if (intentBundle.get(KEY_EXTRA_SELECTED_TRACK) != null) {
                mCurrentSong = (Mix) intentBundle.get(KEY_EXTRA_SELECTED_TRACK);
            }
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String emailName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users").child(emailName);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BrainBeatsUser currentUser = dataSnapshot.getValue(BrainBeatsUser.class);
                ((com.brainbeats.architecture.Application) getApplication()).setUserDetails(currentUser);
                setUserProfileImage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        Intent intent = new Intent(BaseActivity.this, AudioService.class);
        intent.putExtra(KEY_EXTRA_SELECTED_TRACK, mCurrentSong);
        startService(intent);
        BaseActivity.this.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.SONG_COMPLETE_BROADCAST_ACTION);
        mIntentFilter.addAction(Constants.PLAYLIST_COMPLETE_BROADCAST_ACTION);
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(KEY_EXTRA_SELECTED_TRACK, mCurrentSong);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        //AccountManager.getInstance(this).setRestorePlayingFromService(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mAudioService != null)
            if(mAudioService.getIsPlaying()) {
                mCurrentSong = mAudioService.getPlayingSong();
                updateCurrentSongNotificationUI(mAudioService.getPlayingSong());
            }

/*        if (mDisplayCurrentSongView)
            showCurrentSongView();
        else
            hideCurrentSongView();*/

        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNavDrawer();

        //mCurrentSongPlayingView = findViewById(R.id.current_track_container);
        mCurrentSongTitle = findViewById(R.id.playing_mix_title);
        mCurrentSongArtistName = findViewById(R.id.playing_mix_artist);
        mAlbumThumbnail = findViewById(R.id.album_thumbnail);
        mPlayTrackSeekBar = findViewById(R.id.playing_mix_seek_bar);
        mMainActionFab = findViewById(R.id.main_action_fob);

        if(mCurrentSong != null)
            updateCurrentSongNotificationUI(mCurrentSong);

/*        mCurrentSongPlayingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dashboardIntent = new Intent(BaseActivity.this, MainActivity.class);
                dashboardIntent.putExtra(Constants.KEY_EXTRA_SELECTED_MIX, mCurrentSong);
                dashboardIntent.putExtra(Constants.KEY_EXTRA_SELECTED_USER, new BrainBeatsUser());
                dashboardIntent.setAction(Constants.INTENT_ACTION_GO_TO_DETAIL_FRAGMENT);
                startActivity(dashboardIntent);
            }
        });*/
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
        mNavigationDrawer = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.navView);
        mArtistCoverImage = mNavView.getHeaderView(0).findViewById(R.id.profile_cover_image);
        mNavView.getMenu().findItem(R.id.action_browse).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.theme_primary_color), PorterDuff.Mode.SRC_IN);
        mNavView.getMenu().findItem(R.id.action_library).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.theme_primary_color), PorterDuff.Mode.SRC_IN);
        mNavView.getMenu().findItem(R.id.action_mixer).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.theme_primary_color), PorterDuff.Mode.SRC_IN);
        //mNavView.getMenu().findItem(R.id.action_social).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.theme_primary_color), PorterDuff.Mode.SRC_IN);
        mNavView.getMenu().findItem(R.id.action_settings).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        getToolBar();
        mDrawerToggle = new ActionBarDrawerToggle(this, mNavigationDrawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mNavigationDrawer.addDrawerListener(mDrawerToggle);
        mNavView.setNavigationItemSelectedListener(item -> {
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
/*                case R.id.action_social:
                    Intent socialIntent = new Intent(getApplicationContext(), SocialActivity.class);
                    socialIntent.putExtra(KEY_EXTRA_SELECTED_TRACK, mCurrentSong);
                    socialIntent.setAction(Constants.INTENT_ACTION_DISPLAY_CURRENT_TRACK);
                    createBackStack(socialIntent);
                    break;*/
/*                case R.id.action_analytics:
                    Intent analyticsIntent = new Intent(getApplicationContext(), MusicAnalyticsActivity.class);
                    analyticsIntent.putExtra(KEY_EXTRA_SELECTED_TRACK, mCurrentSong);
                    analyticsIntent.setAction(Constants.INTENT_ACTION_DISPLAY_CURRENT_TRACK);
                    createBackStack(analyticsIntent);
                    break;*/
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
                case R.id.action_logout:
                    Constants.buildActionDialog(this, "Logout", "Are you sure you want to logout ?", "confirm", new Constants.ConfirmDialogActionListener() {
                        @Override
                        public void PerformDialogAction() {
                            AccountManager.getInstance(getApplicationContext()).forceLogout(getApplicationContext());
                            FirebaseAuth.getInstance().signOut();
                            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(loginIntent);
                            finish();
                        }
                    });
                    break;
                default:
                    return false;
            }
            return true;
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
        startActivity(backStackIntent);
        finish();
    }

    public Toolbar getToolBar() {
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        if (mToolBar != null) {
            mToolBar.setNavigationIcon(R.drawable.ic_navigation_drawer);
            mToolBar.setNavigationOnClickListener(view -> mNavigationDrawer.openDrawer(GravityCompat.START));
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
        if (fm.getBackStackEntryCount() > 1) { //if there are active com.brainbeats.fragments go up if not go back
            fm.popBackStackImmediate();
            if (fm.getBackStackEntryCount() == 1) {
                getToolBar();
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                mDrawerToggle.syncState();
            }
        } else {
            currentActivity.finish();
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

    public void updateCurrentSongNotificationUI(Mix mix) {
/*        if (mDisplayCurrentSongView) {
            mCurrentSongTitle.setText(mix.getMixTitle());
            Picasso.with(BaseActivity.this).load(mix.getMixAlbumCoverArt()).into(mAlbumThumbnail);

            if(mix.getUser() != null)
                mCurrentSongArtistName.setText(mix.getUser().getArtistName());

            startProgressBarThread();
        }*/
    }

    public void showMainFAB(){
        mMainActionFab.setVisibility(View.VISIBLE);
        mMainActionFab.setClickable(true);
    }

    public void hideMainFAB(){
        mMainActionFab.setVisibility(View.INVISIBLE);
        mMainActionFab.setClickable(false);
    }

    public void showCurrentSongView(){
        mDisplayCurrentSongView = true;
        mCurrentSongPlayingView.setVisibility(View.VISIBLE);
    }

/*    public void hideCurrentSongView(){
        mDisplayCurrentSongView = false;
        mCurrentSongPlayingView.setVisibility(View.INVISIBLE);
    }*/

    public void setUserProfileImage(){
        try {
            BrainBeatsUser user = ((Application) this.getApplication()).getUserDetails();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference imageStorage = storageReference.child("images/" + user.getArtistProfileImage());

            GlideApp.with(this)
                    .load(imageStorage)
                    .into(mArtistCoverImage);

        } catch(Exception ex) {
            ex.printStackTrace();
        }
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

        mUpdateSeekBar = new Thread(() -> {
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
                } catch (Exception ex) {
                    Log.i("Progress bar thread", "Exception occured" + ex.toString());
                }
            }
        });
        mUpdateSeekBar.start();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.SONG_COMPLETE_BROADCAST_ACTION)) {
/*                Mix newTrack = intent.getExtras().getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
                if (newTrack != null) {
                    mCurrentSongTitle.setText(newTrack.getMixTitle());

                    if (newTrack.getMixAlbumCoverArt() == null)
                        mAlbumThumbnail.setImageResource(R.drawable.no_cover);
                    else
                        Picasso.with(BaseActivity.this).load(newTrack.getMixAlbumCoverArt()).into(mAlbumThumbnail);

                    if(newTrack.getUser() != null)
                        mCurrentSongArtistName.setText(newTrack.getUser().getArtistName());

                    mCurrentSong = newTrack;
                }*/

/*                showCurrentSongView();
                startProgressBarThread();*/
            }/* else if (intent.getAction().equals(Constants.PLAYLIST_COMPLETE_BROADCAST_ACTION)) {
                hideCurrentSongView();
            }*/
        }
    };
}