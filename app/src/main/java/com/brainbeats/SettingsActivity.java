package com.brainbeats;

import android.net.Uri;
import android.os.Bundle;

import architecture.BaseActivity;

public class SettingsActivity extends BaseActivity implements SettingFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
