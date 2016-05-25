package fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.brainbeats.MainActivity;
import com.brainbeats.R;

import java.util.ArrayList;
import java.util.List;
import adapters.BeatAdapter;
import architecture.BaseActivity;
import model.Beat;
import utils.Constants;

public class DashboardFragment extends Fragment {

    public static final String TAG = "DashboardFragment";

    List<Beat> beatList = new ArrayList<>();
    private RecyclerView mBeatsGrid;
    private BeatAdapter mBeatAdapter;
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
        mBeatsGrid = (RecyclerView) v.findViewById(R.id.beats_grid);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBeatAdapter = new BeatAdapter(getContext(),beatList);
        mBeatGridLayoutManager = new GridLayoutManager(getContext(), Constants.GRID_SPAN_COUNT);
        mBeatsGrid.setLayoutManager(mBeatGridLayoutManager);
        mBeatsGrid.setAdapter(mBeatAdapter);

        getBeatData();
    }

    //TODO: Replace dummy data with real data from sound cloud
    public void getBeatData(){
        beatList.add(new Beat());
        beatList.get(0).setBeatTitle("Focus");
        beatList.get(0).setBeatAlbumCoverArt(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder));
        beatList.add(new Beat());
        beatList.get(1).setBeatTitle("Meditation");
        beatList.get(1).setBeatAlbumCoverArt(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder));
        beatList.add(new Beat());
        beatList.get(2).setBeatTitle("Relaxation");
        beatList.get(2).setBeatAlbumCoverArt(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder));
        beatList.add(new Beat());
        beatList.get(3).setBeatTitle("Yoga");
        beatList.get(3).setBeatAlbumCoverArt(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder));
        mBeatAdapter.notifyDataSetChanged();
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
