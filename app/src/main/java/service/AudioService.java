package service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.UiThread;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.brainbeats.R;

import utils.Constants;
import web.WebApiManager;

public class AudioService extends Service implements MediaPlayer.OnPreparedListener,  MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    public MediaPlayer mPlayer;
    private IBinder mBinder = new AudioBinder();
    public boolean mIsPaused = false;
    int mProgressStatus = 0;
    int mSongDuration = 0;

    public AudioService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
        mSongDuration = mp.getDuration();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mp.isLooping()) {
            mp.seekTo(0);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
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

    public void pauseSong(){
        mPlayer.pause();
        mIsPaused = true;
    }

    public void setSongLooping(boolean isLooping){
        mPlayer.setLooping(isLooping);
    }

    public void stopSong(){
        mPlayer.stop();
    }

    public class AudioBinder extends Binder{
        public AudioService getService() {return AudioService.this;}
    }
}
