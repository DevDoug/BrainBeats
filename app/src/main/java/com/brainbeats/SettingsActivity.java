package com.brainbeats;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import architecture.AccountManager;
import architecture.BaseActivity;
import fragments.SettingFragment;

public class SettingsActivity extends BaseActivity implements SettingFragment.OnFragmentInteractionListener {

    public Fragment mSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mSettingsFragment = new SettingFragment();
        switchToSettingsFragment();
    }

    public void switchToSettingsFragment(){
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
                AccountManager.getInstance(this).isInCognito(((SettingFragment)mSettingsFragment).mIncogCheckBox.isChecked());
        }
        return true;
    }

}
