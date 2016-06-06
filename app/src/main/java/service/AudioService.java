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
import android.widget.SeekBar;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

import utils.Constants;
import web.WebApiManager;

public class AudioService extends Service implements MediaPlayer.OnPreparedListener,  MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    public MediaPlayer mPlayer;
    private IBinder mBinder = new AudioBinder();
    public boolean mIsPaused = false;
    private Handler mHandler = new Handler();
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
        mp.start();
        mSongDuration = mp.getDuration();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.stop();
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
                mPlayer.setDataSource(getApplicationContext(), songPath.buildUpon().appendQueryParameter(WebApiManager.KEY_ClIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID).build());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mPlayer.prepareAsync();
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
    }

    public void setProgressIndicator(final SeekBar bar,final int duration) {
        bar.setMax(duration);
        bar.setIndeterminate(false);
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < duration) {
                    try {
                        Thread.sleep(1000); //Update once per second
                        mProgressStatus = mPlayer.getCurrentPosition();
                        bar.setProgress(mProgressStatus);
                        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    mPlayer.seekTo(progress);
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
                    }
                }
            }
        }).start();
    }

    public class AudioBinder extends Binder{
        public AudioService getService() {return AudioService.this;}
    }
}
