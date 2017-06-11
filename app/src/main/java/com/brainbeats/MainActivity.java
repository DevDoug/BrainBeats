package com.brainbeats;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
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
import com.brainbeats.model.MixPlaylist;
import com.brainbeats.model.Playlist;
import com.brainbeats.sync.SyncManager;
import com.brainbeats.utils.Constants;
import com.brainbeats.web.WebApiManager;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, BrowseMusicFragment.OnFragmentInteractionListener, MusicDetailFragment.OnFragmentInteractionListener {

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

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content_coordinator_layout);
        mMainActionFab = (FloatingActionButton) findViewById(R.id.main_action_fob);
        mExtraActionOneFab = (FloatingActionButton) findViewById(R.id.action_one_fob);
        mExtraActionTwoFab = (FloatingActionButton) findViewById(R.id.action_two_fob);
        mExtraActionThreeFab = (FloatingActionButton) findViewById(R.id.action_three_fob);
        //mExtraActionFourFab = (FloatingActionButton) findViewById(R.id.action_four_fob);

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
        mIntentFilter.addAction(Constants.SONG_ERROR_BROADCAST_ACTION);
        //mIntentFilter.addAction(Constants.RESTORE_FROM_SERVICE_BROADCAST_ACTION);


        mMainActionFab.setOnClickListener(this);
        mExtraActionOneFab.setOnClickListener(this);
        mExtraActionTwoFab.setOnClickListener(this);
        mExtraActionThreeFab.setOnClickListener(this);
        //mExtraActionFourFab.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mIsFabOpen)
            animateFAB();

        mMainActionFab.setImageDrawable(getDrawable(R.drawable.ic_filter_list_white));
        mExtraActionOneFab.setImageDrawable(getDrawable(R.drawable.ic_whatshot_white));
        mExtraActionTwoFab.setImageDrawable(getDrawable(R.drawable.ic_access_time_white));
        mExtraActionThreeFab.setImageDrawable(getDrawable(R.drawable.ic_sort_by_alpha_white));

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
                if (mDashboardDetailFragment.isVisible()) {                                                             //if they are on the dashboard detail screen update the detail widgets
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
                    ((MusicDetailFragment) mDashboardDetailFragment).updateOfflineSyncManager(Constants.SyncDataAction.UpdateMix, null);
                break;
            case R.id.action_two_fob:
                animateFAB();
                if (mDashboardFragment.isVisible())
                    ((BrowseMusicFragment) mDashboardFragment).getTracks("","",WebApiManager.SOUND_CLOUD_QUERY_FILTER_PARAM_RECENT,"Recent");
                else if (mDashboardDetailFragment.isVisible())
                    ((MusicDetailFragment) mDashboardDetailFragment).updateOfflineSyncManager(Constants.SyncDataAction.UpdateFavorite, null);
                break;
            case R.id.action_three_fob:
                animateFAB();
                if (mDashboardFragment.isVisible())
                    ((BrowseMusicFragment) mDashboardFragment).getTracks("","",WebApiManager.SOUND_CLOUD_QUERY_FILTER_PARAM_A_TO_Z, "Alphabet");
                else if (mDashboardDetailFragment.isVisible())
                    showAddToPlaylist();
                break;
/*            case R.id.action_four_fob:
                animateFAB();
                if (mDashboardFragment.isVisible())
                    ((BrowseMusicFragment) mDashboardFragment).getTracks(WebApiManager.SOUND_CLOUD_QUERY_FILTER_PARAM_A_TO_Z);
                else if (mDashboardDetailFragment.isVisible())
                    showAddToPlaylist();
                break;*/
        }
    }

    public void showAddToPlaylist() {
        ArrayList<String> playlistNames = new ArrayList<String>();
        playlistNames.add("Create New Playlist");

        Cursor userPlaylistsCursor = getContentResolver().query(
                BrainBeatsContract.PlaylistEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (userPlaylistsCursor != null) {
            userPlaylistsCursor.moveToFirst();
            for (int i = 0; i < userPlaylistsCursor.getCount(); i++) {
                playlistNames.add(userPlaylistsCursor.getString(userPlaylistsCursor.getColumnIndex(BrainBeatsContract.PlaylistEntry.COLUMN_NAME_PLAYLIST_TITLE)));
                userPlaylistsCursor.moveToNext();
            }

            userPlaylistsCursor.close();
        }

        String[] mOptions = new String[playlistNames.size()];
        mOptions = playlistNames.toArray(mOptions);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = ((Activity) this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_list_dialog_layout, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText("");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dialog_list_item, R.id.dialog_item, mOptions);
        ((ListView) dialogView.findViewById(R.id.option_list_view)).setAdapter(adapter);
        ((ListView) dialogView.findViewById(R.id.option_list_view)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    showEnterPlaylistTitleDialog();
                else {
                    TextView titleView = (TextView) view.findViewById(R.id.dialog_item);
                    String title = titleView.getText().toString();
                    addToExistingPlaylist(title);
                }
            }
        });
        builder.setView(dialogView);
        alert = builder.create();
        alert.show();
    }

    public void showEnterPlaylistTitleDialog() {
        alert.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = ((Activity) this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_playlist_dialog, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText("");
        EditText editText = ((EditText) dialogView.findViewById(R.id.playlist_title));

        builder.setView(dialogView);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playlistTitle = editText.getText().toString();
                createNewPlaylist(playlistTitle);
                dialog.dismiss();
            }
        });
        alert = builder.create();
        alert.show();

    }

    public void createNewPlaylist(String title) {
        if (!title.isEmpty()) { //require title
            Cursor returnRecord = getContentResolver().query(BrainBeatsContract.PlaylistEntry.CONTENT_URI, null, "playlisttitle = ?", new String[]{title}, null);
            if (returnRecord != null) {
                if (returnRecord.getCount() == 0) { //only add a new playlist if it does not already exist

                    Playlist playList = new Playlist();
                    playList.setPlaylistTitle(title);
                    playList.setSoundCloudId(0);
                    Uri returnRow = getContentResolver().insert(BrainBeatsContract.PlaylistEntry.CONTENT_URI, Constants.buildPlaylistRecord(playList));
                    long returnRowId = ContentUris.parseId(returnRow);
                    alert.dismiss();

                    Snackbar newPlayListAddedSnack;
                    newPlayListAddedSnack = Snackbar.make(mCoordinatorLayout, getString(R.string.new_playlist_added), Snackbar.LENGTH_LONG);
                    newPlayListAddedSnack.show();
                } else {
                    Constants.buildInfoDialog(this, "Playlist already exists", "There is already a playlist with that name");
                }
                returnRecord.close();
            }
        } else {
            Constants.buildInfoDialog(this, "", "Please enter a title");
        }
    }

    public void addToExistingPlaylist(String title) {
        alert.dismiss();

        long mixId = 0;

        //find playlist
        Cursor playlistCursor = getContentResolver().query(BrainBeatsContract.PlaylistEntry.CONTENT_URI, null, "playlisttitle = ?", new String[]{title}, null);
        if (playlistCursor != null) {
            playlistCursor.moveToFirst();

            Cursor mixCursor = getContentResolver().query(BrainBeatsContract.MixEntry.CONTENT_URI, null, "mixtitle = ?", new String[]{mCurrentSong.getTitle()}, null);
            if (mixCursor != null) {
                if (mixCursor.getCount() != 0) {
                    mixCursor.moveToFirst();
                    mixId = mixCursor.getLong(mixCursor.getColumnIndex(BrainBeatsContract.MixEntry._ID));
                    mixCursor.close();
                } else {
                    Uri returnRecord = getContentResolver().insert(BrainBeatsContract.MixEntry.CONTENT_URI, Constants.buildMixRecord(Constants.buildMixRecordFromTrack(mCurrentSong)));
                    mixId = ContentUris.parseId(returnRecord);
                }
            }

            MixPlaylist mixPlaylist = new MixPlaylist();
            mixPlaylist.mMixId = mixId;
            mixPlaylist.mPlaylistId = playlistCursor.getLong(playlistCursor.getColumnIndex(BrainBeatsContract.MixPlaylistEntry._ID));

            //determine if this mix is already part of this playlist
            Cursor mixPlaylistCursor = getContentResolver().query(BrainBeatsContract.MixPlaylistEntry.CONTENT_URI, null, "mixid = ?", new String[]{String.valueOf(mixPlaylist.mMixId)}, null);

            if (mixPlaylistCursor != null) {
                if (mixPlaylistCursor.getCount() == 0) {
                    Uri returnRow = getContentResolver().insert(BrainBeatsContract.MixPlaylistEntry.CONTENT_URI, Constants.buildMixPlaylistRecord(mixPlaylist));
                    long returnRowId = ContentUris.parseId(returnRow);
                    alert.dismiss();

                    Snackbar songAddedToExistingPlaylistSnack;
                    songAddedToExistingPlaylistSnack = Snackbar.make(mCoordinatorLayout, getString(R.string.song_added_to_existing_playlist), Snackbar.LENGTH_LONG);
                    songAddedToExistingPlaylistSnack.show();
                } else {
                    Constants.buildInfoDialog(this, "Duplicate Song", "Song is already in this playlist");
                }
                mixPlaylistCursor.close();
            }
            playlistCursor.close();
        }
    }

    public void switchToDashboardFragment() {
        replaceFragment(mDashboardFragment, mDashboardFragment.getTag());
    }

    public void switchToBeatDetailFragment(Track track) {
        toggleNavDrawerIcon();
        mDashboardDetailFragment = MusicDetailFragment.newInstance(track);
        mCurrentSong = track; //set the clicked song to the current playing

        mMainActionFab.setImageDrawable(getDrawable(R.drawable.ic_android_white));
        mExtraActionOneFab.setImageDrawable(getDrawable(R.drawable.ic_library_add_white));
        mExtraActionTwoFab.setImageDrawable(getDrawable(R.drawable.ic_favorite_white));
        mExtraActionThreeFab.setImageDrawable(getDrawable(R.drawable.ic_playlist_add_white));
        //mExtraActionFourFab.setImageDrawable(getDrawable(R.drawable.ic_playlist_add_white));

        replaceFragment(mDashboardDetailFragment, "DashboardDetailFrag");
    }

    public void animateFAB() {
        if (mIsFabOpen) {
            mMainActionFab.startAnimation(rotate_backward);
            mExtraActionOneFab.startAnimation(fab_close);
            mExtraActionTwoFab.startAnimation(fab_close);
            mExtraActionOneFab.setClickable(false);
            mExtraActionTwoFab.setClickable(false);
            mExtraActionThreeFab.startAnimation(fab_close);
            mExtraActionThreeFab.setClickable(false);
/*            mExtraActionFourFab.startAnimation(fab_close);
            mExtraActionFourFab.setClickable(false);*/

            mIsFabOpen = false;
        } else {
            mMainActionFab.startAnimation(rotate_forward);
            mExtraActionOneFab.startAnimation(fab_open);
            mExtraActionTwoFab.startAnimation(fab_open);
            mExtraActionOneFab.setClickable(true);
            mExtraActionTwoFab.setClickable(true);
            mExtraActionThreeFab.startAnimation(fab_open);
            mExtraActionThreeFab.setClickable(true);
/*            mExtraActionFourFab.startAnimation(fab_open);
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
                    ((MusicDetailFragment) mDashboardDetailFragment).isCurrentSong = (mAudioService.getPlayingSong().getID() == ((MusicDetailFragment) mDashboardDetailFragment).mSelectedTrack.getID());
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri, Track track) {
        if (uri.compareTo(Constants.DASHBOARD_DETAIL_LOAD_SONG_URI) == 0) {
            mAudioService.requestAudioFocus(this);
            mAudioService.setPlayingSong(track);
            mAudioService.playSong(Uri.parse(track.getStreamURL()));
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_UPDATE_CURRENT_PLAYING_SONG_VIEW) == 0) {
            if (mAudioService.getIsPlaying() || mAudioService.getIsPaused()) {
                mDisplayCurrentSongView = true;
                mCurrentSongPlayingView.setVisibility(View.VISIBLE);

                if (mAudioService.getPlayingSong().getID() == track.getID())
                    updateCurrentSongNotificationUI(track);
            }
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_SKIP_FORWARD_URI) == 0) {
            if (mAudioService.getPlayingSong() == null)
                mAudioService.setPlayingSong(track);

            mAudioService.loadNextTrack();
        } else if (uri.compareTo(Constants.DASHBOARD_DETAIL_DOWNVOTE_SONG_URI) == 0) {
            if (mAudioService.getPlayingSong() == null)
                mAudioService.setPlayingSong(track);

            long mixId = 0;
            Cursor mixCursor = getContentResolver().query(BrainBeatsContract.MixEntry.CONTENT_URI, null, "mixtitle = ?", new String[]{track.getTitle()}, null);
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
                    ContentValues values = Constants.buildMixRecord(Constants.buildMixRecordFromTrack(track));
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
                Track newTrack = (Track) intent.getExtras().getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
                if (mDashboardDetailFragment.isVisible()) {                                                             //if they are on the dashboard detail screen update the detail widgets
                    (((MusicDetailFragment) mDashboardDetailFragment)).updateTrackUI(newTrack);
                    hideCurrentSongView();
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
                hideCurrentSongView();
            } else if (intent.getAction().equals(Constants.RESTORE_FROM_SERVICE_BROADCAST_ACTION)) {
                Track newTrack = (Track) intent.getExtras().getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
                if (mDashboardDetailFragment.isVisible()) {                                                             //if they are on the dashboard detail screen update the detail widgets
                    (((MusicDetailFragment) mDashboardDetailFragment)).updateTrackUI(newTrack);
                }
            }
        }
    };
}