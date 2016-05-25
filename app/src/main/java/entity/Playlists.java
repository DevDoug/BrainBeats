package entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by douglas on 5/24/2016.
 */
public class Playlists {

    @SerializedName("tracks")
    List<Track> mPlaylistTracks;

    public List<Track> getPlaylistTracks() {
        return mPlaylistTracks;
    }

    public void setPlaylistTracks(List<Track> PlaylistTracks) {
        this.mPlaylistTracks = PlaylistTracks;
    }
}
