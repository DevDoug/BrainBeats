package com.brainbeats.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.brainbeats.LoginActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.brainbeats.adapters.MixTagAdapter;
import com.brainbeats.architecture.AccountManager;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.data.BrainBeatsDbHelper;
import com.brainbeats.entity.Track;
import com.brainbeats.entity.User;
import com.brainbeats.service.AudioService;
import com.brainbeats.sync.OfflineSyncManager;
import com.brainbeats.utils.BeatLearner;
import com.brainbeats.utils.Constants;
import com.brainbeats.web.WebApiManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;

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

    public Bundle mUserSelections;
    private volatile boolean mIsAlive = true;

    // Playing track members.
    public Thread mUpdateSeekBar;
    private SeekBar mPlayTrackSeekBar;
    public int mProgressStatus = 0;

    //Playing song members.
    public Track mSelectedTrack;
    private boolean mLooping = false;

    //TODO - implement in version 2.0 beta version
/*    private MixTagAdapter mMixTagAdapter;
    private RecyclerView mMixerTags;*/

    //Interfaces.
    private OnFragmentInteractionListener mListener;

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
    public void onPause() {
        super.onPause();
        if (mUpdateSeekBar != null)
            mUpdateSeekBar.interrupt(); // stop updating a the progress bar

        if (((MainActivity) getActivity()).mAudioService.getIsPlaying() || ((MainActivity) getActivity()).mAudioService.mIsPaused) {
            AccountManager.getInstance(getContext()).setDisplayCurrentSongView(true);
            ((MainActivity) getActivity()).mCurrentSongPlayingView.setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).mCurrentSong = mSelectedTrack;
            ((MainActivity) getActivity()).updateCurrentSongNotificationUI();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_detail, container, false);
        //mMixerTags = (RecyclerView) v.findViewById(R.id.mix_tag_grid);    //TODO - implement in version 2.0 beta version

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

        //TODO - implement in version 2.0 beta version
        /*
        GridLayoutManager mTagGridLayoutManager = new GridLayoutManager(getContext(), Constants.GRID_SPAN_COUNT);
        mMixerTags.setLayoutManager(mTagGridLayoutManager);
        mMixerTags.setAdapter(mMixTagAdapter);
        getLoaderManager().initLoader(Constants.MIX_TAGS_LOADER, null, this);
        */
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

                if (((MainActivity) getActivity()).mIsFabOpen)
                    ((MainActivity) getActivity()).animateFAB();

                ((MainActivity) getActivity()).mMainActionFab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_filter_list_white));
                ((MainActivity) getActivity()).mExtraActionOneFab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_whatshot_white));
                ((MainActivity) getActivity()).mExtraActionTwoFab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_access_time_white));
                ((MainActivity) getActivity()).mExtraActionThreeFab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_sort_by_alpha_white));

                FragmentManager fm = getActivity().getSupportFragmentManager();
                ((MainActivity) getActivity()).navigateUpOrBack(getActivity(), fm);
            }
        });

        mIsAlive = true;
        ((MainActivity) getActivity()).mCurrentSongPlayingView.setVisibility(View.INVISIBLE); // hide our playing sound view
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard_detail, menu);
        //TODO - implement in version 2.0 beta version
        /*
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this track" + (mSelectedTrack == null ? mSelectedTrack.getTitle() : ""));
        shareIntent.setType("text/plain");
        mShareActionProvider.setShareIntent(shareIntent);
        */
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
                //Start our audio com.brainbeats.service
                if (!((MainActivity) getActivity()).isAudioServiceRunning(AudioService.class)) {
                    Intent audioService = new Intent(getContext(), AudioService.class);
                    audioService.setAction(AudioService.MAIN_ACTION);
                    audioService.putExtra(Constants.KEY_EXTRA_SELECTED_TRACK, mSelectedTrack);
                    getContext().startService(audioService);

                    ((MainActivity) getActivity()).mAudioService.setRunInForeground();

                    AccountManager.getInstance(getContext()).setDisplayCurrentSongView(true);
                }

                if (((MainActivity) getActivity()).mBound) {
                    if (((MainActivity) getActivity()).mAudioService.requestAudioFocus(getContext())) { //make sure are audio focus request returns true before playback
                        if (((MainActivity) getActivity()).mAudioService.getIsPlaying() && ((MainActivity) getActivity()).mAudioService.mPlayingSong.getID() == mSelectedTrack.getID()) { //if the current song is the one being played pause else start new song
                            ((MainActivity) getActivity()).mAudioService.pauseSong();
                            mPlaySongButton.setImageResource(R.drawable.ic_play_circle);
                        } else {
                            mPlaySongButton.setImageResource(R.drawable.ic_pause_circle);
                            if (mSelectedTrack.getStreamURL() != null) {
                                ((MainActivity) getActivity()).mAudioService.mPlayingSong = mSelectedTrack;
                                ((MainActivity) getActivity()).mAudioService.playSong(Uri.parse(mSelectedTrack.getStreamURL()));
                            }
                            startProgressBarThread();

                            if (mLooping) {
                                ((MainActivity) getActivity()).mAudioService.setSongLooping(true);
                            }
                        }
                    }
                }
                break;
            case R.id.arrow_down:
                BeatLearner.getInstance(getContext()).downVoteTrack(mSelectedTrack.getID()); // downvote this track
                ((MainActivity) getActivity()).mAudioService.loadNextTrack();

                Snackbar downVoteSnack;
                downVoteSnack = Snackbar.make(((MainActivity) getActivity()).mCoordinatorLayout, getString(R.string.downvote_track), Snackbar.LENGTH_LONG);
                downVoteSnack.show();
                break;
            case R.id.skip_forward_button:
                ((MainActivity) getActivity()).mAudioService.loadNextTrack();
                break;
            case R.id.repeat_button:
                if (((MainActivity) getActivity()).mBound) {
                    if (((MainActivity) getActivity()).mAudioService.getIsPlaying()) {
                        if (!((MainActivity) getActivity()).mAudioService.getIsLooping()) {
                            ((MainActivity) getActivity()).mAudioService.setSongLooping(true);
                            mLoopSongButton.setImageResource(R.drawable.ic_repeat);
                        } else {
                            ((MainActivity) getActivity()).mAudioService.setSongLooping(false);
                            mLoopSongButton.setImageResource(R.drawable.ic_repeat_off);
                        }
                    } else {
                        if (!mLooping) {
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
                while ((mProgressStatus < trackDuration) && mIsAlive) {
                    try {
                        Thread.sleep(1000); //Update once per second
                        mProgressStatus = ((MainActivity) getActivity()).mAudioService.getPlayerPosition();
                        mPlayTrackSeekBar.setProgress(mProgressStatus);
                        mPlayTrackSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    ((MainActivity) getActivity()).mAudioService.seekPlayerTo(progress);
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
                        Log.i("Progress bar thread", "Exception occured" + e.toString());
                        mIsAlive = false;
                        mPlayTrackSeekBar.setProgress(0);
                    }
                }
            }
        });
        mUpdateSeekBar.start();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            //TODO - implement in version 2.0 beta version
