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
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import adapters.SearchMusicAdapter;
import adapters.TrackAdapter;
import entity.Playlists;
import entity.Track;
import utils.BeatLearner;
import utils.Constants;
import web.WebApiManager;

public class DashboardFragment extends Fragment {

    public static final String TAG = "DashboardFragment";

    private RecyclerView mTrackGrid;
    private SearchMusicAdapter mTrackAdapter;
    private GridLayoutManager mBeatGridLayoutManager;
    List<Track> trackList = new ArrayList<>();
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
        mTrackGrid = (RecyclerView) v.findViewById(R.id.category_grid);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Track> trackCategory = new ArrayList<>();
        for(int i = 0; i < getResources().getStringArray(R.array.beat_categories).length;i++){
            Track  defaultTrack = new Track();
            defaultTrack.setTitle(getResources().getStringArray(R.array.beat_categories)[i]);
            trackCategory.add(defaultTrack);
        }
        trackList = trackCategory;
        mTrackAdapter = new SearchMusicAdapter(getContext(), trackList);
        mBeatGridLayoutManager = new GridLayoutManager(getContext(), Constants.GRID_SPAN_COUNT);
        mTrackGrid.setLayoutManager(mBeatGridLayoutManager);
        mTrackGrid.setAdapter(mTrackAdapter);
        mTrackAdapter.notifyDataSetChanged();
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
