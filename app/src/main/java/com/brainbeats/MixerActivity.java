package com.brainbeats;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.fragments.ConfirmCreateMixFragment;
import com.brainbeats.fragments.CreateMixFragment;

import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.fragments.MixerDetailFragment;
import com.brainbeats.fragments.MixerFragment;
import com.brainbeats.model.Mix;

import com.brainbeats.utils.Constants;

import java.io.File;

public class MixerActivity extends BaseActivity implements View.OnClickListener, MixerFragment.OnFragmentInteractionListener, CreateMixFragment.OnFragmentInteractionListener, ConfirmCreateMixFragment.OnFragmentInteractionListener {

    Fragment mMixerFragment;
    Fragment mNewMixFragment;
    Fragment mConfirmNewMixFragment;
    Fragment mMixerDetailFragment;

    Bundle mUserSelections;
    public FloatingActionButton mMainActionFab;
    private IntentFilter mIntentFilter;
    public Mix mNewMix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mMainActionFab = (FloatingActionButton) findViewById(R.id.main_action_fob);

        mMixerFragment = new MixerFragment();
        mNewMixFragment = new CreateMixFragment();
        mConfirmNewMixFragment = new ConfirmCreateMixFragment();
        mMixerDetailFragment = new MixerDetailFragment();

        switchToMixerFragment();

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.SONG_COMPLETE_BROADCAST_ACTION);

        if (mUserSelections == null) {
            mUserSelections = new Bundle();
        }

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String intentAction = intent.getAction();
            if (intentAction.equalsIgnoreCase(Constants.INTENT_ACTION_GO_TO_DETAIL_FRAGMENT)) {
                Mix mix = (Mix) intent.getExtras().get(Constants.KEY_EXTRA_SELECTED_MIX);
                loadMixerDetailFragment(mix);
            }
        }

        mMainActionFab.setImageDrawable(getDrawable(R.drawable.ic_add_white));
        mMainActionFab.setOnClickListener(this);
    }

    public void switchToMixerFragment() {
        replaceFragment(mMixerFragment, mMixerFragment.getTag());
    }

    public void switchToNewMixFragment() {
        toggleNavDrawerIcon();
        mNewMix = new Mix();
        replaceFragment(mNewMixFragment, mNewMixFragment.getTag());
    }

    public void switchToConfirmCreateMixFragment() {
        toggleNavDrawerIcon();
        replaceFragment(mConfirmNewMixFragment, mConfirmNewMixFragment.getTag());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_action_fob:
                switchToNewMixFragment();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.compareTo(Constants.NEW_MIX_HIDE_FAB) == 0) {
            hideMainFAB();
        } else if (uri.compareTo(Constants.MIX_SHOW_FAB) == 0) {
            showMainFAB();
        } else if (uri.compareTo(Constants.MIX_SHOW_MIX_LIST) == 0) {
            Intent mixerIntent = new Intent(getApplicationContext(), MixerActivity.class);
            createBackStack(mixerIntent);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri, String source) {
        if (uri.compareTo(Constants.LOAD_SONG_URI) == 0) {
            mAudioService.mIsRecordingTest = true;
            mAudioService.playSong(Uri.fromFile(new File(source)));
        } else if (uri.compareTo(Constants.NEW_MIX_LOAD_CONFIRM_FRAG) == 0) {
            mNewMix.setStreamURL(source);
            switchToConfirmCreateMixFragment();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri, String title, String imageUrl) {
        if (uri.compareTo(Constants.MIX_ADD_NEW) == 0) {
            mNewMix.setMixTitle(title);
            mNewMix.setIsInMixer(1);
            mNewMix.setIsInLibrary(1);
            Uri returnRow = getContentResolver().insert(BrainBeatsContract.MixEntry.CONTENT_URI, Constants.buildMixRecord(mNewMix));
            long returnRowId = ContentUris.parseId(returnRow);
        }
    }

    public void loadMixerDetailFragment(Mix mix) {
        if (mDrawerToggle != null)
            toggleNavDrawerIcon();

        mUserSelections.putParcelable(Constants.KEY_EXTRA_SELECTED_MIX, mix);
        mMixerDetailFragment.setArguments(mUserSelections);
        replaceFragment(mMixerDetailFragment, mMixerDetailFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
/*            if(intent.getAction().equals(Constants.SONG_COMPLETE_BROADCAST_ACTION)) {
                Track newTrack = (Track) intent.getExtras().getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
                mCurrentSongTitle.setText(newTrack.getTitle());
                if (newTrack.getArtworkURL() == null)
                    mAlbumThumbnail.setImageResource(R.drawable.placeholder);
                else
                    Picasso.with(MixerActivity.this).load(newTrack.getArtworkURL()).into(mAlbumThumbnail);

                mCurrentSongArtistName.setText(newTrack.getUser().getUsername());

                //Update the current playing song in base activity to the song from this broadcast
                mCurrentSong = newTrack;
            }*/
        }
    };
}
