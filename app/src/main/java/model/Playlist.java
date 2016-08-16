package model;

/**
 * Created by douglas on 7/28/2016.
 */
public class Playlist {

    public String mPlaylistTitle;
    private int mSoundCloudId;

    public String getPlaylistTitle() {
        return mPlaylistTitle;
    }

    public void setPlaylistTitle(String playlisttitle) {
        this.mPlaylistTitle = playlisttitle;
    }

    public int getSoundCloudId() {
        return mSoundCloudId;
    }

    public void setSoundCloudId(int soundCloudId) {
        this.mSoundCloudId = soundCloudId;
    }
}
