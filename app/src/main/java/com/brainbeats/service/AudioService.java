package com.brainbeats.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.brainbeats.model.Mix;
import com.brainbeats.utils.BeatLearner;
import com.brainbeats.utils.Constants;
import com.brainbeats.web.WebApiManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Queue;

import static com.brainbeats.utils.Constants.KEY_EXTRA_SELECTED_TRACK;

/*Audio com.brainbeats.service should handle playing all music, should be a bound com.brainbeats.service and a started com.brainbeats.service which will allow us to bind to com.brainbeats.ui and keep the music in the background when not on the detail activity*/
public class AudioService extends IntentService implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener, BeatLearner.RecommendationCompleteListener {

    public static MediaPlayer mPlayer;
    public static Mix mPlayingSong;
    public static boolean mIsPaused = false;
    public boolean mIsRecordingTest = false;
    public boolean mIsPlayingPlaylist = false;
    public Queue<Mix> mPlaylistSongs;

    public static int FOREGROUND_SERVICE = 101;
    public static String MAIN_ACTION = "com.brainbeats.foregroundservice.action.main";
    public static String DOWNVOTE_ACTION = "com.brainbeats.foregroundservice.action.prev";
    public static String PLAY_ACTION = "com.brainbeats.foregroundservice.action.play";
    public static String NEXT_ACTION = "com.brainbeats.foregroundservice.action.next";
    public static String STARTFOREGROUND_ACTION = "com.brainbeats.foregroundservice.action.startforeground";
    public static String STOPFOREGROUND_ACTION = "com.brainbeats.foregroundservice.action.stopforeground";

    private IBinder mBinder = new AudioBinder();

    public AudioService() {
        super("Audio Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

/*        if (intent.getAction().equals(MAIN_ACTION)) {
            mPlayingSong = intent.getExtras().getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
        } else if (intent.getAction().equals(PLAY_ACTION)) {
            //TODO play/pause song
        } else if (intent.getAction().equals(NEXT_ACTION)) {
            loadNextTrack();
        } else if (intent.getAction().equals(DOWNVOTE_ACTION)) {
            //TODO downvote this track
            loadNextTrack();
        }*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Bundle intentBundle = intent.getExtras(); //If an intent is passed to main activity.

        if (intentBundle != null) {
            if (intentBundle.get(KEY_EXTRA_SELECTED_TRACK) != null) {
                mPlayingSong = (Mix) intentBundle.get(KEY_EXTRA_SELECTED_TRACK);
            }
        }

        return mBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        try {
            mPlayer.start();

            //player is ready update ui
            if(!mIsRecordingTest) {
                Intent broadcastIntent = new Intent(); // send broadcast to activity to tell it to update com.brainbeats.ui
                broadcastIntent.setAction(Constants.SONG_DONE_LOADING_BROADCAST_ACTION);
                broadcastIntent.putExtra(Constants.KEY_EXTRA_SELECTED_TRACK, mPlayingSong);
                sendBroadcast(broadcastIntent);
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp.isLooping()) {
            mp.seekTo(0);
        } else {
            //tell ui to show loading spinner
            if (!mIsRecordingTest) {
                mp.seekTo(0);
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(Constants.SONG_COMPLETE_BROADCAST_ACTION);
                sendBroadcast(broadcastIntent);

/*                if (mIsPlayingPlaylist) {
                    if (mPlaylistSongs.size() > 0) {
                        mPlayingSong = mPlaylistSongs.peek();
                        playSong(Uri.parse(mPlaylistSongs.peek().getStreamURL()));
                        mPlaylistSongs.remove();
                    } else {
                        mIsPlayingPlaylist = false;
                        sendPlaylistCompleteBroadcast();
                    }
                } else {
                    loadNextTrack(); //load next track on com.brainbeats.service side
                }*/
            } else {
                mIsRecordingTest = false;
            }
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Constants.SONG_ERROR_BROADCAST_ACTION);
        sendBroadcast(broadcastIntent);
        return false;
    }

    public void initMediaPlayer(){
        if(mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
    }

    public void playSong(Uri songPath) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(getApplicationContext(), songPath.buildUpon().appendQueryParameter(WebApiManager.SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID).build());
            mPlayer.prepareAsync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playBrainBeatsSong(Uri songPath){
        try {
            mPlayer.reset();
            mPlayer.setDataSource(getApplicationContext(), songPath);
            mPlayer.prepareAsync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void pauseSong() {
        mPlayer.pause();
        mIsPaused = true;
    }

    public void resumeSong(){
        mPlayer.start();
        mIsPaused = false;
    }

    public void setPlayingSong(Mix mix) {
        mPlayingSong = mix;
    }

    public Mix getPlayingSong(){
        return mPlayingSong;
    }

    public void setPlaylist(Queue<Mix> playlist){
        this.mPlaylistSongs = playlist;
    }

    public void setSongLooping(boolean isLooping) {
        mPlayer.setLooping(isLooping);
    }

    public void stopSong() {
        mPlayer.stop();
    }

    public boolean getIsPlaying(){
        return mPlayer.isPlaying();
    }

    public boolean getIsPaused(){return mIsPaused;}

    public boolean getIsLooping(){
        return mPlayer.isLooping();
    }

    public void seekPlayerTo(int pos){ mPlayer.seekTo(pos);}

    public int getPlayerPosition() {
        return mPlayer.getCurrentPosition();
    }

    public void setRunInForeground(){

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent previousIntent = new Intent(this, AudioService.class);
        previousIntent.setAction(DOWNVOTE_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);

        Intent playIntent = new Intent(this, AudioService.class);
        playIntent.setAction(PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

        Intent nextIntent = new Intent(this, AudioService.class);
        nextIntent.setAction(NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_album);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("BrainBeats")
                .setTicker("BrainBeats Music Player")
                .setContentText("Title")
                .setSmallIcon(R.drawable.ic_music_note_black)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(R.drawable.ic_arrow_downward_black, "Skip", ppreviousIntent)
                .addAction(R.drawable.ic_play_circle, "Play", pplayIntent)
                .addAction(R.drawable.ic_skip_next, "Next", pnextIntent).build();
        startForeground(FOREGROUND_SERVICE, notification);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            pauseSong();             // Pause playback
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            resumeSong();
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            pauseSong();
        }
    }

    @Override
    public void recommendationComplete(Mix mix) {
/*        mPlayingSong = mix;
        mIsPaused = false; //unpause to load new track instead of resume old track

        playSong(Uri.parse(mix.getStreamURL()));*/
    }

    public class AudioBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

    public boolean requestAudioFocus(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    public void loadNextTrack(){
        try {
            BeatLearner.getInstance(getApplicationContext()).loadNextRecommendedBeat(Integer.parseInt(mPlayingSong.getMixId()), this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendPlaylistCompleteBroadcast(){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Constants.PLAYLIST_COMPLETE_BROADCAST_ACTION);
        sendBroadcast(broadcastIntent);
    }
}
