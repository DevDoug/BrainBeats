package com.brainbeats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.entity.Track;
import com.brainbeats.fragments.SettingFragment;
import com.brainbeats.utils.Constants;

public class SettingsActivity extends BaseActivity implements SettingFragment.OnFragmentInteractionListener {

    public Fragment mSettingsFragment;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mSettingsFragment = new SettingFragment();
        switchToSettingsFragment();

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.SONG_COMPLETE_BROADCAST_ACTION);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hideMainFAB();
    }

    public void switchToSettingsFragment() {
        replaceFragment(mSettingsFragment, mSettingsFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get item selected and deal with it
        switch (item.getItemId()) {
            case android.R.id.home:
                //called when the up affordance/carat in actionbar is pressed
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_global, menu);
        return true;
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

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.SONG_COMPLETE_BROADCAST_ACTION)) {
                Track newTrack = (Track) intent.getExtras().getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
                mCurrentSongTitle.setText(newTrack.getTitle());
                if (newTrack.getArtworkURL() == null)
                    mAlbumThumbnail.setImageResource(R.drawable.placeholder);
                else
                    Picasso.with(SettingsActivity.this).load(newTrack.getArtworkURL()).into(mAlbumThumbnail);

                mCurrentSongArtistName.setText(newTrack.getUser().getUsername());

                //Update the current playing song in base activity to the song from this broadcast
                mCurrentSong = newTrack;
            }
        }
    };
}
