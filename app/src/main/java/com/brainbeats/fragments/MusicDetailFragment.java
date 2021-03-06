package com.brainbeats.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brainbeats.LoginActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.brainbeats.adapters.MixTagAdapter;
import com.brainbeats.architecture.AccountManager;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.data.BrainBeatsDbHelper;
import com.brainbeats.entity.Track;
import com.brainbeats.model.Mix;
import com.brainbeats.sync.OfflineSyncManager;
import com.brainbeats.utils.BeatLearner;
import com.brainbeats.utils.Constants;
import com.squareup.picasso.Picasso;

public class MusicDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static final String TAG = "MusicDetailFragment";
    private static final int RESULT_PICK_CONTACT = 1;

    private TextView mTrackTitle;
    private TextView mArtistDescription;
    private ImageView mAlbumCoverArt;
    public ImageView mPlaySongButton;
    private ImageView mUpvoteArrow;
    private ImageView mDownVoteArrow;
    private ImageView mSkipForwardButton;
    private ImageView mLoopSongButton;
    private TextView mArtistName;
    private ImageView mArtistThumbnail;
    private TextView mShowArtistDetails;
    private CardView mArtistCard;

    public Bundle mUserSelections;
    public volatile boolean mIsAlive = true;
    public boolean isCurrentSong = false;
    public boolean isRepeating = false;

    // Playing track members.
    public Thread mUpdateSeekBar;
    private SeekBar mPlayTrackSeekBar;
    public int mProgressStatus = 0;
    public boolean mProgressUpdating = false;

    //Playing song members.
    public Mix mSelectedTrack;
    private boolean mLooping = false;
    public ProgressDialog loadingMusicDialog;

    private ActionProvider mShareActionProvider;


    //TODO - implement in version 2.0 beta version
    private MixTagAdapter mMixTagAdapter;
    private RecyclerView mMixerTags;

    //Interfaces.
    private OnFragmentInteractionListener mListener;

    public MusicDetailFragment() {
        // Required empty public constructor
    }

    public static MusicDetailFragment newInstance(Mix mix) {
        MusicDetailFragment musicFrag = new MusicDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, mix);
        musicFrag.setArguments(args);

        return musicFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mUserSelections = getArguments();
        if (mUserSelections != null) {
            mSelectedTrack = (Mix) mUserSelections.get(Constants.KEY_EXTRA_SELECTED_TRACK);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mSelectedTrack != null) {
                mTrackTitle.setText(Constants.generateUIFriendlyString(mSelectedTrack.getMixTitle()));
                if (mSelectedTrack.getMixAlbumCoverArt() == null)
                    mAlbumCoverArt.setImageResource(R.drawable.no_cover);
                else
                    Picasso.with(getContext()).load(mSelectedTrack.getMixAlbumCoverArt()).into(mAlbumCoverArt);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //mListener.onFragmentInteraction(Constants.DASHBOARD_DETAIL_CHECK_IF_CURRENT_SONG);
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

        mPlaySongButton.setImageResource(R.drawable.ic_play_circle);
        ((MainActivity) getActivity()).mAudioService.stopSong();

        if (mUpdateSeekBar != null)
            mUpdateSeekBar.interrupt(); // stop updating the progress bar
    }

    @Override
    public void onStop() {
        super.onStop();
        try{
/*            if(mSelectedTrack != null)
                mListener.onFragmentInteraction(Constants.DASHBOARD_DETAIL_UPDATE_CURRENT_PLAYING_SONG_VIEW, mSelectedTrack);*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_music_detail, container, false);
        //mMixerTags = (RecyclerView) v.findViewById(R.id.mix_tag_grid);    //TODO - implement in version 2.0 beta version

        mTrackTitle = v.findViewById(R.id.mix_title);
        mArtistDescription = v.findViewById(R.id.artist_description);
        mAlbumCoverArt = v.findViewById(R.id.album_cover_art);
        mPlaySongButton = v.findViewById(R.id.play_song_button);
/*        mUpvoteArrow = v.findViewById(R.id.arrow_up);
        mDownVoteArrow = v.findViewById(R.id.arrow_down);
        mSkipForwardButton = v.findViewById(R.id.skip_forward_button);
        mLoopSongButton = v.findViewById(R.id.repeat_button);*/
        mArtistThumbnail = v.findViewById(R.id.artist_thumbnail);
        mPlayTrackSeekBar = v.findViewById(R.id.play_song_seek_bar);
/*        mArtistName = v.findViewById(R.id.user_name);
        mShowArtistDetails = v.findViewById(R.id.artist_detail_toggle_switch);
        mArtistCard = v.findViewById(R.id.card_view);*/

        mPlaySongButton.setOnClickListener(this);
/*        mUpvoteArrow.setOnClickListener(this);
        mDownVoteArrow.setOnClickListener(this);
        mSkipForwardButton.setOnClickListener(this);
        mLoopSongButton.setOnClickListener(this);*/
//        mShowArtistDetails.setOnClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
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
                //((MainActivity) getActivity()).mExtraActionThreeFab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_sort_by_alpha_white));
                //((MainActivity) getActivity()).mExtraActionFourFab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_sort_by_alpha_white));

                mListener.onFragmentInteraction(Constants.DASHBOARD_DETAIL_LOAD_DASHBOARD_FAB_IMAGES);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ((MainActivity) getActivity()).navigateUpOrBack(getActivity(), fm);
            }
        });

        //((MainActivity) getActivity()).mCurrentSongPlayingView.setVisibility(View.INVISIBLE); // hide our playing sound view

