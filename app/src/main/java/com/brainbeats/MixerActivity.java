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
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;

import com.brainbeats.architecture.AccountManager;
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
    public Mix mNewMix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mMainActionFab = (FloatingActionButton) findViewById(R.id.main_action_fob);

        if (savedInstanceState == null) {
            mMixerFragment = new MixerFragment();
            mNewMixFragment = new CreateMixFragment();
            mConfirmNewMixFragment = new ConfirmCreateMixFragment();
            mMixerDetailFragment = new MixerDetailFragment();
            switchToMixerFragment();
        }

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

        if(mAudioService.getIsPlaying() || mAudioService.getIsPaused()) {
            mAudioService.stopSong();
            hideCurrentSongView();
        }

        mNewMix = new Mix();
        replaceFragment(mNewMixFragment, mNewMixFragment.getTag());
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        navigateUpOrBack(this, fm);
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
        } else if (uri.compareTo(Constants.STOP_SONG_URI) == 0){
            mAudioService.mIsRecordingTest = true;
            mAudioService.stopSong();
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
            mNewMix.setStreamURL(getExternalCacheDir().getAbsolutePath() + "/" + title + ".3gp");
            mNewMix.setMixUserId(Long.parseLong(AccountManager.getInstance(this).getUserId()));
            Uri returnRow = getContentResolver().insert(BrainBeatsContract.MixEntry.CONTENT_URI, Constants.buildMixRecord(mNewMix));
            long returnRowId = ContentUris.parseId(returnRow);
            if(returnRowId != -1){ //file clean-up
                String fileName = getExternalCacheDir().getAbsolutePath() + "/" + getString(R.string.temperary_file_name) + ".3gp"; // clean up our temp file
                File oldFile = new File(fileName);
                File newFile = new File(getExternalCacheDir().getAbsolutePath() + "/" + title + ".3gp");
                boolean success = oldFile.renameTo(newFile);

            }
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
}
