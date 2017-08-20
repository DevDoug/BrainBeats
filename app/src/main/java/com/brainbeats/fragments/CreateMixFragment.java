package com.brainbeats.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brainbeats.LoginActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;
import com.brainbeats.architecture.AccountManager;
import com.brainbeats.utils.Constants;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateMixFragment extends Fragment implements View.OnClickListener{

    private static final String LOG_TAG = "AudioRecordTest";
    private OnFragmentInteractionListener mListener;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private TextView mInstructionText;
    private ImageView mRecordButton;
    private ImageView mPlayRecordingButton;
    private ImageView mSaveSong;
    private ImageView mRerecordSong;
    private SeekBar mPlayRecordingSeekBar;
    private LinearLayout mRecordingOptions;
    RelativeLayout mMaestroPanel;
    RelativeLayout mRecordingContainer;
    private Button mDoneButton;

    private MediaRecorder mRecorder = null;
    private boolean mIsRecording = false;

    private Button mPlayButton = null;
    private MediaPlayer mPlayer = null;

    private PowerManager.WakeLock recordingWakeLock;


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void onFragmentInteraction(Uri uri, String source);
    }

    public CreateMixFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setHasOptionsMenu(true);

        // Record to the external cache directory for visibility
        mFileName = getActivity().getExternalCacheDir().getAbsolutePath();
        mFileName += "/" + getString(R.string.temperary_file_name) + ".3gp";
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_mix, container, false);
        mInstructionText = (TextView) v.findViewById(R.id.instruction_text);
        mRecordButton = (ImageView) v.findViewById(R.id.start_stop_recording);
        mPlayRecordingButton = (ImageView) v.findViewById(R.id.play_song_button);
        mSaveSong = (ImageView) v.findViewById(R.id.save_button);
        mRerecordSong = (ImageView) v.findViewById(R.id.restart);
        mPlayRecordingSeekBar = (SeekBar) v.findViewById(R.id.play_recording_seek_bar);
        mRecordingOptions = (LinearLayout) v.findViewById(R.id.button_panel);
        mMaestroPanel = (RelativeLayout) v.findViewById(R.id.maestro_panel);
        mRecordingContainer = (RelativeLayout) v.findViewById(R.id.recording_container);
        mDoneButton = (Button) v.findViewById(R.id.maestro_done);

        mRecordButton.setOnClickListener(this);
        mPlayRecordingButton.setOnClickListener(this);
        mSaveSong.setOnClickListener(this);
        mRerecordSong.setOnClickListener(this);
        mDoneButton.setOnClickListener(this);
        return v;
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
                ((MixerActivity) getActivity()).mMainActionFab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_add_white));
                mListener.onFragmentInteraction(Constants.MIX_SHOW_FAB);
                mIsRecording = false;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ((MixerActivity) getActivity()).navigateUpOrBack(getActivity(), fm);
            }
        });

        mListener.onFragmentInteraction(Constants.NEW_MIX_HIDE_FAB);
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mIsRecording)
            stopRecording();

        mListener.onFragmentInteraction(Constants.STOP_SONG_URI);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_stop_recording:
                if(mIsRecording)
                    showPlaybackRecordingView();

                onRecord(!mIsRecording);
                break;
            case R.id.maestro_done:
                dismissMaestro();
                break;
/*            case R.id.play_song_button:
                mListener.onFragmentInteraction(Constants.LOAD_SONG_URI, mFileName);
                break;
            case R.id.save_button:
                mListener.onFragmentInteraction(Constants.NEW_MIX_LOAD_CONFIRM_FRAG, mFileName);
                break;
            case R.id.restart:
                showRecordingView();
                break;*/
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_global, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AccountManager.getInstance(getContext()).forceLogout(getContext());
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
        mIsRecording = true;
        mRecordButton.setImageResource(R.drawable.stop);
        showRecordingView();
    }

    private void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            mIsRecording = false;
            mRecordButton.setImageResource(R.drawable.ic_mic);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void showRecordingView(){
        mInstructionText.setText(R.string.start_recording_text);
        mRecordButton.setVisibility(View.VISIBLE);
        mPlayRecordingSeekBar.setVisibility(View.INVISIBLE);
        mRecordingOptions.setVisibility(View.INVISIBLE);
    }

    public void showPlaybackRecordingView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        View dialogView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.audio_playback_dialog, null);
        builder.setView(dialogView);
        ((ImageView) dialogView.findViewById(R.id.play_song_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(Constants.LOAD_SONG_URI, mFileName);
            }
        });

        builder.setPositiveButton("Sounds Good", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onFragmentInteraction(Constants.NEW_MIX_LOAD_CONFIRM_FRAG, mFileName);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Rerecord", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onFragmentInteraction(Constants.STOP_SONG_URI);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void summonMaestro() {
        mListener.onFragmentInteraction(Constants.NEW_MIX_HIDE_FAB);
        mRecordingContainer.animate().alpha(0.0f).withEndAction(new Runnable() {
            @Override
            public void run() {
                mMaestroPanel.animate().alpha(1.0f).withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        mMaestroPanel.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    public void dismissMaestro(){
        mMaestroPanel.animate().alpha(0.0f).withEndAction(new Runnable() {
            @Override
            public void run() {
                mRecordingContainer.animate().alpha(1.0f).withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onFragmentInteraction(Constants.MIX_SHOW_FAB);
                        mMaestroPanel.setVisibility(View.GONE);
                    }
                });
            }
        });

    }
}