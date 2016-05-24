package com.brainbeats;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;

import architecture.BaseActivity;
import fragments.SettingFragment;

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.headers_preference, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SettingFragment.class.getName().equals(fragmentName);
    }
}
