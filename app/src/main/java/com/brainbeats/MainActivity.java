package com.brainbeats;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import architecture.AccountManager;
import architecture.BaseActivity;
import data.MixContract;
import data.MixDbHelper;
import entity.Track;
import fragments.DashboardDetailFragment;
import fragments.DashboardFragment;
import fragments.DashboardSongListFragment;
import utils.Constants;

public class MainActivity extends BaseActivity implements DashboardFragment.OnFragmentInteractionListener, DashboardSongListFragment.OnFragmentInteractionListener, DashboardDetailFragment.OnFragmentInteractionListener {

    public Fragment mDashboardFragment;
    public Fragment mDashboardSongListFragment;
    public Fragment mDashboardDetailFragment;
    Bundle mUserSelections;
    private CoordinatorLayout mCoordinatorLayout;
    public ContentObserver mDataObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mDashboardFragment = new DashboardFragment();
        mDashboardSongListFragment = new DashboardSongListFragment();
        mDashboardDetailFragment = new DashboardDetailFragment();
        switchToDashboardFragment();

        if (mUserSelections == null) {
            mUserSelections = new Bundle();
        }

        if (!AccountManager.getInstance(this).isLoggedIn()) { //if the user has not created an account load login activity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content_coordinator_layout);

        mDataObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean selfChange) {
                Snackbar createdSnack = Snackbar.make(mCoordinatorLayout, R.string.error_favoriting_message, Snackbar.LENGTH_LONG);
                createdSnack.show();
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                Snackbar createdSnack;
                Log.i("",uri.toString());
                Constants.buildConfirmDialog(MainActivity.this,getString(R.string.login_to_sound_cloud_title));

/*                if(uri == Constants.NOT_LOGGED_IN_TO_SOUNDCLOUD_URI){
                    Constants.buildConfirmDialog(getApplicationContext(),"test");
                } else if (uri == Constants.FAVORITE_ERROR_URI){
                    createdSnack = Snackbar.make(mCoordinatorLayout, R.string.error_favoriting_message, Snackbar.LENGTH_LONG);
                    createdSnack.show();
                } else if(uri == Constants.RATE_ERROR_URI){
                    createdSnack = Snackbar.make(mCoordinatorLayout, R.string.error_favoriting_message, Snackbar.LENGTH_LONG);
                    createdSnack.show();
                } else {
                    createdSnack = Snackbar.make(mCoordinatorLayout, R.string.error_unkown_message, Snackbar.LENGTH_LONG);
                    createdSnack.show();
                }*/
            }
        };
        getContentResolver().registerContentObserver(Constants.NOT_LOGGED_IN_TO_SOUNDCLOUD_URI, false, mDataObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void switchToDashboardFragment() {
        replaceFragment(mDashboardFragment, mDashboardFragment.getTag());
    }

    public void loadRefinedBeatList(String searchParam) {
        toggleNavDrawerIcon();
        mUserSelections.putString(Constants.KEY_EXTRA_SEARCH_KEYWORD, searchParam);
        mDashboardSongListFragment.setArguments(mUserSelections);
        replaceFragment(mDashboardSongListFragment, mDashboardSongListFragment.getTag());
    }

    public void loadBeatDetailFragment(Track track) {
        toggleNavDrawerIcon();
        mUserSelections.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, track);
        mDashboardDetailFragment.setArguments(mUserSelections);
        replaceFragment(mDashboardDetailFragment, mDashboardDetailFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * Created by douglas on 7/22/2016.
     */
    public static class UpdateUIReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            CharSequence intentData = intent.getCharSequenceExtra("message");

        }
    }
}

