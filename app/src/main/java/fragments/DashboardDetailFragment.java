package fragments;

import android.accounts.AccountManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.brainbeats.MainActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapters.RelatedTracksAdapter;
import entity.Collection;
import entity.RelatedTracksResponse;
import entity.Track;
import model.Mix;
import service.AudioService;
import utils.BeatLearner;
import utils.Constants;
import web.WebApiManager;

public class DashboardDetailFragment extends Fragment implements RelatedTracksAdapter.OnRelatedTrackUpdateListener, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = "DashboardDetailFragment";

    private TextView mTrackTitle;
    private ImageView mAlbumCoverArt;
    private ImageView mPlaySongButton;
    private ImageView mPauseSongButton;
    private ImageView mSkipBackwardButton;
    private ImageView mSkipForwardButton;
    private ImageView mLoopSongButton;

    public  Bundle mUserSelections;
    public AudioService mAudioService;
    boolean mBound = false;

    public Track mSelectedTrack;
    public  List<Collection> mCollections = new ArrayList<>();
    private RecyclerView mAlbumTrackList;
    private RelatedTracksAdapter mRelatedTracksAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SeekBar mPlayTrackSeekBar;
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
    public void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(getContext(), AudioService.class);
        ((MainActivity) getActivity()).bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            ((MainActivity) getActivity()).unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_detail, container, false);
        mAlbumTrackList = (RecyclerView) v.findViewById(R.id.album_title_list);
        mTrackTitle = (TextView) v.findViewById(R.id.track_title);
        mAlbumCoverArt = (ImageView) v.findViewById(R.id.album_cover_art);
        mPlaySongButton = (ImageView) v.findViewById(R.id.play_song_button);
        //mPauseSongButton = (ImageView) v.findViewById(R.id.pause_song_button);
        mSkipBackwardButton = (ImageView) v.findViewById(R.id.skip_backward_button);
        mSkipForwardButton = (ImageView) v.findViewById(R.id.skip_forward_button);
        mLoopSongButton = (ImageView) v.findViewById(R.id.repeat_button);
        mPlayTrackSeekBar = (SeekBar) v.findViewById(R.id.play_song_seek_bar);
        ((TextView)v.findViewById(R.id.separator_title)).setText(R.string.suggested_tracks);

        mPlaySongButton.setOnClickListener(this);
        mSkipBackwardButton.setOnClickListener(this);
        mSkipForwardButton.setOnClickListener(this);
        mLoopSongButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUserSelections = getArguments();
        if(mUserSelections != null) {
            mSelectedTrack = (Track) mUserSelections.get(Constants.KEY_EXTRA_SELECTED_TRACK);
            mTrackTitle.setText(mSelectedTrack.getTitle());
            Picasso.with(getContext()).load(mSelectedTrack.getArtworkURL()).into(mAlbumCoverArt);
            loadRelatedTracks();
        }

/*        mFob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.buildListDialogue(getContext(), getString(R.string.add_beat_to_user_list), R.array.add_beat_to_user_list_options,DashboardDetailFragment.this);
            }
        });*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!architecture.AccountManager.getInstance(getContext()).isLoggedIn())
            inflater.inflate(R.menu.menu_dashboard, menu);
        else
            inflater.inflate(R.menu.menu_dashboard_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get item selected and deal with it
        switch (item.getItemId()) {
            case android.R.id.home:
                //called when the up affordance/carat in actionbar is pressed
                getActivity().onBackPressed();
            case R.id.action_favorite:
                WebApiManager.putUserFavorite(getContext(), architecture.AccountManager.getInstance(getContext()).getUserId(), String.valueOf(mSelectedTrack.getID()), new WebApiManager.OnObjectResponseListener() {
                    @Override
                    public void onObjectResponse(JSONObject object) {
                        object.toString();
                    }
                }, new WebApiManager.OnErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.toString();
                    }
                });
                break;
            case R.id.action_library:
                WebApiManager.putUserTrack(getContext(), String.valueOf(mSelectedTrack.getID()), new WebApiManager.OnObjectResponseListener() {

                    @Override
                    public void onObjectResponse(JSONObject object) {
                        object.toString();
                    }
                }, new WebApiManager.OnErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorRespomse = new String(error.networkResponse.data);
                        error.printStackTrace();
                    }
                });
                break;
            case R.id.action_share:
                Toast.makeText(getContext(), "Share track", Toast.LENGTH_LONG).show();
                break;

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
                WebApiManager.putUserTrack(getContext(), String.valueOf(mSelectedTrack.getID()), new WebApiManager.OnObjectResponseListener() {

                    @Override
                    public void onObjectResponse(JSONObject object) {
                        object.toString();
                    }
                }, new WebApiManager.OnErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.toString();
                    }
                });
                break;
            case 1:
                WebApiManager.putUserFavorite(getContext(), "3207", String.valueOf(mSelectedTrack.getID()), new WebApiManager.OnObjectResponseListener() {
                    @Override
                    public void onObjectResponse(JSONObject object) {
                        object.toString();
                    }
                }, new WebApiManager.OnErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.toString();
                    }
                });
                break;
            case 2:
                Toast.makeText(getContext(), "Add to playlist", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_song_button:
                if (mBound) {
                    if (mAudioService.mPlayer.isPlaying()) {
                        mAudioService.pauseSong();
                        mPlaySongButton.setImageResource(R.drawable.ic_pause_circle);
                    } else {
                        mAudioService.playSong(Uri.parse(mSelectedTrack.getStreamURL()));
                        mAudioService.setProgressIndicator(mPlayTrackSeekBar, mSelectedTrack.getDuration());
                        mPlaySongButton.setImageResource(R.drawable.ic_play_circle);
                    }
                }
                break;
            case R.id.skip_backward_button:
                BeatLearner.getInstance().loadLastBeat();
                break;
            case R.id.skip_forward_button:
                BeatLearner.getInstance().loadNextRecommendedBeat();
                break;
            case R.id.repeat_button:
                if (mBound) {
                    if (!mAudioService.mPlayer.isLooping()) {
                        mAudioService.setSongLooping(true);
                        mLoopSongButton.setImageResource(R.drawable.ic_repeat);
                    } else {
                        mAudioService.setSongLooping(false);
                        mLoopSongButton.setImageResource(R.drawable.ic_repeat_off);
                    }
                }
                break;
            case R.id.shuffle__button:
                break;
            default:
                break;
        }
    }

    @Override
    public void relatedTrackUpdated(Track track) {
        mSelectedTrack = track;
        mTrackTitle.setText(mSelectedTrack.getTitle());
        Picasso.with(getContext()).load(mSelectedTrack.getArtworkURL()).into(mAlbumCoverArt);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            AudioService.AudioBinder binder = (AudioService.AudioBinder) service;
            mAudioService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public void loadRelatedTracks(){
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
                    mRelatedTracksAdapter = new RelatedTracksAdapter(getContext(), mCollections, DashboardDetailFragment.this);
                    mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
                    mAlbumTrackList.setLayoutManager(mLayoutManager);
                    mAlbumTrackList.setAdapter(mRelatedTracksAdapter);
                    mRelatedTracksAdapter.notifyDataSetChanged();
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
    }
}
