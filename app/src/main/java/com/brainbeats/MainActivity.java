package com.brainbeats;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.brainbeats.architecture.AccountManager;
import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.entity.Track;
import com.brainbeats.fragments.BrowseMusicFragment;
import com.brainbeats.fragments.MusicDetailFragment;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.Mix;
import com.brainbeats.model.Playlist;
import com.brainbeats.sync.SyncManager;
import com.brainbeats.utils.Constants;
import com.brainbeats.web.WebApiManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, BrowseMusicFragment.OnFragmentInteractionListener, MusicDetailFragment.OnFragmentInteractionListener {

    //Data
    private DatabaseReference mDatabase;

    public Fragment mDashboardFragment;
    public Fragment mDashboardDetailFragment;
    public CoordinatorLayout mCoordinatorLayout;
    private IntentFilter mIntentFilter;

    public FloatingActionButton mMainActionFab;
    public FloatingActionButton mExtraActionOneFab;
    public FloatingActionButton mExtraActionTwoFab;
    public FloatingActionButton mExtraActionThreeFab;
    public FloatingActionButton mExtraActionFourFab;

    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    public boolean mIsFabOpen;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mCoordinatorLayout = findViewById(R.id.main_content_coordinator_layout);
        mMainActionFab = findViewById(R.id.main_action_fob);
        mExtraActionOneFab = findViewById(R.id.action_one_fob);
        mExtraActionTwoFab = findViewById(R.id.action_two_fob);
/*        mExtraActionThreeFab = findViewById(R.id.action_three_fob);
        mExtraActionFourFab = findViewById(R.id.action_four_fob);*/

        fab_open = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_backward);

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
                        switchToBeatDetailFragment(sentMix);
                    }
                }
            }
        }

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.SONG_COMPLETE_BROADCAST_ACTION);
        mIntentFilter.addAction(Constants.SONG_LOADING_BROADCAST_ACTION);
        mIntentFilter.addAction(Constants.SONG_ERROR_BROADCAST_ACTION);
        //mIntentFilter.addAction(Constants.RESTORE_FROM_SERVICE_BROADCAST_ACTION);


        mMainActionFab.setOnClickListener(this);
        mExtraActionOneFab.setOnClickListener(this);
        mExtraActionTwoFab.setOnClickListener(this);
