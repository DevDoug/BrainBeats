package fragments;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

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

import adapters.MixerAdapter;
import adapters.RelatedTracksAdapter;
import architecture.AccountManager;
import data.MixContract;
import data.MixDbHelper;
import entity.Collection;
import entity.RelatedTracksResponse;
import entity.Track;
import service.AudioService;
import utils.BeatLearner;
import utils.Constants;
import web.OfflineSyncManager;
import web.WebApiManager;

public class DashboardDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

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
    private ListView mAlbumTrackList;
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
        getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            getContext().unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_detail, container, false);
        mAlbumTrackList = (ListView) v.findViewById(R.id.album_title_list);
        mTrackTitle = (TextView) v.findViewById(R.id.track_title);
        mAlbumCoverArt = (ImageView) v.findViewById(R.id.album_cover_art);
        mPlaySongButton = (ImageView) v.findViewById(R.id.play_song_button);
        mSkipBackwardButton = (ImageView) v.findViewById(R.id.skip_backward_button);
        mSkipForwardButton = (ImageView) v.findViewById(R.id.skip_forward_button);
        mLoopSongButton = (ImageView) v.findViewById(R.id.repeat_button);
        mPlayTrackSeekBar = (SeekBar) v.findViewById(R.id.play_song_seek_bar);
        ((TextView) v.findViewById(R.id.separator_title)).setText(R.string.suggested_tracks);
        //mCoordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.main_content_coordinator_layout);


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
        if (mUserSelections != null) {
            mSelectedTrack = (Track) mUserSelections.get(Constants.KEY_EXTRA_SELECTED_TRACK);
            if (mSelectedTrack != null) {
                mTrackTitle.setText(mSelectedTrack.getTitle());
            }
            Picasso.with(getContext()).load(mSelectedTrack.getArtworkURL()).into(mAlbumCoverArt);
        }

        getLoaderManager().initLoader(Constants.RELATED_TRACKS_LOADER,null,this);
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
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this track" + (mSelectedTrack == null ? mSelectedTrack.getTitle() : ""));
        shareIntent.setType("text/plain");
        mShareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putInt(Constants.KEY_EXTRA_SELECTED_TRACK_ID,mSelectedTrack.getID());
        settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE,Constants.SyncDataType.Mixes.getCode());
        settingsBundle.putString(Constants.KEY_EXTRA_SELECTED_TRACK_TITLE,mSelectedTrack.getTitle());
        settingsBundle.putString(Constants.KEY_EXTRA_SELECTED_TRACK_ALBUM_COVER_ART,mSelectedTrack.getArtworkURL());

        //TODO move local db code out of sync adaper so that sync adapter only has api call's or network interaction.
        //TODO add sync to sound cloud for determing weather to update sc as well.

        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            case R.id.action_add_to_library:
                settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_ACTION,Constants.SyncDataAction.UpdateMix.getCode());
                OfflineSyncManager.getInstance(getContext()).performSyncOnLocalDb(((MainActivity)getActivity()).mCoordinatorLayout, settingsBundle,getActivity().getContentResolver());
                break;
            case R.id.action_favorite:
                settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_ACTION,Constants.SyncDataAction.UpdateFavorite.getCode());
                OfflineSyncManager.getInstance(getContext()).performSyncOnLocalDb(((MainActivity)getActivity()).mCoordinatorLayout, settingsBundle,getActivity().getContentResolver());
                break;
            case R.id.action_rate:
                break;
            case R.id.action_logout:
                AccountManager.getInstance(getContext()).forceLogout(getContext());
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
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
                        mAudioService.setProgressIndicator(mPlaySongButton,mPlayTrackSeekBar, mSelectedTrack.getDuration());
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.RELATED_TRACKS_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        MixContract.MixEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        null,                  // No selection clause
                        null,                  // No selection arguments
                        null                   // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRelatedTracksAdapter = new RelatedTracksAdapter(getContext(), data,0);
        mAlbumTrackList.setAdapter(mRelatedTracksAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
}
