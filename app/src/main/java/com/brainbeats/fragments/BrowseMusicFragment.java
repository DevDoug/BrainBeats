package com.brainbeats.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.brainbeats.R;
import com.brainbeats.adapters.SearchMusicAdapter;
import com.brainbeats.entity.Track;
import com.brainbeats.entity.TrackCollection;
import com.brainbeats.model.Mix;
import com.brainbeats.utils.Constants;
import com.brainbeats.web.WebApiManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class BrowseMusicFragment extends Fragment implements Constants.ConfirmDialogActionListener {

    public static final String TAG = "BrowseMusicFragment";

    private FirebaseDatabase mFirebaseDatabase;
    private Query mFirebasDatabaseReference;

    private RecyclerView mTrackGrid;
    private SearchMusicAdapter mTrackAdapter;
    private GridLayoutManager mBeatGridLayoutManager;
    private ArrayList<Track> mTracks = new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    private String mQueryText = "";
    private SearchView.OnQueryTextListener listener;
    AlertDialog alert;

    private String mNextTracksHref;
    private boolean mIsLoading;
    private boolean mIsLastPage;
    private String mAdvancedSearchTagsList = "";

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
                getTracks(query,"","","");
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
                getTracks("","","","");
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
            case R.id.advanced_search:
                showAdvancedSearchDialog();
                break;
            case R.id.action_logout:
                mListener.onFragmentInteraction(Constants.LOGOUT_URI);
                break;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mQueryText.equalsIgnoreCase("")) {
            getTracks("","","","");
        }
    }

    public void showAdvancedSearchDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.advanced_filter_dialog, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText("");

        EditText songTitle = (EditText) dialogView.findViewById(R.id.query_text);
        final EditText songTags = (EditText) dialogView.findViewById(R.id.tag_text);
        Spinner genreSpinner = (Spinner) dialogView.findViewById(R.id.genres_spinner);
        LinearLayout tagContainer = (LinearLayout) dialogView.findViewById(R.id.tag_container);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.genres, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(adapter);
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.theme_secondary_text_color));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        genreSpinner.setSelection(0);

        songTags.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    View tag = inflater.inflate(R.layout.tag_item, null);
                    Button b = (Button) tag.findViewById(R.id.sound_cloud_tag);
                    mAdvancedSearchTagsList = mAdvancedSearchTagsList.concat("#" + songTags.getText().toString() + " ");
                    b.setText(songTags.getText().toString());
/*                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tagContainer.removeView(v);
                        }
                    });*/
                    tagContainer.addView(tag);
                    songTags.setText("");
                    return true;
                }
                return false;
            }
        });
        builder.setView(dialogView);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getTracks(songTitle.getText().toString(),genreSpinner.getSelectedItem().toString(),mAdvancedSearchTagsList,"");
                mAdvancedSearchTagsList = "";
            }
        });
        builder.setOnDismissListener(dialog -> mAdvancedSearchTagsList = "");
        alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirebaseDatabase = mFirebaseDatabase.getInstance();
        mFirebasDatabaseReference = mFirebaseDatabase
                .getReference("mixes/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild("artistId")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public void updateMixes() {
        mFirebasDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mTracks.add(new Track(dataSnapshot.getValue(Mix.class)));
                mTrackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
/*                Track track = new Track(dataSnapshot.getValue(Mix.class));
                int index = getMixItemIndex(mix);
                mixList.set(index, mix);
                mLibraryMixAdapter.notifyItemChanged(index);*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
/*                Mix mix = dataSnapshot.getValue(Mix.class);
                int index = getMixItemIndex(mix);
                mixList.remove(index);
                mLibraryMixAdapter.notifyItemRemoved(index);

                if(index == 0){
                    mEmptyDataPlaceholder.setVisibility(View.VISIBLE);
                    mMixRecyclerView.setVisibility(View.INVISIBLE);
                }*/
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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

    public void getTracks(String query, String genre, String tagList, String sortOrder) {
        if (Constants.isNetworkAvailable(getContext())) {
            ProgressDialog loadingMusicDialog = new ProgressDialog(getActivity());
            loadingMusicDialog.setCancelable(false);
            loadingMusicDialog.setMessage(getString(R.string.loading_message));
            loadingMusicDialog.show();

            WebApiManager.getTracks(getContext(), query, genre, tagList, new WebApiManager.OnObjectResponseListener() {
                @Override
                public void onObjectResponse(JSONObject object) {
                    loadingMusicDialog.dismiss();
                    Gson gson = new Gson();
                    Type token = new TypeToken<TrackCollection>() {}.getType();
                    TrackCollection tracks = gson.fromJson(object.toString(), token);
                    mNextTracksHref = tracks.getNextHref();
                    mTracks = tracks.getTracks();
                    if (mTracks.size() != 0) {
                        mTrackAdapter = new SearchMusicAdapter(getContext(), mTracks);
                        mBeatGridLayoutManager = new GridLayoutManager(getContext(), Constants.GRID_SPAN_COUNT);

                        if(sortOrder.equalsIgnoreCase("Alphabet"))
                            Collections.sort(mTracks);

                        mTrackGrid.setLayoutManager(mBeatGridLayoutManager);
                        mTrackGrid.setAdapter(mTrackAdapter);
                        mTrackAdapter.notifyDataSetChanged();

                        updateMixes();

                        mTrackGrid.addOnScrollListener(recyclerViewOnScrollListener);

                    } else {
                        Constants.buildInfoDialog(getContext(), getString(R.string.error_no_results_found_error_message), getString(R.string.no_search_results));
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

    public void loadMoreItems() {
        mIsLoading = true;
        WebApiManager.getNextTrackListByHref(getActivity(), mNextTracksHref, mQueryText, "", new WebApiManager.OnObjectResponseListener() {
            @Override
            public void onObjectResponse(JSONObject object) {
                Gson gson = new Gson();
                Type token = new TypeToken<TrackCollection>() {}.getType();
                TrackCollection tracks = gson.fromJson(object.toString(), token);
                mNextTracksHref = tracks.getNextHref();
                ArrayList<Track> trackList = tracks.getTracks();
                if(trackList.size() != 0) {
                    mTrackAdapter.mTracks.addAll(trackList);
                    mTrackAdapter.notifyDataSetChanged();
                    mIsLoading = false;
                }
            }
        }, new WebApiManager.OnErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mBeatGridLayoutManager.getChildCount();
            int totalItemCount = mBeatGridLayoutManager.getItemCount();
            int firstVisibleItemPosition = mBeatGridLayoutManager.findFirstVisibleItemPosition();

            if (!mIsLoading && !mIsLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= 50) {
                    loadMoreItems();
                }
            }
        }
    };
}
