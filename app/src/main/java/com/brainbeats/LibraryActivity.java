package com.brainbeats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.entity.Track;
import com.brainbeats.fragments.LibraryFragment;

import com.brainbeats.utils.Constants;

public class LibraryActivity extends BaseActivity implements LibraryFragment.OnFragmentInteractionListener {

    Fragment mLibraryFragment;
    String mQueryText;
    SearchView.OnQueryTextListener listener;
    SearchView mSearchView;
    public CoordinatorLayout mCoordinatorLayout;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content_coordinator_layout);

        Bundle intentBundle = getIntent().getExtras(); //If an intent is passed to main activity.
        if (intentBundle != null) {
            if (intentBundle.get(Constants.KEY_EXTRA_IS_CURRENT_SONG_VIEW_VISIBLE) != null && intentBundle.getBoolean(Constants.KEY_EXTRA_IS_CURRENT_SONG_VIEW_VISIBLE))
                mShouldShowCurrentSongView = true;
        }

        mLibraryFragment = new LibraryFragment();
        switchToLibraryFragment();



        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.SONG_COMPLETE_BROADCAST_ACTION);

        listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQueryText = query;
                ((LibraryFragment) mLibraryFragment).updateTabFilter(mQueryText);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
    }

    @Override
    public void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_library, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(listener);

        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                ((LibraryFragment) mLibraryFragment).updateTabFilter("");
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };

        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, expandListener);
        return true;
    }

    public void switchToLibraryFragment() {
        replaceFragment(mLibraryFragment, mLibraryFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.SONG_COMPLETE_BROADCAST_ACTION)) {
                Track newTrack = (Track) intent.getExtras().getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
                mCurrentSongTitle.setText(newTrack.getTitle());
                if (newTrack.getArtworkURL() == null)
                    mAlbumThumbnail.setImageResource(R.drawable.placeholder);
                else
                    Picasso.with(LibraryActivity.this).load(newTrack.getArtworkURL()).into(mAlbumThumbnail);

                mCurrentSongArtistName.setText(newTrack.getUser().getUsername());

                //Update the current playing song in base activity to the song from this broadcast
                mCurrentSong = newTrack;
            }
        }
    };
}