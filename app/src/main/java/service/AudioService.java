package service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.brainbeats.MainActivity;
import com.brainbeats.R;

import utils.Constants;
import web.WebApiManager;

public class AudioService extends Service implements MediaPlayer.OnPreparedListener,  MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    public static MediaPlayer mPlayer;

    public static int FOREGROUND_SERVICE = 101;
    public static String MAIN_ACTION = "com.brainbeats.foregroundservice.action.main";
    public static String PREV_ACTION = "com.brainbeats.foregroundservice.action.prev";
    public static String PLAY_ACTION = "com.brainbeats.foregroundservice.action.play";
    public static String NEXT_ACTION = "com.brainbeats.foregroundservice.action.next";
    public static String STARTFOREGROUND_ACTION = "com.brainbeats.foregroundservice.action.startforeground";
    public static String STOPFOREGROUND_ACTION = "com.brainbeats.foregroundservice.action.stopforeground";

    private IBinder mBinder = new AudioBinder();
    public boolean mIsPaused = false;

    public AudioService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp.isLooping()) {
            mp.seekTo(0);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
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
        if (mIsPaused) {
            mPlayer.start();
        } else {
            try {
                mPlayer.reset();
                mPlayer.setDataSource(getApplicationContext(), songPath.buildUpon().appendQueryParameter(WebApiManager.SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID).build());
                mPlayer.prepareAsync();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void pauseSong() {
        mPlayer.pause();
        mIsPaused = true;
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

    public boolean getIsLooping(){
        return mPlayer.isLooping();
    }

    public void seekPlayerTo(int pos){ mPlayer.seekTo(pos);}

    public int getPlayerPosition(){
        return mPlayer.getCurrentPosition();
    }

    public void setRunInForeground(){

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent previousIntent = new Intent(this, AudioService.class);
        previousIntent.setAction(PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, AudioService.class);
        playIntent.setAction(PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

        Intent nextIntent = new Intent(this, AudioService.class);
        nextIntent.setAction(NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_album);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("BrainBeats Music Player")
                .setTicker("BrainBeats Music Player")
                .setContentText("My Music")
                .setSmallIcon(R.drawable.ic_music_note_black)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_previous, "Previous", ppreviousIntent)
                .addAction(android.R.drawable.ic_media_play, "Play", pplayIntent)
                .addAction(android.R.drawable.ic_media_next, "Next", pnextIntent).build();
        startForeground(FOREGROUND_SERVICE, notification);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            // Pause playback
            pauseSong();
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // Resume playback
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            //am.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
            //am.abandonAudioFocus(afChangeListener);
            // Stop playback
        }
    }

    public class AudioBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

    public boolean requestAudioFocus(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
            return true;
        else
            return false;

    }
}
