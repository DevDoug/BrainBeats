package fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.brainbeats.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import adapters.RelatedTracksAdapter;
import entity.Collection;
import entity.RelatedTracksResponse;
import entity.Track;
import model.Mix;
import service.AudioService;
import utils.Constants;
import web.WebApiManager;

public class DashboardDetailFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static final String TAG = "DashboardDetailFragment";

    private TextView mTrackTitle;
    private ImageView mAlbumCoverArt;
    private ImageButton mPlaySongButton;
    public Bundle mUserSelections;

    public List<Collection> mCollections = new ArrayList<>();
    private RecyclerView mAlbumTrackList;
    private RelatedTracksAdapter mRelatedTracksAdapter;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_detail, container, false);
        mAlbumTrackList = (RecyclerView) v.findViewById(R.id.album_title_list);
        mFob = (FloatingActionButton) v.findViewById(R.id.add_to_list_fob);
        mTrackTitle = (TextView) v.findViewById(R.id.track_title);
        mAlbumCoverArt = (ImageView) v.findViewById(R.id.album_cover_art);
        //mPlaySongButton = (ImageButton) v.findViewById(R.id.play_song_button);
        ((TextView)v.findViewById(R.id.separator_title)).setText(R.string.suggested_tracks);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUserSelections = getArguments();
        if(mUserSelections != null) {
            Track selectedTrack = (Track) mUserSelections.get(Constants.KEY_EXTRA_SELECTED_TRACK);
            mTrackTitle.setText(selectedTrack.getTitle());
            Picasso.with(getContext()).load(selectedTrack.getArtworkURL()).into(mAlbumCoverArt);
        }

        WebApiManager.getRelatedTracks(getContext(), new WebApiManager.OnObjectResponseListener() {
            @Override
            public void onObjectResponse(JSONObject object) {
                Log.i(getClass().getSimpleName(), "Response = " + object.toString());
                Gson gson = new Gson();
                Type token = new TypeToken<RelatedTracksResponse>() {
                }.getType();
                try {
                    RelatedTracksResponse relatedTracks = gson.fromJson(object.toString(), token);
                    mCollections = relatedTracks.getCollection();
                    mRelatedTracksAdapter = new RelatedTracksAdapter(getContext(), mCollections);
                    mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
                    mAlbumTrackList.setLayoutManager(mLayoutManager);
                    mAlbumTrackList.setAdapter(mRelatedTracksAdapter);
                    mFob.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new WebApiManager.OnErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(getClass().getSimpleName(), "Response = " + error.toString());
            }
        });

/*        mPlaySongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startService(new Intent(getContext(), AudioService.class));
            }
        });*/
        mFob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.buildListDialogue(getContext(), getString(R.string.add_beat_to_user_list), R.array.add_beat_to_user_list_options,DashboardDetailFragment.this);
            }
        });
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Toast.makeText(getContext(), "Add to library", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(getContext(), "Add to favorites", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(getContext(), "Add to playlist", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
