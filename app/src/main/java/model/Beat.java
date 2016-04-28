package model;

import android.graphics.Bitmap;

/**
 * Created by Douglas on 4/15/2016.
 */
public class Beat {

    private String mBeatTitle;

    private Bitmap mBeatAlbumCoverArt;

    public Beat(){

    }

    public String getBeatTitle() {
        return mBeatTitle;
    }

    public void setBeatTitle(String BeatTitle) {
        this.mBeatTitle = BeatTitle;
    }

    public Bitmap getBeatAlbumCoverArt() {
        return mBeatAlbumCoverArt;
    }

    public void setBeatAlbumCoverArt(Bitmap BeatAlbumCoverArt) {
        this.mBeatAlbumCoverArt = BeatAlbumCoverArt;
    }
}