/*        if (isCurrentSong)
            mListener.onFragmentInteraction(Constants.DASHBOARD_DETAIL_UPDATE_PROGRESS_BAR_THREAD);*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
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
                playSong();
                break;
/*            case R.id.arrow_down:
                downvoteSong();
                break;
            case R.id.skip_forward_button:
                skipForward();
                break;
            case R.id.repeat_button:
                turnOnRepeat();
                break;
            case R.id.arrow_up:
                upvoteSong();
                break;*/
/*            case R.id.artist_detail_toggle_switch:
                showArtistDetails();
                break;*/
            default:
                break;
        }
    }

    public void startProgressBarThread(int position) {
        int trackDuration = mSelectedTrack.getDuration();
        mPlayTrackSeekBar.setProgress(position);
        mPlayTrackSeekBar.setMax(trackDuration);
        mPlayTrackSeekBar.setIndeterminate(false);
        mIsAlive = true;

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
                            public void onStartTrackingTouch(SeekBar seekBar) {}

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {}
                        });
                    } catch (InterruptedException e) {
                        Log.i("Progress bar thread", "Exception occured" + e.toString());
                        mIsAlive = false;
                        mPlayTrackSeekBar.setProgress(0);
                    } catch (Exception ex) {
                        ex.printStackTrace();
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
            case Constants.MIX_TAGS_LOADER:
                return new CursorLoader(
                        getActivity(),                                          // Parent activity context
                        BrainBeatsContract.MixTagEntry.CONTENT_URI,             // Table to query
                        null,                                                   // Projection to return
                        BrainBeatsContract.MixTagEntry.COLUMN_NAME_MIX_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,          // Where the mix is in the li,
                        new String[]{String.valueOf(mSelectedTrack.getArtistId())},   // No selection arguments
                        BrainBeatsDbHelper.DB_SORT_TYPE_LIMIT_FIVE              // Default sort order
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //TODO - implement in version 2.0 beta version
        mMixTagAdapter = new MixTagAdapter(getContext(), data);
        mMixerTags.setAdapter(mMixTagAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //TODO - implement in version 2.0 beta version
        if (mMixTagAdapter != null)
            mMixTagAdapter.swapCursor(null);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void onFragmentInteraction(Uri uri, Mix mix);
    }

    public void updateTrackUI(Mix mix) {
        mSelectedTrack = mix;
        mTrackTitle.setText(mSelectedTrack.getMixTitle());

        if (mSelectedTrack.getMixAlbumCoverArt() == null)
            mAlbumCoverArt.setImageResource(R.drawable.no_cover);
        else
            Picasso.with(getContext()).load(mSelectedTrack.getMixAlbumCoverArt()).into(mAlbumCoverArt);

        if (((MainActivity) getActivity()).mBound) {
            mPlaySongButton.setImageResource(R.drawable.ic_pause_circle);
            startProgressBarThread(0);
        }

/*        mArtistName.setText(mSelectedTrack.getUser().getArtistName());
        Picasso.with(getContext()).load(mSelectedTrack.getUser().getArtistProfileImage()).into(mArtistThumbnail);
        mArtistDescription.setText(mSelectedTrack.getUser().getArtistDescription());*/

        if (loadingMusicDialog != null)
            loadingMusicDialog.dismiss();
    }
/*
    public void updateOfflineSyncManager(Constants.SyncDataAction syncAction, Constants.SyncDataType syncDataType) {
        Bundle settingsBundle = new Bundle();
        if (syncAction != null)
            settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_ACTION, syncAction.getCode());
        if (syncDataType != null)
            settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE, syncDataType.getCode());

        settingsBundle.putParcelable(Constants.KEY_EXTRA_SELECTED_TRACK, mSelectedTrack);
        OfflineSyncManager.getInstance(getContext()).performSyncOnLocalDb(((MainActivity) getActivity()).mCoordinatorLayout, settingsBundle, getActivity().getContentResolver());
    }*/

    public void showLoadingMusicDialog(){
        loadingMusicDialog = new ProgressDialog(getContext());
        loadingMusicDialog.setCancelable(false);
        loadingMusicDialog.setMessage(getString(R.string.loading_message));
        loadingMusicDialog.show();
    }

    public void playSong() {
        MainActivity main = ((MainActivity) getActivity());

        if (!main.mAudioService.getIsPlaying() && !main.mAudioService.getIsPaused()
                || !(main.mAudioService.getPlayingSong().getMixId().equals(mSelectedTrack.getArtistId()))) { //if music not playing or not the current playing song
            showLoadingMusicDialog();
            mListener.onFragmentInteraction(Constants.DASHBOARD_DETAIL_LOAD_SONG_URI, mSelectedTrack);
        } else if (main.mAudioService.getIsPlaying()) {                                                        //Song is playing so pause song
            mPlaySongButton.setImageResource(R.drawable.ic_play_circle);
            mListener.onFragmentInteraction(Constants.DASHBOARD_DETAIL_PAUSE_SONG_URI);
        } else {                                                                                            //Song is paused so resume
            mPlaySongButton.setImageResource(R.drawable.ic_pause_circle);
            mListener.onFragmentInteraction(Constants.DASHBOARD_DETAIL_PLAY_SONG_URI);
        }
    }

    public void downvoteSong() {
        showLoadingMusicDialog();
        BeatLearner.getInstance(getContext()).downVoteTrack(Integer.parseInt(mSelectedTrack.getMixId()));
        mListener.onFragmentInteraction(Constants.DASHBOARD_DETAIL_DOWNVOTE_SONG_URI, mSelectedTrack);

        Snackbar downVoteSnack;
        downVoteSnack = Snackbar.make(((MainActivity) getActivity()).mCoordinatorLayout, getString(R.string.downvote_track), Snackbar.LENGTH_LONG);
        downVoteSnack.show();
    }

    public void upvoteSong(){
        BeatLearner.getInstance(getContext()).upVoteTrack(Integer.parseInt(mSelectedTrack.getMixId()));

        Snackbar upvoteSnack;
        upvoteSnack = Snackbar.make(((MainActivity) getActivity()).mCoordinatorLayout, getString(R.string.upvote_track), Snackbar.LENGTH_LONG);
        upvoteSnack.show();
    }

    public void skipForward(){
        showLoadingMusicDialog();
        mListener.onFragmentInteraction(Constants.DASHBOARD_DETAIL_SKIP_FORWARD_URI, mSelectedTrack);
    }

    public void turnOnRepeat() {
        isRepeating = !isRepeating;

        if(isRepeating)
            mLoopSongButton.setImageResource(R.drawable.ic_repeat);
        else
            mLoopSongButton.setImageResource(R.drawable.ic_repeat_off);

        mListener.onFragmentInteraction(Constants.DASHBOARD_DETAIL_SET_SONG_REPEAT_URI);
    }

    public void showArtistDetails(){
        mShowArtistDetails.setVisibility(View.GONE);
        mArtistCard.setVisibility(View.VISIBLE);
    }
}