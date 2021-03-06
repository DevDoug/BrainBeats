package com.brainbeats;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;

import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.fragments.LibraryFragment;

public class LibraryActivity extends BaseActivity implements LibraryFragment.OnFragmentInteractionListener {

    Fragment mLibraryFragment;
    String mQueryText;
    SearchView.OnQueryTextListener listener;
    SearchView mSearchView;
    public CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        mCoordinatorLayout = findViewById(R.id.main_content_coordinator_layout);
        mLibraryFragment = new LibraryFragment();
        switchToLibraryFragment();

/*        listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQueryText = query;
                ((LibraryFragment) mLibraryFragment).updateTabFilter(mQueryText);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_library, menu);

        // Inflate the menu; this adds items to the action bar if it is present.
/*        getMenuInflater().inflate(R.menu.menu_library, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(listener);

        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                ((LibraryFragment) mLibraryFragment).updateTabFilter("");
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };

        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, expandListener);*/
        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        navigateUpOrBack(this, fm);
    }

    public void switchToLibraryFragment() {
        replaceFragment(mLibraryFragment, mLibraryFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}