/*            case Constants.MIX_TAGS_LOADER:
                return new CursorLoader(
                        getActivity(),                                          // Parent activity context
                        BrainBeatsContract.MixTagEntry.CONTENT_URI,             // Table to query
                        null,                                                   // Projection to return
                        BrainBeatsContract.MixTagEntry.COLUMN_NAME_MIX_ID +
                                BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,          // Where the mix is in the li,
                        new String[]{String.valueOf(mSelectedTrack.getID())},   // No selection arguments
                        BrainBeatsDbHelper.DB_SORT_TYPE_LIMIT_FIVE              // Default sort order
                );*/
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //TODO - implement in version 2.0 beta version
        /*
        mMixTagAdapter = new MixTagAdapter(getContext(), data);
        mMixerTags.setAdapter(mMixTagAdapter);
        */
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //TODO - implement in version 2.0 beta version
        /*
        if (mMixTagAdapter != null)
        mMixTagAdapter.swapCursor(null);
        */
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void updateTrackUI(Track track) {
        mSelectedTrack = track;
        mTrackTitle.setText(track.getTitle());
        if (track.getArtworkURL() == null)
            mAlbumCoverArt.setImageResource(R.drawable.placeholder);
        else
            Picasso.with(getContext()).load(track.getArtworkURL()).into(mAlbumCoverArt);
        if (((MainActivity) getActivity()).mBound) {
            if (track.getStreamURL() != null) {
                mPlaySongButton.setImageResource(R.drawable.ic_pause_circle);
                //startProgressBarThread();
            }
        }

        mArtistName.setText(track.getUser().getUsername());
        Picasso.with(getContext()).load(track.getUser().getAvatarUrl()).into(mArtistThumbnail);
        mArtistDescription.setText(track.getUser().getDescription());
    }

    public void getUserInfo(int userId) {
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

    public void updateOfflineSyncManager(Constants.SyncDataAction syncAction, Constants.SyncDataType syncDataType) {
        Bundle settingsBundle = new Bundle();

        if (syncAction != null)
            settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_ACTION, syncAction.getCode());
        if (syncDataType != null)
            settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE, syncDataType.getCode());

        settingsBundle.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, mSelectedTrack);
        OfflineSyncManager.getInstance(getContext()).performSyncOnLocalDb(((MainActivity) getActivity()).mCoordinatorLayout, settingsBundle, getActivity().getContentResolver());
    }
}