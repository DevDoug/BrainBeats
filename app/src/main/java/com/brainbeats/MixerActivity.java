package com.brainbeats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import architecture.BaseActivity;
import entity.Track;
import fragments.MixerDetailFragment;
import fragments.MixerFragment;
import model.Mix;
import utils.BeatLearner;
import utils.Constants;

public class MixerActivity extends BaseActivity implements View.OnClickListener, MixerFragment.OnFragmentInteractionListener {

    Fragment mMixerFragment;
    Fragment mMixerDetailFragment;
    Bundle mUserSelections;
    Track mPlayingTrack;
    public FloatingActionButton mMainActionFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mMainActionFab = (FloatingActionButton) findViewById(R.id.main_action_fob);

        mMixerFragment = new MixerFragment();
        mMixerDetailFragment = new MixerDetailFragment();
        switchToMixerFragment();

        if (mUserSelections == null) {
            mUserSelections = new Bundle();
        }

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String intentAction = intent.getAction();
            if(intentAction.equalsIgnoreCase(Constants.INTENT_ACTION_GO_TO_DETAIL_FRAGMENT)){
                Mix mix = (Mix) intent.getExtras().get(Constants.KEY_EXTRA_SELECTED_MIX);
                loadMixerDetailFragment(mix);
            } else if (intentAction.equalsIgnoreCase(Constants.INTENT_ACTION_DISPLAY_CURRENT_TRACK)){
                mPlayingTrack = (Track) intent.getExtras().get(Constants.KEY_EXTRA_SELECTED_TRACK);
            } else if (intentAction.equalsIgnoreCase(Constants.INTENT_ACTION_GO_TO_MIX_DETAIL_FRAGMENT)) {
/*                Mix mix = (Mix) intent.getExtras().get(Constants.KEY_EXTRA_SELECTED_MIX);
                loadMixerDetailFragment(mix);*/
            }
        }

        mMainActionFab.setImageDrawable(getDrawable(R.drawable.ic_add_white));
        mMainActionFab.setOnClickListener(this);
    }

    public void switchToMixerFragment() {
        replaceFragment(mMixerFragment, mMixerFragment.getTag());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(mPlayingTrack != null){
            mCurrentSong = mPlayingTrack;
            updateCurrentSongNotificationUI();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_action_fob:
                ((MixerFragment) mMixerFragment).mAddOptionsDialog = Constants.buildListDialogue(MixerActivity.this, getString(R.string.new_beat_title), R.array.new_beat_options, ((MixerFragment) mMixerFragment));
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
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
