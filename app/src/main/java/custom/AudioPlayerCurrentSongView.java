package custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.brainbeats.R;

/**
 * Created by douglas on 8/24/2016.
 * Custom view to allow the user to return to their music from the application
 */
public class AudioPlayerCurrentSongView extends RelativeLayout {

    public static boolean isDisplayed;

    LayoutInflater mInflater;

    public AudioPlayerCurrentSongView(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }
    public AudioPlayerCurrentSongView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        init();
    }
    public AudioPlayerCurrentSongView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }
    public void init() {
        View v = mInflater.inflate(R.layout.current_song_playing_layout, this, true);
    }
}
