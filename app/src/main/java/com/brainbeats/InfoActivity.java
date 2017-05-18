package com.brainbeats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.entity.Track;
import com.brainbeats.fragments.InfoFragment;
import com.brainbeats.fragments.SettingFragment;
import com.brainbeats.utils.Constants;
import com.squareup.picasso.Picasso;

public class InfoActivity extends BaseActivity implements InfoFragment.OnFragmentInteractionListener {

    public Fragment mInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mInfoFragment = new InfoFragment();

        switchToInfoFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hideMainFAB();
    }

    public void switchToInfoFragment() {
        replaceFragment(mInfoFragment, mInfoFragment.getTag());
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
