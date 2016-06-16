package com.brainbeats;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
}
