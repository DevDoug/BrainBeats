package com.brainbeats.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.brainbeats.LoginActivity;
import com.brainbeats.R;
import com.brainbeats.adapters.SocialAdapter;
import com.brainbeats.architecture.AccountManager;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.utils.Constants;

public class SocialFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mUserList;
    private OnFragmentInteractionListener mListener;
    private String mQueryText = "";
    private SearchView.OnQueryTextListener listener;

    public SocialFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_social, container, false);
        mUserList = (RecyclerView) v.findViewById(R.id.user_list);

        listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQueryText = query;
                Cursor usersCursor = getActivity().getContentResolver().query(
                        BrainBeatsContract.UserEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null, // select args
                        null
                );

                usersCursor.close();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(Constants.SOCIAL_LOADER, null, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mUserList.setLayoutManager(layoutManager);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_social, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(listener);

        if (!mQueryText.equalsIgnoreCase("")) {
            searchMenuItem.expandActionView();
            ((SearchView) searchMenuItem.getActionView()).setQuery(mQueryText, true);
        }

        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mQueryText = "";
                getActivity().getContentResolver().query(BrainBeatsContract.CONTENT_URI_RAW_QUERY,
                        null,
                        null,  //return everything
                        null, //raw query sql
                        null, // select args
                        null
                );

                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };

        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, expandListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AccountManager.getInstance(getContext()).forceLogout(getContext());
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.SOCIAL_LOADER:

                String rawQuery =
                        "SELECT * FROM " + BrainBeatsContract.UserEntry.TABLE_NAME + " INNER JOIN " + BrainBeatsContract.UserFollowersEntry.TABLE_NAME
                                + " ON " + "user." + BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID + " = " + "userfollowers." +
                                BrainBeatsContract.UserFollowersEntry.COLUMN_NAME_USER_FOLLOWER_ID;

                return new CursorLoader(
                        getActivity(),                      // Parent activity context
                        BrainBeatsContract.CONTENT_URI_RAW_QUERY,
                        null,  //return everything
                        rawQuery, //raw query sql
                        null, // select args
                        null                   // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        SocialAdapter mUserAdapter = new SocialAdapter(getContext(), data);
        mUserList.setAdapter(mUserAdapter);
        Log.i("com/brainbeats/data", String.valueOf(data.getColumnCount()));
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
