package web;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Stream;

import architecture.AccountManager;
import data.MixContract;
import data.MixDbHelper;
import entity.Collection;
import entity.Playlists;
import entity.RelatedTracksResponse;
import entity.Track;
import entity.UserPlaylistsResponse;
import entity.UserTrackResponse;
import model.Mix;
import model.Playlist;
import utils.Constants;

/**
 * Created by douglas on 7/21/2016.
 */
public class BrainBeatsSyncAdapter extends AbstractThreadedSyncAdapter {

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    private Context mContext;
    /**
     * Set up the sync adapter
     */
    public BrainBeatsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }
    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public BrainBeatsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /*
   * Specify the code you want to run in the sync adapter. The entire
   * sync adapter runs in a background thread, so you don't have to set
   * up your own background processing.
   */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i("In sync adapter","in sync");

        int selectedTrackId = extras.getInt(Constants.KEY_EXTRA_SELECTED_TRACK_ID);
        int SYNC_TYPE = extras.getInt(Constants.KEY_EXTRA_SYNC_TYPE);

        switch (SYNC_TYPE){
            case 0: //sync mixes
                WebApiManager.getUserTracks(getContext(), AccountManager.getInstance(getContext()).getUserId(), new WebApiManager.OnArrayResponseListener() {
                    @Override
                    public void onArrayResponse(JSONArray array) {
                        Log.i(getClass().getSimpleName(), "Response = " + array.toString());
                        Gson gson = new Gson();
                        Type token = new TypeToken<ArrayList<UserTrackResponse>>(){}.getType();
                        try {
                            ArrayList<UserTrackResponse> userTracks = gson.fromJson(array.toString(), token);
                            for(UserTrackResponse trackResponse : userTracks) {
                                try {
                                    Cursor trackCursor = provider.query( //find if this mix exists
                                            MixContract.MixEntry.CONTENT_URI, //Get users
                                            null,  //return everything
                                            MixContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + MixDbHelper.WHERE_CLAUSE_EQUAL,
                                            new String[]{String.valueOf(trackResponse.getID())},
                                            null
                                    );
                                    if (trackCursor != null && trackCursor.getCount() != 0) { // this mix exists so update the record.
                                        /*                                        Mix mix = Constants.buildMixFromCursor(getContext(), trackCursor, 0);
                                        Log.i("InLib", String.valueOf(mix.getIsInLibrary()));
                                        int returnId = provider.update(
                                                MixContract.MixEntry.CONTENT_URI,
                                                Constants.buildMixRecord(mix),
                                                MixContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + MixDbHelper.WHERE_CLAUSE_EQUAL,
                                                new String[]{String.valueOf(trackResponse.getID())});
                                        if(returnId != -1)
                                            Log.i("Mix updated","Updated");
                                        else
                                            Log.i("Mix Updated","Fail");*/
                                        trackCursor.close();
                                    } else{
                                        addMix(trackResponse.getTitle(), trackResponse.getArtworkURL(), trackResponse.getID(), 0,true,false,false, provider); // create this as a mix from a sound cloud track
                                        Log.i("Mix Added","Added");
                                    }
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new WebApiManager.OnErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.toString();
                    }
                });
                break;
            case 1: //sync mix related
                WebApiManager.getRelatedTracks(getContext(), String.valueOf(selectedTrackId), new WebApiManager.OnObjectResponseListener() {
                    @Override
                    public void onObjectResponse(JSONObject object) {
                        Log.i(getClass().getSimpleName(), "Response = " + object.toString());
                        Gson gson = new Gson();
                        Type token = new TypeToken<RelatedTracksResponse>() {
                        }.getType();
                        try {
                            RelatedTracksResponse relatedTracks = gson.fromJson(object.toString(), token);
                            ArrayList<Collection> mCollections = (ArrayList<Collection>) relatedTracks.getCollection();

                            Cursor relatedMixCursor = provider.query( //find if this mix has an associated mix related entry
                                    MixContract.MixEntry.CONTENT_URI, //Get users
                                    null,  //return everything
                                    MixContract.MixRelatedEntry._ID + MixDbHelper.WHERE_CLAUSE_EQUAL,
                                    new String[]{String.valueOf(selectedTrackId)},
                                    null
                            );

                            if (relatedMixCursor != null && relatedMixCursor.getCount() >= 1) { //this mix is already part of a related mix record update the related list
                                updateRelateMixes(mCollections, provider);
                                relatedMixCursor.close();
                            } else { //add the related record with this mix
                                Uri returnRecord = provider.insert(MixContract.MixRelatedEntry.CONTENT_URI, Constants.buildMixRelatedRecord());
                                long returnRowId = ContentUris.parseId(returnRecord);
                                if (returnRowId != -1) {
                                    updateRelateMixes(mCollections, provider);
                                } else {
                                    Log.i("Error", "Transaction Failed");
                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new WebApiManager.OnErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(getClass().getSimpleName(), "Response = " + error.toString());
                    }
                });
                break;
            case 2: //sync mix playlist
                WebApiManager.getUserPlaylists(getContext(), AccountManager.getInstance(getContext()).getUserId(), new WebApiManager.OnArrayResponseListener() {
                    @Override
                    public void onArrayResponse(JSONArray array) {
                        Log.i(getClass().getSimpleName(), "Response = " + array.toString());
                        Gson gson = new Gson();
                        Type token = new TypeToken<ArrayList<UserPlaylistsResponse>>() {
                        }.getType();
                        try {
                            ArrayList<UserPlaylistsResponse> userPlaylists = gson.fromJson(array.toString(), token);
                            for(UserPlaylistsResponse playlistsResponse : userPlaylists) {
                                try {
                                    Cursor playlistCursor = provider.query( //find if this mix exists
                                            MixContract.MixEntry.CONTENT_URI, //Get users
                                            null,  //return everything
                                            MixContract.MixPlaylistEntry._ID + MixDbHelper.WHERE_CLAUSE_EQUAL,
                                            new String[]{String.valueOf(playlistsResponse.getId())},
                                            null
                                    );
                                    if (playlistCursor != null && playlistCursor.getCount() != 0) { // this playlist exists so update the record.
                                        /*                                        Mix mix = Constants.buildMixFromCursor(getContext(), trackCursor, 0);
                                        Log.i("InLib", String.valueOf(mix.getIsInLibrary()));
                                        int returnId = provider.update(
                                                MixContract.MixEntry.CONTENT_URI,
                                                Constants.buildMixRecord(mix),
                                                MixContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + MixDbHelper.WHERE_CLAUSE_EQUAL,
                                                new String[]{String.valueOf(trackResponse.getID())});
                                        if(returnId != -1)
                                            Log.i("Mix updated","Updated");
                                        else
                                            Log.i("Mix Updated","Fail");*/
                                        playlistCursor.close();
                                    } else{
                                        addPlaylist(playlistsResponse,provider); // create this as a mix from a sound cloud track
                                        Log.i("Mix Added","Added");
                                    }
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new WebApiManager.OnErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(getClass().getSimpleName(), "Response = " + error.toString());
                    }
                });
                break;
            case 3: // sync user's
                break;
            default:
                break;
        }
    }

    public void addMix(String selectedTrackTitle, String selectedTrackAlbumUrl, int selectedTrackId, int relatedTracksId, boolean inLibrary, boolean isFavorite, boolean inMixer, ContentProviderClient provider){
        Track insertTrack = new Track();
        insertTrack.setTitle(selectedTrackTitle);
        insertTrack.setArtworkURL(selectedTrackAlbumUrl);
        insertTrack.setUserFavorite(isFavorite);
        insertTrack.setID(selectedTrackId);

        Mix newMix = Constants.buildMixRecordFromTrack(insertTrack);
        newMix.setRelatedTracksId(relatedTracksId);
        newMix.setIsInLibrary((inLibrary) ? 1 : 0 );
        newMix.setIsInMixer((inMixer) ? 1 : 0);

        Log.i("New Mix", "Adding new mix");
        try {
            Uri result = provider.insert(MixContract.MixEntry.CONTENT_URI, Constants.buildMixRecord(newMix));
            getContext().getContentResolver().notifyChange(MixContract.MixEntry.CONTENT_URI, null, false);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addPlaylist(UserPlaylistsResponse playlistsResponse, ContentProviderClient provider){
        Playlist playlist = new Playlist();
        playlist.setPlaylistTitle(playlistsResponse.getTitle());

        try {
            Uri result = provider.insert(MixContract.MixPlaylistEntry.CONTENT_URI, Constants.buildPlaylistRecord(playlist));
            getContext().getContentResolver().notifyChange(MixContract.MixEntry.CONTENT_URI, null, false);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateRelateMixes(ArrayList<Collection> mCollections, ContentProviderClient provider){
        //update all mixes in the collection
        Cursor mixCursor;
        for(Collection collection : mCollections) {
            try {
                mixCursor = provider.query( //find if this mix exists ?
                        MixContract.MixEntry.CONTENT_URI, //Get users
                        null,  //return everything
                        MixContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + MixDbHelper.WHERE_CLAUSE_EQUAL,
                        new String[]{String.valueOf(collection.getId())},
                        null
                );
                if (mixCursor != null && mixCursor.getCount() != 0) { // this mix exists so update the record.
                    Mix mix = Constants.buildMixFromCursor(getContext(), mixCursor, 0);
                    mix.setRelatedTracksId(collection.getId());

                    int returnId = provider.update(
                            MixContract.MixEntry.CONTENT_URI,
                            Constants.buildMixRecord(mix),
                            MixContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + MixDbHelper.WHERE_CLAUSE_EQUAL,
                            new String[]{String.valueOf(collection.getId())});
                } else
                    addMix(collection.getTitle(), collection.getArtworkUrl(), collection.getId(), collection.getId(),false,false,false, provider); // create this as a mix from a sound cloud track
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}