package service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.IOException;

public class AudioService extends Service implements MediaPlayer.OnPreparedListener,  MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String ACTION_PLAY = "com.example.action.PLAY";

    private MediaPlayer mPlayer;

    public AudioService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            mPlayer = new MediaPlayer();
            mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);

            try{
                mPlayer.setDataSource("http://api.soundcloud.com/tracks/7399237/stream?client_id=6af4e9b999eaa63f5d797d466cdc4ccb");
            }catch (IllegalArgumentException e){
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            mPlayer.prepareAsync();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
