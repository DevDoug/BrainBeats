package com.brainbeats;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
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
import model.MixItem;
import utils.Constants;

public class MainActivity extends BaseActivity implements DashboardFragment.OnFragmentInteractionListener, DashboardSongListFragment.OnFragmentInteractionListener, DashboardDetailFragment.OnFragmentInteractionListener {

    public Fragment mDashboardFragment;
    public Fragment mDashboardSongListFragment;
    public Fragment mDashboardDetailFragment;
    Bundle mUserSelections;
    private CoordinatorLayout mCoordinatorLayout;
    public ContentObserver mDataObserver;

    //Feilds for testing sync adapter
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.example.android.datasync";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    public Account mAccount;

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
        mAccount = CreateSyncAccount(this);

        mDataObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean selfChange) {
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                Log.i("",uri.toString());
                Snackbar createdSnack;
                if(uri.toString().equalsIgnoreCase(Constants.ADDED_TO_LIBRARY_URI.toString())) {
                    createdSnack = Snackbar.make(mCoordinatorLayout, R.string.song_added_to_library_snack_message, Snackbar.LENGTH_LONG);
                    createdSnack.show();
                } else if (uri.toString().equalsIgnoreCase(Constants.LIBRARY_ALREADY_URI.toString())){
                    createdSnack = Snackbar.make(mCoordinatorLayout, R.string.error_this_mix_is_already_in_library, Snackbar.LENGTH_LONG);
                    createdSnack.show();
                } else if (uri.toString().equalsIgnoreCase(Constants.FAVORITE_ERROR_URI.toString())){
                    createdSnack = Snackbar.make(mCoordinatorLayout, R.string.error_favoriting_message, Snackbar.LENGTH_LONG);
                    createdSnack.show();
                } else if(uri.toString().equalsIgnoreCase(Constants.NOT_LOGGED_IN_TO_SOUNDCLOUD_URI.toString())){
                    Constants.buildConfirmDialog(getApplicationContext(),"test");
                } else {
                    createdSnack = Snackbar.make(mCoordinatorLayout, R.string.error_unkown_message, Snackbar.LENGTH_LONG);
                    createdSnack.show();
                }
            }
        };
        getContentResolver().registerContentObserver(Constants.ADDED_TO_LIBRARY_URI, false, mDataObserver);
        getContentResolver().registerContentObserver(Constants.LIBRARY_ALREADY_URI, false, mDataObserver);
        getContentResolver().registerContentObserver(Constants.FAVORITE_SUCCESS_URI, false, mDataObserver);
        getContentResolver().registerContentObserver(Constants.FAVORITE_ALREADY_URI, false, mDataObserver);
        getContentResolver().registerContentObserver(Constants.FAVORITE_ERROR_URI, false, mDataObserver);
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
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        settingsBundle.putInt(Constants.KEY_EXTRA_SELECTED_TRACK_ID,track.getID());
        settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE,1);
        ContentResolver.requestSync(mAccount, MixContract.CONTENT_AUTHORITY, settingsBundle);

        toggleNavDrawerIcon();
        mUserSelections.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, track);
        mDashboardDetailFragment.setArguments(mUserSelections);
        replaceFragment(mDashboardDetailFragment, mDashboardDetailFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        android.accounts.AccountManager accountManager = (android.accounts.AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            return newAccount;
        } else {
            return accountManager.getAccountsByType(ACCOUNT_TYPE)[0];
        }
    }
}
