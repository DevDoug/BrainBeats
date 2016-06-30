package fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.brainbeats.LoginActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import adapters.RelatedTracksAdapter;
import architecture.AccountManager;
import entity.Collection;
import entity.RelatedTracksResponse;
import entity.Track;
import service.AudioService;
import utils.BeatLearner;
import utils.Constants;
import web.WebApiManager;

public class DashboardDetailFragment extends Fragment implements RelatedTracksAdapter.OnRelatedTrackUpdateListener, View.OnClickListener {

    public static final String TAG = "DashboardDetailFragment";

    private TextView mTrackTitle;
    private ImageView mAlbumCoverArt;
    private ImageView mPlaySongButton;
    private ImageView mSkipBackwardButton;
    private ImageView mSkipForwardButton;
    private ImageView mLoopSongButton;
    private ShareActionProvider mShareActionProvider;
    private CoordinatorLayout mCoordinatorLayout;

    public  Bundle mUserSelections;
    public AudioService mAudioService;
    boolean mBound = false;

    public Track mSelectedTrack;
    public  List<Collection> mCollections = new ArrayList<>();
    private RecyclerView mAlbumTrackList;
    public RelatedTracksAdapter mRelatedTracksAdapter;
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
        mSkipBackwardButton = (ImageView) v.findViewById(R.id.skip_backward_button);
        mSkipForwardButton = (ImageView) v.findViewById(R.id.skip_forward_button);
        mLoopSongButton = (ImageView) v.findViewById(R.id.repeat_button);
        mPlayTrackSeekBar = (SeekBar) v.findViewById(R.id.play_song_seek_bar);
        mCoordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.main_content_coordinator_layout);
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
        }

        mRelatedTracksAdapter = new RelatedTracksAdapter(getContext(), mCollections, DashboardDetailFragment.this);
        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        mAlbumTrackList.setLayoutManager(mLayoutManager);
        mAlbumTrackList.setAdapter(mRelatedTracksAdapter);

        loadRelatedTracks(mSelectedTrack.getID());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard_detail, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        shareIntent.setType("text/plain");
        mShareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            case R.id.action_favorite:
                WebApiManager.putUserFavorite(getContext(), architecture.AccountManager.getInstance(getContext()).getUserId(), String.valueOf(mSelectedTrack.getID()), new WebApiManager.OnObjectResponseListener() {
                    @Override
                    public void onObjectResponse(JSONObject object) {
                        Snackbar createdSnack = Snackbar.make(mCoordinatorLayout, R.string.song_added_to_favorites_snack_message, Snackbar.LENGTH_LONG);
                        createdSnack.show();
                    }
                }, new WebApiManager.OnErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = new String(error.networkResponse.data);
                        Log.i("",errorMessage);
                        Snackbar createdSnack = Snackbar.make(mCoordinatorLayout, R.string.error_favoriting_message, Snackbar.LENGTH_LONG);
                        createdSnack.show();
                    }
                });
                break;
            case R.id.action_logout:
                AccountManager.getInstance(getContext()).forceLogout(getContext());
                Intent loginIntent = new Intent(getContext(),LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
        return false;
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_song_button:
                if (mBound) {
                    if (mAudioService.mPlayer.isPlaying()) {
                        mAudioService.pauseSong();
                        mPlaySongButton.setImageResource(R.drawable.ic_play_circle);
                    } else {
                        mPlaySongButton.setImageResource(R.drawable.ic_pause_circle);
                        mAudioService.playSong(Uri.parse(mSelectedTrack.getStreamURL()));
                        mAudioService.setProgressIndicator(mPlayTrackSeekBar, mSelectedTrack.getDuration());
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
        mAudioService.stopSong();
        mAudioService.mIsPaused = false;
        if(mSelectedTrack.getStreamURL() != null)
            mAudioService.playSong(Uri.parse(mSelectedTrack.getStreamURL()));
        else
            Snackbar.make(mCoordinatorLayout,mAudioService.getString(R.string.error_playing_song_message),Snackbar.LENGTH_LONG).show();
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

    public void loadRelatedTracks(int trackId){
        WebApiManager.getRelatedTracks(getContext(), String.valueOf(trackId), new WebApiManager.OnObjectResponseListener() {
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
                    mAlbumTrackList.setAdapter(mRelatedTracksAdapter);
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
