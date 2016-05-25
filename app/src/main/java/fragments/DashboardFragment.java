package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.brainbeats.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import adapters.TrackAdapter;
import entity.Playlists;
import entity.Track;
import utils.BeatLearner;
import utils.Constants;
import web.WebApiManager;

public class DashboardFragment extends Fragment {

    public static final String TAG = "DashboardFragment";

    List<Track> trackList = new ArrayList<>();
    private RecyclerView mTrackGrid;
    private TrackAdapter mTrackAdapter;
    private GridLayoutManager mBeatGridLayoutManager;
    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mTrackGrid = (RecyclerView) v.findViewById(R.id.beats_grid);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBeatData();
    }

    //TODO: Replace dummy data with real data from sound cloud
    public void getBeatData() {
        if (!BeatLearner.getInstance().mHasStartedLearning) { // user does not have any tracks yet
            String defaultPlaylistID = "405726";
            WebApiManager.getPlayList(getContext(), defaultPlaylistID, new WebApiManager.OnResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Gson gson = new Gson();
                            Type token = new TypeToken<Playlists>() {}.getType();
                            Playlists playlist = gson.fromJson(response.toString(), token);
                            if (playlist != null) {
                                trackList = playlist.getPlaylistTracks();
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
                    }
            );
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
