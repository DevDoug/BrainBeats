package fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brainbeats.LoginActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.squareup.picasso.Picasso;

import architecture.AccountManager;
import data.BrainBeatsContract;
import entity.Collection;
import entity.Track;
import service.AudioService;
import utils.BeatLearner;
import utils.Constants;
import web.OfflineSyncManager;

public class DashboardDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, BeatLearner.RecommendationCompleteListener {

    public static final String TAG = "DashboardDetailFragment";

    private TextView mTrackTitle;
    private ImageView mAlbumCoverArt;
    private ImageView mPlaySongButton;
    private ImageView mSkipBackwardButton;
    private ImageView mSkipForwardButton;
    private ImageView mLoopSongButton;
    private TextView mArtistName;
    private ShareActionProvider mShareActionProvider;
    private CoordinatorLayout mCoordinatorLayout;
    private LinearLayout mFollowButton;
    public Thread mUpdateSeekBar;
    int mProgressStatus = 0;

    public  Bundle mUserSelections;
    public AudioService mAudioService;
    boolean mBound = false;

    public Track mSelectedTrack;
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
        setRetainInstance(true);

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
    public void onPause() {
        super.onPause();
        if(mUpdateSeekBar != null)
            mUpdateSeekBar.interrupt(); // stop updating a the progress bar if out of view

        mPlayTrackSeekBar.setProgress(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_detail, container, false);
        //mAlbumTrackList = (ListView) v.findViewById(R.id.album_title_list);
        mTrackTitle = (TextView) v.findViewById(R.id.track_title);
        mAlbumCoverArt = (ImageView) v.findViewById(R.id.album_cover_art);
        mPlaySongButton = (ImageView) v.findViewById(R.id.play_song_button);
        mSkipBackwardButton = (ImageView) v.findViewById(R.id.skip_backward_button);
        mSkipForwardButton = (ImageView) v.findViewById(R.id.skip_forward_button);
        mLoopSongButton = (ImageView) v.findViewById(R.id.repeat_button);
        mPlayTrackSeekBar = (SeekBar) v.findViewById(R.id.play_song_seek_bar);
        mArtistName = (TextView) v.findViewById(R.id.user_name);
        mFollowButton = (LinearLayout) v.findViewById(R.id.follow_button);
        //((TextView) v.findViewById(R.id.separator_title)).setText(R.string.suggested_tracks);
        //mCoordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.main_content_coordinator_layout);

        mPlaySongButton.setOnClickListener(this);
        mSkipBackwardButton.setOnClickListener(this);
        mSkipForwardButton.setOnClickListener(this);
        mLoopSongButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("LayoutShiftDetail", true);
        outState.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK,mSelectedTrack);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUserSelections = getArguments();
        if (mUserSelections != null) {
            mSelectedTrack = (Track) mUserSelections.get(Constants.KEY_EXTRA_SELECTED_TRACK);
            if (mSelectedTrack != null) {
                mTrackTitle.setText(mSelectedTrack.getTitle());

                if(mSelectedTrack.getArtworkURL() == null)
                    mAlbumCoverArt.setImageResource(R.drawable.placeholder);
                else
                    Picasso.with(getContext()).load(mSelectedTrack.getArtworkURL()).into(mAlbumCoverArt);

                mArtistName.setText(mSelectedTrack.getUser().getUsername());
            }
        }

        mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle settingsBundle = new Bundle();
                settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE,Constants.SyncDataType.Users.getCode());
                settingsBundle.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, mSelectedTrack);
                OfflineSyncManager.getInstance(getContext()).performSyncOnLocalDb(((MainActivity)getActivity()).mCoordinatorLayout, settingsBundle,getActivity().getContentResolver());
            }
        });

        getLoaderManager().initLoader(Constants.RELATED_TRACKS_LOADER,null,this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard_detail, menu);
/*        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this track" + (mSelectedTrack == null ? mSelectedTrack.getTitle() : ""));
        shareIntent.setType("text/plain");
        mShareActionProvider.setShareIntent(shareIntent);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE,Constants.SyncDataType.Mixes.getCode());
        settingsBundle.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, mSelectedTrack);

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
/*            case R.id.action_rate:
                break;*/
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
                        startProgressBarThread();
                    }
                }
                break;
            case R.id.skip_backward_button:
                BeatLearner.getInstance(getContext()).loadLastBeat();
                break;
            case R.id.skip_forward_button:
                BeatLearner.getInstance(getContext()).loadNextRecommendedBeat(mSelectedTrack.getID(),this);
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

    public void startProgressBarThread(){
        int trackDuration = mSelectedTrack.getDuration();
        mPlayTrackSeekBar.setMax(trackDuration);
        mPlayTrackSeekBar.setIndeterminate(false);
        mUpdateSeekBar = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressStatus < trackDuration) {
                    try {
                        Thread.sleep(1000); //Update once per second
                        mProgressStatus = mAudioService.mPlayer.getCurrentPosition();
                        mPlayTrackSeekBar.setProgress(mProgressStatus);
                        mPlayTrackSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    mAudioService.mPlayer.seekTo(progress);
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.i("Progress bar thread", "Exception occured" + e.toString());
                    }
                }
            }
        });

        mUpdateSeekBar.start();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.RELATED_TRACKS_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        BrainBeatsContract.MixEntry.CONTENT_URI,  // Table to query
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
/*        mRelatedTracksAdapter = new RelatedTracksAdapter(getContext(), data,0);
        mAlbumTrackList.setAdapter(mRelatedTracksAdapter);*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public Track recommendationComplete(Track track) {
        mTrackTitle.setText(track.getTitle());

        if(track.getArtworkURL() == null)
            mAlbumCoverArt.setImageResource(R.drawable.placeholder);
        else
            Picasso.with(getContext()).load(track.getArtworkURL()).into(mAlbumCoverArt);

        if(mBound){
            if(track.getStreamURL() != null){
                mPlaySongButton.setImageResource(R.drawable.ic_pause_circle);
                mAudioService.playSong(Uri.parse(track.getStreamURL()));
                startProgressBarThread();
            }
        }

        mArtistName.setText(track.getUser().getUsername());
        return null;
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