/*        mExtraActionThreeFab.setOnClickListener(this);
        mExtraActionFourFab.setOnClickListener(this);*/
    }

    @Override
    public void onBackPressed() {
        if (mIsFabOpen)
            animateFAB();

        mMainActionFab.setImageDrawable(getDrawable(R.drawable.ic_filter_list_white));
        mExtraActionOneFab.setImageDrawable(getDrawable(R.drawable.ic_whatshot_white));
        mExtraActionTwoFab.setImageDrawable(getDrawable(R.drawable.ic_access_time_white));
/*        mExtraActionThreeFab.setImageDrawable(getDrawable(R.drawable.ic_sort_by_alpha_white));
        mExtraActionFourFab.setImageDrawable(getDrawable(R.drawable.ic_m));*/


        FragmentManager fm = getSupportFragmentManager();
        navigateUpOrBack(this, fm);
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

        if(mAudioService != null)
            if(mAudioService.getIsPlaying()) {
                if (mDashboardDetailFragment.isVisible()) {
                    (((MusicDetailFragment) mDashboardDetailFragment)).updateTrackUI(mAudioService.getPlayingSong());
                }
            }

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
                if (mDashboardFragment.isVisible())
                    ((BrowseMusicFragment) mDashboardFragment).getTracks("","",WebApiManager.SOUND_CLOUD_QUERY_FILTER_PARAM_POPULAR,"");
                else if (mDashboardDetailFragment.isVisible())
                    addMixToLibrary();
                break;
            case R.id.action_two_fob:
                animateFAB();
                if (mDashboardFragment.isVisible())
                    ((BrowseMusicFragment) mDashboardFragment).getTracks("","",WebApiManager.SOUND_CLOUD_QUERY_FILTER_PARAM_RECENT,"Recent");
                else if (mDashboardDetailFragment.isVisible())
                   addMixToFavorites();
                break;
/*            case R.id.action_three_fob:
                animateFAB();
                if (mDashboardFragment.isVisible())
                    ((BrowseMusicFragment) mDashboardFragment).getTracks("","",WebApiManager.SOUND_CLOUD_QUERY_FILTER_PARAM_A_TO_Z, "Alphabet");
                else if (mDashboardDetailFragment.isVisible())
                    showAddToPlaylist();
                break;
            case R.id.action_four_fob:
                animateFAB();
                //load mistro
                break;*/
        }
    }

    public void addMixToLibrary(){
        DatabaseReference mixRef = mDatabase.child("artist_mix/" + FirebaseAuth.getInstance().getCurrentUser().getUid()); //save this mix under mixes --> userUid -- new song
        mixRef.push().setValue(mCurrentSong);

        Snackbar mixAddedSnack;
        mixAddedSnack = Snackbar.make(mCoordinatorLayout, getString(R.string.song_added_to_library_snack_message), Snackbar.LENGTH_LONG);
        mixAddedSnack.show();
    }

    public void addMixToFavorites(){
        DatabaseReference mixRef = mDatabase.child("mixes_favorites/" + FirebaseAuth.getInstance().getCurrentUser().getUid()); //save this mix under mixes --> userUid -- new song
        mixRef.push().setValue(mCurrentSong);

        Snackbar mixAddedSnack;
        mixAddedSnack = Snackbar.make(mCoordinatorLayout, getString(R.string.song_added_to_favorites_snack_message), Snackbar.LENGTH_LONG);
        mixAddedSnack.show();

    }

    public void showAddToPlaylist() {
        ArrayList<String> playlistNames = new ArrayList<String>();
        playlistNames.add("Create New Playlist");

        DatabaseReference playlistRef = mDatabase.child("playlists/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Playlist playlist = data.getValue(Playlist.class);
                    playlistNames.add(playlist.getPlaylistTitle());
                }

                String[] mOptions = new String[playlistNames.size()];
                mOptions = playlistNames.toArray(mOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_list_dialog_layout, null);
                ((TextView) dialogView.findViewById(R.id.separator_title)).setText("");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.dialog_list_item, R.id.dialog_item, mOptions);
                ((ListView) dialogView.findViewById(R.id.option_list_view)).setAdapter(adapter);
                ((ListView) dialogView.findViewById(R.id.option_list_view)).setOnItemClickListener((parent, view, position, id) -> {
                    if (position == 0)
                        showEnterPlaylistTitleDialog();
                    else {
                        TextView titleView = view.findViewById(R.id.dialog_item);
                        String title = titleView.getText().toString();
                        addToExistingPlaylist(title);
                    }
                });
                builder.setView(dialogView);
                alert = builder.create();
                alert.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void showEnterPlaylistTitleDialog() {
        alert.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_playlist_dialog, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText("");
        EditText editText = dialogView.findViewById(R.id.playlist_title);
        builder.setView(dialogView);
        builder.setPositiveButton("Create", (dialog, which) -> {
            String playlistTitle = editText.getText().toString();
            createNewPlaylist(playlistTitle);
            dialog.dismiss();
        });
        alert = builder.create();
        alert.show();
    }

    public void createNewPlaylist(String title) {
        if (!title.isEmpty()) { //require title
            Playlist playList = new Playlist();
            playList.setPlaylistTitle(title);

            DatabaseReference playlistRef = mDatabase.child("playlists/" + FirebaseAuth.getInstance().getCurrentUser().getUid()); //save this mix under mixes --> userUid -- new song
            playlistRef.push().setValue(playList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Snackbar newPlayListAddedSnack;
                        newPlayListAddedSnack = Snackbar.make(mCoordinatorLayout, getString(R.string.new_playlist_added), Snackbar.LENGTH_LONG);
                        newPlayListAddedSnack.show();

                        addToExistingPlaylist(title);

                    } else {
                        Snackbar errorSnack;
                        errorSnack = Snackbar.make(mCoordinatorLayout, getString(R.string.error_processing_request), Snackbar.LENGTH_LONG);
                        errorSnack.show();
                    }
                }
            });
        } else {
            Constants.buildInfoDialog(this, "", "Please enter a title");
        }
    }

    public void addToExistingPlaylist(String title) {
        Query playlistQuery = mDatabase.child("playlists/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("playlistTitle").equalTo(title);
        playlistQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Playlist playlist = childSnapshot.getValue(Playlist.class);
                    ArrayList<Mix> mixes = new ArrayList<Mix>();
                    if (playlist.getMixes() != null) {
                        mixes = playlist.getMixes();
                    }
                    mixes.add(((MusicDetailFragment)mDashboardDetailFragment).mSelectedTrack);
                    playlist.setMixes(mixes);

                    DatabaseReference playlistRef = mDatabase.child("playlists/" + FirebaseAuth.getInstance().getCurrentUser().getUid()); //save this mix under mixes --> userUid -- new song
                    playlistRef.push().setValue(playlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar newPlayListAddedSnack;
                                newPlayListAddedSnack = Snackbar.make(mCoordinatorLayout, getString(R.string.song_added_to_existing_playlist), Snackbar.LENGTH_LONG);
                                newPlayListAddedSnack.show();
                            } else {
                                Snackbar errorSnack;
                                errorSnack = Snackbar.make(mCoordinatorLayout, getString(R.string.error_processing_request), Snackbar.LENGTH_LONG);
                                errorSnack.show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        alert.dismiss();
    }

    public void switchToDashboardFragment() {
        replaceFragment(mDashboardFragment, mDashboardFragment.getTag());
    }

    public void switchToBeatDetailFragment(Mix mix) {
        toggleNavDrawerIcon();
        mDashboardDetailFragment = MusicDetailFragment.newInstance(mix);
        mCurrentSong = mix; //set the clicked song to the current playing

        mMainActionFab.setImageDrawable(getDrawable(R.drawable.ic_more_vert_white));
        mExtraActionOneFab.setImageDrawable(getDrawable(R.drawable.ic_library_add_white));
        mExtraActionTwoFab.setImageDrawable(getDrawable(R.drawable.ic_favorite_white));
/*        mExtraActionThreeFab.setImageDrawable(getDrawable(R.drawable.ic_playlist_add_white));
        mExtraActionFourFab.setImageDrawable(getDrawable(R.drawable.ic_m));*/

        replaceFragment(mDashboardDetailFragment, "DashboardDetailFrag");
    }

    public void animateFAB() {
        if (mIsFabOpen) {
            mMainActionFab.startAnimation(rotate_backward);
            mExtraActionOneFab.startAnimation(fab_close);
            mExtraActionTwoFab.startAnimation(fab_close);
            mExtraActionOneFab.setClickable(false);
            mExtraActionTwoFab.setClickable(false);
/*            mExtraActionThreeFab.startAnimation(fab_close);
            mExtraActionThreeFab.setClickable(false);
            mExtraActionFourFab.startAnimation(fab_close);
            mExtraActionFourFab.setClickable(false);*/

            mIsFabOpen = false;
        } else {
            mMainActionFab.startAnimation(rotate_forward);
            mExtraActionOneFab.startAnimation(fab_open);
            mExtraActionTwoFab.startAnimation(fab_open);
            mExtraActionOneFab.setClickable(true);
            mExtraActionTwoFab.setClickable(true);
/*            mExtraActionThreeFab.startAnimation(fab_open);
            mExtraActionThreeFab.setClickable(true);
            mExtraActionFourFab.startAnimation(fab_open);
            mExtraActionFourFab.setClickable(true);*/

            mIsFabOpen = true;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.compareTo(Constants.DASHBOARD_DETAIL_PLAY_SONG_URI) == 0) {
            mAudioService.resumeSong();
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_PAUSE_SONG_URI) == 0) {
            mAudioService.pauseSong();
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_SET_SONG_REPEAT_URI) == 0) {
            mAudioService.setSongLooping(true);
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_UPDATE_PROGRESS_BAR_THREAD) == 0) {
            if (mAudioService != null)
                if (mAudioService.getIsPlaying() || mAudioService.getIsPaused())
                    ((MusicDetailFragment) mDashboardDetailFragment).startProgressBarThread(mAudioService.getPlayerPosition());
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_CHECK_IF_CURRENT_SONG) == 0) {
            if (mAudioService != null)
                if (mAudioService.getIsPlaying() || mAudioService.getIsPaused())
                    ((MusicDetailFragment) mDashboardDetailFragment).isCurrentSong = (mAudioService.getPlayingSong().getMixId().equals(((MusicDetailFragment) mDashboardDetailFragment).mSelectedTrack.getArtistId()));
        } else if(uri.compareTo(Constants.LOGOUT_URI) == 0) {
            mAudioService.stopSong();
            mFirebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri, Mix mix) {
        if (uri.compareTo(Constants.DASHBOARD_DETAIL_LOAD_SONG_URI) == 0) {
            mAudioService.requestAudioFocus(this);
            mAudioService.setPlayingSong(mix);
            //mAudioService.playSong(Uri.parse(mix.getStreamURL()));

            String storageUrl = mix.getFirebaseStorageUrl();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference mixStorage = storageReference.child("mixes/" + storageUrl);

            try {
                final long ONE_MEGABYTE = 1024 * 1024;
                mixStorage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        try{
                            File outputFile = File.createTempFile(mix.getMixTitle(), ".3gp", MainActivity.this.getCacheDir());
                            outputFile.deleteOnExit();
                            FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
                            fileoutputstream.write(bytes);
                            fileoutputstream.close();

                            mCurrentSong = mix;
                            mAudioService.setPlayingSong(mix);
                            mAudioService.playBrainBeatsSong(Uri.parse(outputFile.getPath()));

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        exception.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_UPDATE_CURRENT_PLAYING_SONG_VIEW) == 0) {
            if (mAudioService.getIsPlaying() || mAudioService.getIsPaused()) {
                mDisplayCurrentSongView = true;
                mCurrentSongPlayingView.setVisibility(View.VISIBLE);

                if (mAudioService.getPlayingSong().getMixId().equals(mix.getMixId()))
                    updateCurrentSongNotificationUI(mix);
            }
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_SKIP_FORWARD_URI) == 0) {
            if (mAudioService.getPlayingSong() == null)
                mAudioService.setPlayingSong(mix);

            mAudioService.loadNextTrack();
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_DOWNVOTE_SONG_URI) == 0) {
            if (mAudioService.getPlayingSong() == null)
                mAudioService.setPlayingSong(mix);

            long mixId = 0;
            Cursor mixCursor = getContentResolver().query(BrainBeatsContract.MixEntry.CONTENT_URI, null, "mixtitle = ?", new String[]{mix.getMixTitle()}, null);
            if (mixCursor != null) {
                if (mixCursor.getCount() != 0) {
                    mixCursor.moveToFirst();
                    Mix updateMix = Constants.buildMixFromCursor(this, mixCursor, 0);
                    updateMix.setIsDownvoted(1);

                    getContentResolver().update(
                            BrainBeatsContract.MixEntry.CONTENT_URI,
                            Constants.buildMixRecord(updateMix),
                            "_Id = ?",
                            new String[]{String.valueOf(updateMix.getMixId())});

                    mixCursor.close();
                } else {
                    ContentValues values = Constants.buildMixRecord(mix);
                    values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_DOWNVOTE, 1);
                    Uri returnRecord = getContentResolver().insert(BrainBeatsContract.MixEntry.CONTENT_URI, values);
                }
            }

            mAudioService.loadNextTrack();
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.SONG_COMPLETE_BROADCAST_ACTION)) {

                Mix mix = intent.getExtras().getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
                if (mDashboardDetailFragment.isVisible()) {                                                             //if they are on the dashboard detail screen update the detail widgets
                    (((MusicDetailFragment) mDashboardDetailFragment)).updateTrackUI(mix);
                    //hideCurrentSongView();
                }
            } else if (intent.getAction().equals(Constants.SONG_LOADING_BROADCAST_ACTION)) {
                if (mDashboardDetailFragment.isVisible()) {
                    ((MusicDetailFragment) mDashboardDetailFragment).showLoadingMusicDialog();
                }
            } else if (intent.getAction().equals(Constants.SONG_ERROR_BROADCAST_ACTION)) {
                if (mDashboardDetailFragment.isVisible()) {
                    ((MusicDetailFragment) mDashboardDetailFragment).loadingMusicDialog.dismiss();
                }
            } else if (intent.getAction().equals(Constants.PLAYLIST_COMPLETE_BROADCAST_ACTION)) {
               // hideCurrentSongView();
            } else if (intent.getAction().equals(Constants.RESTORE_FROM_SERVICE_BROADCAST_ACTION)) {
                Mix mix = intent.getExtras().getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
                if (mDashboardDetailFragment.isVisible()) {                                                             //if they are on the dashboard detail screen update the detail widgets
                    (((MusicDetailFragment) mDashboardDetailFragment)).updateTrackUI(mix);
                }
            }
        }
    };
}