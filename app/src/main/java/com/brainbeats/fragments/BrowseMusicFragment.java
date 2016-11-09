package com.brainbeats.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.brainbeats.LoginActivity;
import com.brainbeats.R;
import com.brainbeats.adapters.SearchMusicAdapter;
import com.brainbeats.architecture.AccountManager;
import com.brainbeats.entity.Track;
import com.brainbeats.utils.Constants;
import com.brainbeats.web.WebApiManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BrowseMusicFragment extends Fragment implements Constants.ConfirmDialogActionListener {

    public static final String TAG = "BrowseMusicFragment";

    private RecyclerView mTrackGrid;
    private SearchMusicAdapter mTrackAdapter;
    private GridLayoutManager mBeatGridLayoutManager;
    private OnFragmentInteractionListener mListener;
    private String mQueryText = "";
    private SearchView.OnQueryTextListener listener;

    public BrowseMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse, container, false);
        mTrackGrid = (RecyclerView) v.findViewById(R.id.category_grid);

        listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQueryText = query;
                getTracks(WebApiManager.SOUND_CLOUD_QUERY_FILTER_INSTRUMENTAL);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
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
                getTracks(WebApiManager.SOUND_CLOUD_QUERY_FILTER_INSTRUMENTAL);
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
    public void onResume() {
        super.onResume();
        if (mQueryText.equalsIgnoreCase("")) {
            getTracks(WebApiManager.SOUND_CLOUD_QUERY_FILTER_INSTRUMENTAL);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
    public void PerformDialogAction() {
        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void getTracks(String filterTag) {
        if (Constants.isNetworkAvailable(getContext())) {
            final ProgressDialog loadingMusicDialog = new ProgressDialog(getContext());
            loadingMusicDialog.setCancelable(false);
            loadingMusicDialog.setMessage(getString(R.string.loading_message));
            loadingMusicDialog.show();

            WebApiManager.getTracks(getContext(), mQueryText, filterTag, new WebApiManager.OnArrayResponseListener() {
                @Override
                public void onArrayResponse(JSONArray array) {
                    loadingMusicDialog.dismiss();
                    Gson gson = new Gson();
                    Type token = new TypeToken<List<Track>>() {
                    }.getType();
                    ArrayList<Track> trackList = gson.fromJson(array.toString(), token);
                    if (trackList.size() != 0) {
                        mTrackAdapter = new SearchMusicAdapter(getContext(), trackList);
                        mBeatGridLayoutManager = new GridLayoutManager(getContext(), Constants.GRID_SPAN_COUNT);
                        mTrackGrid.setLayoutManager(mBeatGridLayoutManager);
                        mTrackGrid.setAdapter(mTrackAdapter);
                        mTrackAdapter.notifyDataSetChanged();
                    } else {
                        Constants.buildInfoDialog(getContext(), getString(R.string.no_results_found_error_message), getString(R.string.no_search_results));
                    }
                }
            }, new WebApiManager.OnErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(getClass().getSimpleName(), "Response = " + error.toString());
                    loadingMusicDialog.dismiss();
                }
            });
        } else {
            Constants.buildActionDialog(getContext(), getString(R.string.connect_to_network_message), getString(R.string.enable_wifi_in_settings_message), getString(R.string.go_to_settings_message), this);
        }
    }
}
