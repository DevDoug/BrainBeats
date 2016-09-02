package com.brainbeats;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import architecture.BaseActivity;
import entity.Track;
import fragments.SettingFragment;
import utils.Constants;

public class SettingsActivity extends BaseActivity implements SettingFragment.OnFragmentInteractionListener {

    public Fragment mSettingsFragment;
    Track mPlayingTrack;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mSettingsFragment = new SettingFragment();
        switchToSettingsFragment();

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String intentAction = intent.getAction();

            if (intentAction.equalsIgnoreCase(Constants.INTENT_ACTION_DISPLAY_CURRENT_TRACK)){
                mPlayingTrack = (Track) intent.getExtras().get(Constants.KEY_EXTRA_SELECTED_TRACK);
            }
        }
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
}
