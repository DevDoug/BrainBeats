package fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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

import adapters.MixTagAdapter;
import architecture.AccountManager;
import data.BrainBeatsContract;
import data.BrainBeatsDbHelper;
import entity.Track;
import entity.User;
import service.AudioService;
import utils.BeatLearner;
import utils.Constants;
import sync.OfflineSyncManager;
import web.WebApiManager;

public class DashboardDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static final String TAG = "DashboardDetailFragment";

    private TextView mTrackTitle;
    private TextView mArtistDescription;
    private ImageView mAlbumCoverArt;
    private ImageView mPlaySongButton;
    private ImageView mUpvoteArrow;
    private ImageView mDownVoteArrow;
    private ImageView mSkipForwardButton;
    private ImageView mLoopSongButton;
    private TextView mArtistName;
    private ImageView mArtistThumbnail;
    public Thread mUpdateSeekBar;
    int mProgressStatus = 0;

    public Bundle mUserSelections;
    public AudioService mAudioService;
    boolean mBound = false;

    public Track mSelectedTrack;
    private SeekBar mPlayTrackSeekBar;
    private OnFragmentInteractionListener mListener;

    private FloatingActionButton mTrackOptionsFab;
    private FloatingActionButton mAddToLibraryFab;
    private FloatingActionButton mFavFab;
    private FloatingActionButton mFollowArtistFab;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private boolean mIsFabOpen = false;
    private boolean mLooping = false;
    private boolean mIsAlive = true;

    private MixTagAdapter mMixTagAdapter;
    private RecyclerView mMixerTags;


    public DashboardDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getContext(), AudioService.class);
        getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        //((MainActivity) getActivity()).mCurrentSongPlayingView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
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
        if (mUpdateSeekBar != null)
            mUpdateSeekBar.interrupt(); // stop updating a the progress bar if out of view

        if(mAudioService.getIsPlaying() || mAudioService.mIsPaused)
            ((MainActivity) getActivity()).mCurrentSongPlayingView.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_detail, container, false);
        mMixerTags = (RecyclerView) v.findViewById(R.id.mix_tag_grid);

        mTrackTitle = (TextView) v.findViewById(R.id.track_title);
        mArtistDescription = (TextView) v.findViewById(R.id.artist_description);
        mAlbumCoverArt = (ImageView) v.findViewById(R.id.album_cover_art);
        mPlaySongButton = (ImageView) v.findViewById(R.id.play_song_button);
        mUpvoteArrow = (ImageView) v.findViewById(R.id.arrow_up);
        mDownVoteArrow = (ImageView) v.findViewById(R.id.arrow_down);
        mSkipForwardButton = (ImageView) v.findViewById(R.id.skip_forward_button);
        mLoopSongButton = (ImageView) v.findViewById(R.id.repeat_button);
        mArtistThumbnail = (ImageView) v.findViewById(R.id.artist_thumbnail);
        mPlayTrackSeekBar = (SeekBar) v.findViewById(R.id.play_song_seek_bar);
        mArtistName = (TextView) v.findViewById(R.id.user_name);

        mTrackOptionsFab = (FloatingActionButton) v.findViewById(R.id.floating_action_button_track_options);
        mAddToLibraryFab = (FloatingActionButton) v.findViewById(R.id.floating_action_button_add_to_library);
        mFavFab = (FloatingActionButton) v.findViewById(R.id.floating_action_favorite);
        mFollowArtistFab = (FloatingActionButton) v.findViewById(R.id.floating_follow_artist);

        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);

        mTrackOptionsFab.setOnClickListener(this);
        mAddToLibraryFab.setOnClickListener(this);
        mFavFab.setOnClickListener(this);
        mFollowArtistFab.setOnClickListener(this);

        mPlaySongButton.setOnClickListener(this);
        mUpvoteArrow.setOnClickListener(this);
        mDownVoteArrow.setOnClickListener(this);
        mSkipForwardButton.setOnClickListener(this);
        mLoopSongButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("LayoutShiftDetail", true);
        outState.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, mSelectedTrack);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUserSelections = getArguments();
        if (mUserSelections != null) {
            mSelectedTrack = (Track) mUserSelections.get(Constants.KEY_EXTRA_SELECTED_TRACK);
            if (mSelectedTrack != null) {
                mTrackTitle.setText(Constants.generateUIFriendlyString(mSelectedTrack.getTitle()));

                if (mSelectedTrack.getArtworkURL() == null)
                    mAlbumCoverArt.setImageResource(R.drawable.placeholder);
                else
                    Picasso.with(getContext()).load(mSelectedTrack.getArtworkURL()).into(mAlbumCoverArt);

                getUserInfo(mSelectedTrack.getUser().getId());
            }
        }

        GridLayoutManager mTagGridLayoutManager = new GridLayoutManager(getContext(), Constants.GRID_SPAN_COUNT);
        mMixerTags.setLayoutManager(mTagGridLayoutManager);
        mMixerTags.setAdapter(mMixTagAdapter);

        getLoaderManager().initLoader(Constants.MIX_TAGS_LOADER, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

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

        mIsAlive = true;
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
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
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
        Bundle settingsBundle = new Bundle();
        settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE, Constants.SyncDataType.Mixes.getCode());
        settingsBundle.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, mSelectedTrack);

        switch (v.getId()) {
            case R.id.play_song_button:
                //Start our audio service
                Intent audioService = new Intent(getContext(), AudioService.class);
                audioService.putExtra("StartedTrackId", mSelectedTrack.getID());
                getContext().startService(audioService);

                if (mBound) {
                    if(mAudioService.requestAudioFocus(getContext())) { //make sure are audio focus request returns true before playback
                        if (mAudioService.getIsPlaying()) {
                            mAudioService.pauseSong();
                            mPlaySongButton.setImageResource(R.drawable.ic_play_circle);
                        } else {
                            mPlaySongButton.setImageResource(R.drawable.ic_pause_circle);
                            if(mSelectedTrack.getStreamURL() != null)
                                mAudioService.playSong(Uri.parse(mSelectedTrack.getStreamURL()));
                            startProgressBarThread();
                            mAudioService.setRunInForeground();
                            if(mLooping){
                                mAudioService.setSongLooping(true);
                            }
                        }
                    }
                }
                break;
            case R.id.arrow_down:
                BeatLearner.getInstance(getContext()).downVoteTrack(mSelectedTrack.getID()); // downvote this track
                mAudioService.loadNextTrack();

                Snackbar downVoteSnack;
                downVoteSnack = Snackbar.make(((MainActivity) getActivity()).mCoordinatorLayout, getString(R.string.downvote_track), Snackbar.LENGTH_LONG);
                downVoteSnack.show();
                break;
            case R.id.skip_forward_button:
                mAudioService.loadNextTrack();
                break;
            case R.id.repeat_button:
                if (mBound) {
                    if(mAudioService.getIsPlaying()){
                        if (!mAudioService.getIsLooping()) {
                            mAudioService.setSongLooping(true);
                            mLoopSongButton.setImageResource(R.drawable.ic_repeat);
                        } else {
                            mAudioService.setSongLooping(false);
                            mLoopSongButton.setImageResource(R.drawable.ic_repeat_off);
                        }
                    } else {
                        if(!mLooping) {
                            mLoopSongButton.setImageResource(R.drawable.ic_repeat);
                            mLooping = true;
                        } else {
                            mLoopSongButton.setImageResource(R.drawable.ic_repeat_off);
                            mLooping = false;
                        }
                    }
                }
                break;
            case R.id.arrow_up:
                BeatLearner.getInstance(getContext()).upVoteTrack(mSelectedTrack.getID()); // upvote this track

                Snackbar upvoteSnack;
                upvoteSnack = Snackbar.make(((MainActivity) getActivity()).mCoordinatorLayout, getString(R.string.upvote_track), Snackbar.LENGTH_LONG);
                upvoteSnack.show();
                break;
            case R.id.floating_action_button_track_options:
                animateFAB();
                break;
            case R.id.floating_action_button_add_to_library:
                settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_ACTION, Constants.SyncDataAction.UpdateMix.getCode());
                OfflineSyncManager.getInstance(getContext()).performSyncOnLocalDb(((MainActivity) getActivity()).mCoordinatorLayout, settingsBundle, getActivity().getContentResolver());
                animateFAB();
                break;
            case R.id.floating_action_favorite:
                settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_ACTION, Constants.SyncDataAction.UpdateFavorite.getCode());
                OfflineSyncManager.getInstance(getContext()).performSyncOnLocalDb(((MainActivity) getActivity()).mCoordinatorLayout, settingsBundle, getActivity().getContentResolver());
                animateFAB();
                break;
            case R.id.floating_follow_artist:
                settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE, Constants.SyncDataType.Users.getCode());
                settingsBundle.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, mSelectedTrack);
                OfflineSyncManager.getInstance(getContext()).performSyncOnLocalDb(((MainActivity) getActivity()).mCoordinatorLayout, settingsBundle, getActivity().getContentResolver());
                animateFAB();
                break;
            default:
                break;
        }
    }

    public void startProgressBarThread() {
        int trackDuration = mSelectedTrack.getDuration();
        mPlayTrackSeekBar.setMax(trackDuration);
        mPlayTrackSeekBar.setIndeterminate(false);
        mUpdateSeekBar = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressStatus < trackDuration && mIsAlive) {
                    try {
                        Thread.sleep(1000); //Update once per second
                        mProgressStatus = mAudioService.getPlayerPosition();
                        mPlayTrackSeekBar.setProgress(mProgressStatus);
                        mPlayTrackSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    mAudioService.seekPlayerTo(progress);
                                }
                            }
                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {}
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {}
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.i("Progress bar thread", "Exception occured" + e.toString());
                        mIsAlive = false;
                    }
                }
            }
        });
        mUpdateSeekBar.start();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.MIX_TAGS_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        BrainBeatsContract.MixTagEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        BrainBeatsContract.MixTagEntry.COLUMN_NAME_MIX_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL, // where the mix is in the li,
                        new String[]{String.valueOf(mSelectedTrack.getID())},         // No selection arguments
                        BrainBeatsDbHelper.DB_SORT_TYPE_LIMIT_FIVE                   // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMixTagAdapter = new MixTagAdapter(getContext(), data);
        mMixerTags.setAdapter(mMixTagAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(mMixTagAdapter != null)
            mMixTagAdapter.swapCursor(null);

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

            if(mAudioService.getIsPlaying()){
                mAudioService.stopSong();
                mPlayTrackSeekBar.setProgress(0);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public void animateFAB() {
        if (mIsFabOpen) {
            mTrackOptionsFab.startAnimation(rotate_backward);
            mAddToLibraryFab.startAnimation(fab_close);
            mFavFab.startAnimation(fab_close);
            mFollowArtistFab.startAnimation(fab_close);
            mAddToLibraryFab.setClickable(false);
            mFavFab.setClickable(false);
            mFollowArtistFab.setClickable(false);
            mIsFabOpen = false;
        } else {
            mTrackOptionsFab.startAnimation(rotate_forward);
            mAddToLibraryFab.startAnimation(fab_open);
            mFavFab.startAnimation(fab_open);
            mFollowArtistFab.startAnimation(fab_open);
            mAddToLibraryFab.setClickable(true);
            mFavFab.setClickable(true);
            mFollowArtistFab.setClickable(true);
            mIsFabOpen = true;
        }
    }

    public void updateTrackUI(Track track){
        mTrackTitle.setText(track.getTitle());
        if (track.getArtworkURL() == null)
            mAlbumCoverArt.setImageResource(R.drawable.placeholder);
        else
            Picasso.with(getContext()).load(track.getArtworkURL()).into(mAlbumCoverArt);
        if (mBound) {
            if (track.getStreamURL() != null) {
                mPlaySongButton.setImageResource(R.drawable.ic_pause_circle);
                startProgressBarThread();
            }
        }
       // getUserInfo(track.getUser().getId());
    }

    public void getUserInfo(int userId){
        WebApiManager.getSoundCloudUser(getContext(), String.valueOf(userId), new WebApiManager.OnObjectResponseListener() {
            @Override
            public void onObjectResponse(JSONObject object) {
                Gson gson = new Gson();
                Type token = new TypeToken<User>() {}.getType();
                User soundCloudUser = gson.fromJson(object.toString(), token);
                mArtistName.setText(soundCloudUser.getUsername());
                Picasso.with(getContext()).load(soundCloudUser.getAvatarUrl()).into(mArtistThumbnail);
                mArtistDescription.setText(soundCloudUser.getDescription());
            }
        }, new WebApiManager.OnErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}