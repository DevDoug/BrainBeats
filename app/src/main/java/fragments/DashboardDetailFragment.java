package fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.brainbeats.R;

import java.util.ArrayList;
import java.util.List;
import adapters.BeatAlbumAdapter;
import architecture.BaseActivity;
import model.Beat;

import static architecture.BaseActivity.navigateUpOrBack;

public class DashboardDetailFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "DashboardDetailFragment";

    List<Beat> beatList = new ArrayList<>();
    private RecyclerView mAlbumTrackList;
    private BeatAlbumAdapter mBeatAlbumAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;
    public FloatingActionButton mFob;

    public DashboardDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        Drawable up = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_up));
        DrawableCompat.setTint(up, getResources().getColor(R.color.theme_primary_text_color));
        toolbar.setNavigationIcon(up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                navigateUpOrBack(getActivity(), fm);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_detail, container, false);
        mAlbumTrackList = (RecyclerView) v.findViewById(R.id.album_title_list);
        mFob = (FloatingActionButton) v.findViewById(R.id.add_to_list_fob);
        ((TextView)v.findViewById(R.id.separator_title)).setText("Album Name");
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBeatAlbumAdapter = new BeatAlbumAdapter(getContext(),beatList);
        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        mAlbumTrackList.setLayoutManager(mLayoutManager);
        mAlbumTrackList.setAdapter(mBeatAlbumAdapter);
        mFob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddToUserListDialog();
            }
        });
        getBeatData();
    }

    //TODO: Replace dummy data with real data from sound cloud
    public void getBeatData(){
        beatList.add(new Beat());
        beatList.get(0).setBeatTitle("Focus");
        beatList.add(new Beat());
        beatList.get(1).setBeatTitle("Meditation");
        beatList.add(new Beat());
        beatList.get(2).setBeatTitle("Relaxation");
        beatList.add(new Beat());
        beatList.get(3).setBeatTitle("Yoga");
        mBeatAlbumAdapter.notifyDataSetChanged();
    }

    public void displayAddToUserListDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = (View) inflater.inflate(R.layout.custom_dialog_layout, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText(getContext().getString(R.string.add_beat_to_user_list));
        ((TextView)dialogView.findViewById(R.id.option_1)).setText(getResources().getStringArray(R.array.add_beat_to_user_list_options)[0]);
        ((TextView)dialogView.findViewById(R.id.option_1)).setOnClickListener(this);
        ((TextView)dialogView.findViewById(R.id.option_2)).setText(getResources().getStringArray(R.array.add_beat_to_user_list_options)[1]);
        ((TextView)dialogView.findViewById(R.id.option_2)).setOnClickListener(this);
        ((TextView)dialogView.findViewById(R.id.option_3)).setText(getResources().getStringArray(R.array.add_beat_to_user_list_options)[2]);
        ((TextView)dialogView.findViewById(R.id.option_3)).setOnClickListener(this);
        ((TextView)dialogView.findViewById(R.id.option_4)).setVisibility(View.GONE);
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.option_1:
                Toast.makeText(getContext(), "Add to library", Toast.LENGTH_LONG).show();
                break;
            case R.id.option_2:
                Toast.makeText(getContext(), "Add to favorites", Toast.LENGTH_LONG).show();
                break;
            case R.id.option_3:
                Toast.makeText(getContext(), "Add to playlist", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get item selected and deal with it
        switch (item.getItemId()) {
            case android.R.id.home:
                //called when the up affordance/carat in actionbar is pressed
                getActivity().onBackPressed();
        }
        return true;
    }

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
        void onFragmentInteraction(Uri uri);
    }
}
