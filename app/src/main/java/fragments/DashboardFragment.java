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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class DashboardFragment extends Fragment {

    public static final String TAG = "DashboardFragment";

    private RecyclerView mTrackGrid;
    private SearchMusicAdapter mTrackAdapter;
    private GridLayoutManager mBeatGridLayoutManager;
    List<Track> trackList = new ArrayList<>();
    FloatingActionButton mAddCategoryFab;
    private OnFragmentInteractionListener mListener;

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


/*        mAddCategoryFab = (FloatingActionButton) v.findViewById(R.id.floating_action_button_fab_with_listview);
        mAddCategoryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Add Category !", Toast.LENGTH_SHORT).show();
            }
        });*/
        return v;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ProgressDialog loadingMusicDialog = new ProgressDialog(getContext());
        loadingMusicDialog.setCancelable(false);
        loadingMusicDialog.setMessage(getString(R.string.loading_message));
        loadingMusicDialog.show();

        WebApiManager.getMostPopularTracks(getContext(), new WebApiManager.OnArrayResponseListener() {
            @Override
            public void onArrayResponse(JSONArray array) {
                loadingMusicDialog.dismiss();
                Gson gson = new Gson();
                Type token = new TypeToken<List<Track>>() {}.getType();
                List<Track> trackList = gson.fromJson(array.toString(), token);
                if(trackList != null) {
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
