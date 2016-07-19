package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.VolleyError;
import com.brainbeats.LoginActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import java.lang.reflect.Type;
import java.util.List;
import adapters.TrackAdapter;
import architecture.AccountManager;
import entity.Track;
import utils.BeatLearner;
import utils.Constants;
import web.WebApiManager;

/**
 * Created by douglas on 5/31/2016.
 */
public class DashboardSongListFragment extends Fragment {

    public static final String TAG = "DashboardSongListFragment";

    private RecyclerView mTrackGrid;
    private TrackAdapter mTrackAdapter;
    private GridLayoutManager mBeatGridLayoutManager;
    private OnFragmentInteractionListener mListener;
    public Bundle mUserSelections;
    private String mSearchKeyword;

    public DashboardSongListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AccountManager.getInstance(getContext()).forceLogout(getContext());
                Intent loginIntent = new Intent(getContext(),LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        Drawable up = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_up));
        DrawableCompat.setTint(up, getResources().getColor(R.color.theme_primary_text_color));
        toolbar.setNavigationIcon(up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ((MainActivity) getActivity()).navigateUpOrBack(getActivity(), fm);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_song_list, container, false);
        mTrackGrid = (RecyclerView) v.findViewById(R.id.beats_grid);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserSelections = getArguments();
        if(mUserSelections != null) {
            mSearchKeyword = mUserSelections.getString(Constants.KEY_EXTRA_SEARCH_KEYWORD);
        }
        getBeatData();
    }

    public void getBeatData() {
        if (!BeatLearner.getInstance().mHasStartedLearning) { // user does not have any tracks yet.
            final ProgressDialog loadingMusicDialog = new ProgressDialog(getContext());
            loadingMusicDialog.setCancelable(false);
            loadingMusicDialog.setMessage(getString(R.string.loading_message));
            loadingMusicDialog.show();
            WebApiManager.searchTrackWithKeyword(getContext(), mSearchKeyword, new WebApiManager.OnArrayResponseListener() {
                @Override
                public void onArrayResponse(JSONArray array) {
                    loadingMusicDialog.dismiss();
                    Gson gson = new Gson();
                    Type token = new TypeToken<List<Track>>() {}.getType();
                    List<Track> trackList = gson.fromJson(array.toString(), token);
                    if(trackList != null) {
                        mTrackAdapter = new TrackAdapter(getContext(), trackList);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
