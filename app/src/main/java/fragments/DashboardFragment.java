package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.brainbeats.LoginActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import adapters.SearchMusicAdapter;
import adapters.TrackAdapter;
import architecture.AccountManager;
import entity.Track;
import utils.Constants;
import web.WebApiManager;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "DashboardFragment";

    private RecyclerView mTrackGrid;
    private SearchMusicAdapter mTrackAdapter;
    private GridLayoutManager mBeatGridLayoutManager;
    List<Track> trackList = new ArrayList<>();
    FloatingActionButton mQuickFilterFab;
    FloatingActionButton mFilerByPopularFab;
    FloatingActionButton mFilterByRecentFab;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private OnFragmentInteractionListener mListener;
    private boolean mIsFabOpen = false;
    private SearchView mSearchView;
    private MenuItem searchMenuItem;
    String mQueryText = "";
    SearchView.OnQueryTextListener listener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).getToolBar();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mTrackGrid = (RecyclerView) v.findViewById(R.id.category_grid);
        mQuickFilterFab = (FloatingActionButton) v.findViewById(R.id.floating_action_button_quick_filter);
        mFilerByPopularFab = (FloatingActionButton) v.findViewById(R.id.floating_action_button_filter_by_popular);
        mFilterByRecentFab = (FloatingActionButton) v.findViewById(R.id.floating_action_button_filter_by_recent);

        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);

        mQuickFilterFab.setOnClickListener(this);
        mFilerByPopularFab.setOnClickListener(this);
        mFilterByRecentFab.setOnClickListener(this);

        listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQueryText = query;
                getTracks(WebApiManager.SOUND_CLOUD_QUERY_FILTER_INSTRUMENTAL);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // newText is text entered by user to SearchView
                return false;
            }
        };

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(listener);
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
       getTracks(WebApiManager.SOUND_CLOUD_QUERY_FILTER_INSTRUMENTAL);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.floating_action_button_quick_filter:
                animateFAB();
                break;
            case R.id.floating_action_button_filter_by_popular:
                //sort by popular
                //TODO hookup to local sync and remove api call
                animateFAB();
                getTracks(WebApiManager.SOUND_CLOUD_QUERY_FILTER_PARAM_POPULAR);
                break;
            case R.id.floating_action_button_filter_by_recent:
                //sort by recet
                animateFAB();
                getTracks(WebApiManager.SOUND_CLOUD_QUERY_FILTER_PARAM_RECENT);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void animateFAB() {
        if (mIsFabOpen) {
            mQuickFilterFab.startAnimation(rotate_backward);
            mFilerByPopularFab.startAnimation(fab_close);
            mFilterByRecentFab.startAnimation(fab_close);
            mFilerByPopularFab.setClickable(false);
            mFilterByRecentFab.setClickable(false);
            mIsFabOpen = false;
        } else {
            mQuickFilterFab.startAnimation(rotate_forward);
            mFilerByPopularFab.startAnimation(fab_open);
            mFilterByRecentFab.startAnimation(fab_open);
            mFilerByPopularFab.setClickable(true);
            mFilterByRecentFab.setClickable(true);
            mIsFabOpen = true;
        }
    }

    public void getTracks(String filterTag) {
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
                if (trackList != null) {
                    mTrackAdapter = new SearchMusicAdapter(getContext(), trackList);
                    mBeatGridLayoutManager = new GridLayoutManager(getContext(), Constants.GRID_SPAN_COUNT);
                    mTrackGrid.setLayoutManager(mBeatGridLayoutManager);
                    mTrackGrid.setAdapter(mTrackAdapter);
                    mTrackAdapter.notifyDataSetChanged();
                }
            }
        }, new WebApiManager.OnErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(getClass().getSimpleName(), "Response = " + error.toString());
            }
        });
    }
}
