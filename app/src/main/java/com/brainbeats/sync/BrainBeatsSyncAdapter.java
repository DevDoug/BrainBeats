package com.brainbeats.sync;

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

import com.brainbeats.architecture.AccountManager;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.data.BrainBeatsDbHelper;
import com.brainbeats.entity.Track;
import com.brainbeats.entity.UserPlaylistsResponse;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.Mix;
import com.brainbeats.model.Playlist;
import com.brainbeats.utils.Constants;

/**
 * Created by douglas on 7/21/2016.
 * Sync adapter for brain beats should keep all Sound Cloud API com.brainbeats.data in com.brainbeats.sync.
 */
public class BrainBeatsSyncAdapter extends AbstractThreadedSyncAdapter {

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /**
     * Set up the com.brainbeats.sync adapter
     */
    public BrainBeatsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the com.brainbeats.sync adapter. This form of the
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
   * Specify the code you want to run in the com.brainbeats.sync adapter. The entire
   * com.brainbeats.sync adapter runs in a background thread, so you don't have to set
   * up your own background processing.
   */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        /*Log.i("In com.brainbeats.sync", "in com.brainbeats.sync");

        int selectedTrackId = extras.getInt(Constants.KEY_EXTRA_SELECTED_TRACK_ID);
        int SYNC_TYPE = extras.getInt(Constants.KEY_EXTRA_SYNC_TYPE);
        int SYNC_ACTION = extras.getInt(Constants.KEY_EXTRA_SYNC_ACTION);

        switch (SYNC_TYPE) {
            case 0: //com.brainbeats.sync mixes
                switch (SYNC_ACTION) {
                    case 0:
                        WebApiManager.getUserTracks(getContext(), AccountManager.getInstance(getContext()).getUserId(), new WebApiManager.OnArrayResponseListener() {
                            @Override
                            public void onArrayResponse(JSONArray array) {
                                Log.i(getClass().getSimpleName(), "Response = " + array.toString());
                                Gson gson = new Gson();
                                Type token = new TypeToken<ArrayList<Track>>() {
                                }.getType();
                                try {
                                    ArrayList<Track> userTracks = gson.fromJson(array.toString(), token);
                                    for (Track track : userTracks) {
                                        try {
                                            Cursor trackCursor = provider.query( //find if this mix exists
                                                    BrainBeatsContract.MixEntry.CONTENT_URI,
                                                    null,  //return everything
                                                    BrainBeatsContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                                                    new String[]{String.valueOf(track.getID())},
                                                    null
                                            );
                                            if (trackCursor != null && trackCursor.getCount() != 0) { // this mix exists so update the record.
                                                //TODO add code that allows user to change mix attribute such as title
                                                trackCursor.close();
                                            } else {
                                                addMix(track, true, false, false, provider); // create this as a mix from a sound cloud track
                                                Log.i("Mix Added", "Added");
                                                if (trackCursor != null) {
                                                    trackCursor.close();
                                                }
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
                                error.toString();
                            }
                        });
                        break;
                    case 1:
                        WebApiManager.getUserFavorites(getContext(), AccountManager.getInstance(getContext()).getUserId(), new WebApiManager.OnArrayResponseListener() {
                            @Override
                            public void onArrayResponse(JSONArray array) {
                                Log.i(getClass().getSimpleName(), "Response = " + array.toString());
                                Gson gson = new Gson();
                                Type token = new TypeToken<ArrayList<Track>>() {
                                }.getType();
                                try {
                                    ArrayList<Track> userTracks = gson.fromJson(array.toString(), token);
                                    for (Track track : userTracks) {
                                        try {
                                            Cursor trackCursor = provider.query( //find if this mix exists
                                                    BrainBeatsContract.MixEntry.CONTENT_URI,
                                                    null,  //return everything
                                                    BrainBeatsContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                                                    new String[]{String.valueOf(track.getID())},
                                                    null
                                            );
                                            if (trackCursor != null && trackCursor.getCount() != 0) { // this mix exists so update the record.
                                                trackCursor.moveToFirst();
                                                if (trackCursor.getInt(trackCursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_FAVORITE)) == 0) {
                                                    //update the local db from the change on sound cloud
                                                    Log.i("Action", "favorite in local");
                                                    Mix mix = Constants.buildMixFromCursor(getContext(), trackCursor, 0);
                                                    mix.setMixFavorite(1);
                                                    int returnId = provider.update(
                                                            BrainBeatsContract.MixEntry.CONTENT_URI,
                                                            Constants.buildMixRecord(mix),
                                                            BrainBeatsContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                                                            new String[]{String.valueOf(track.getID())});
                                                    if (returnId != -1)
                                                        Log.i("Mix updated", "Updated");
                                                    else
                                                        Log.i("Mix Updated", "Fail");

                                                }
                                                trackCursor.close();
                                            } else {
                                                addMix(track, false, true, false, provider); // create this as a mix from a sound cloud track
                                                Log.i("Favorite Added", "Added a favorite");
                                                if (trackCursor != null) {
                                                    trackCursor.close();
                                                }
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
                    case 2: //set user fav on sc
                        Log.i("Action", "Favorite locally but not in api, favorite on SC !");
                        favoriteTrackOnSoundCloud(selectedTrackId, provider);
                        break;
                }
                break;
            case 1: //com.brainbeats.sync mix related     //TODO - implement in version 2.0 beta version
                *//*WebApiManager.getRelatedTracks(getContext(), String.valueOf(selectedTrackId), new WebApiManager.OnObjectResponseListener() {
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
                                    BrainBeatsContract.MixEntry.CONTENT_URI, //Get users
                                    null,  //return everything
                                    BrainBeatsContract.MixRelatedEntry._ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                                    new String[]{String.valueOf(selectedTrackId)},
                                    null
                            );

                            if (relatedMixCursor != null && relatedMixCursor.getCount() >= 1) { //this mix is already part of a related mix record update the related list
                                updateRelateMixes(mCollections, provider);
                                relatedMixCursor.close();
                            } else { //add the related record with this mix
                                Uri returnRecord = provider.insert(BrainBeatsContract.MixRelatedEntry.CONTENT_URI, Constants.buildMixRelatedRecord());
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
                });*//*
                break;
            case 2: //com.brainbeats.sync mix playlist
                WebApiManager.getUserPlaylists(getContext(), AccountManager.getInstance(getContext()).getUserId(), new WebApiManager.OnArrayResponseListener() {
                    @Override
                    public void onArrayResponse(JSONArray array) {
                        Log.i(getClass().getSimpleName(), "Response = " + array.toString());
                        Gson gson = new Gson();
                        Type token = new TypeToken<ArrayList<UserPlaylistsResponse>>() {
                        }.getType();
                        try {
                            ArrayList<UserPlaylistsResponse> userPlaylists = gson.fromJson(array.toString(), token);
                            for (UserPlaylistsResponse playlistsResponse : userPlaylists) {
                                try {
                                    Cursor playlistCursor = provider.query( //find if this mix exists
                                            BrainBeatsContract.PlaylistEntry.CONTENT_URI, //Get users
                                            null,  //return everything
                                            BrainBeatsContract.PlaylistEntry.COLUMN_NAME_PLAYLIST_SOUNDCLOUD_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                                            new String[]{String.valueOf(playlistsResponse.getId())},
                                            null
                                    );
                                    if (playlistCursor != null && playlistCursor.getCount() != 0) { // this playlist exists so update the record.
                                        //TODO add code that allows user to change playlist attributes
                                        playlistCursor.close();
                                    } else {
                                        addPlaylist(playlistsResponse, provider); // create this as a mix from a sound cloud track
                                        Log.i("Mix Added", "Added");
                                        if (playlistCursor != null) {
                                            playlistCursor.close();
                                        }
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
            case 3: // com.brainbeats.sync user's
                WebApiManager.getUserFollowing(getContext(), AccountManager.getInstance(getContext()).getUserId(), new WebApiManager.OnObjectResponseListener() {
                    @Override
                    public void onObjectResponse(JSONObject object) {
                        Gson gson = new Gson();
                        Type token = new TypeToken<UserCollection>() {
                        }.getType();
                        try {
                            UserCollection userFollowingCollection = gson.fromJson(object.toString(), token);
                            for (UserCollectionEntry collection : userFollowingCollection.getCollection()) {
                                try {
                                    Cursor userCursor = provider.query( //find if this user exists
                                            BrainBeatsContract.UserEntry.CONTENT_URI, //Get users
                                            null,  //return everything
                                            BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                                            new String[]{String.valueOf(collection.getId())},
                                            null
                                    );
                                    if (userCursor != null && userCursor.getCount() != 0) { // this user exists so update the record.
                                        //TODO add code that allows user to change user attributes
                                        userCursor.close();
                                    } else {
                                        if (!String.valueOf(collection.getId()).equalsIgnoreCase(AccountManager.getInstance(getContext()).getUserId())) // if this user is not the current user
                                            getUserInfo(collection.getId(),provider); // create this as a user from sound cloud
                                        if (userCursor != null) {
                                            userCursor.close();
                                        }
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
                        Log.i("User fetch fail", "fail");
                    }
                });
                // break;
            default:
                break;
        }*/
    }

/*    public void favoriteTrackOnSoundCloud(int trackId, ContentProviderClient provider) {
        WebApiManager.putUserFavorite(getContext(), com.brainbeats.architecture.AccountManager.getInstance(getContext()).getUserId(), String.valueOf(trackId), new WebApiManager.OnObjectResponseListener() {
            @Override
            public void onObjectResponse(JSONObject object) {
                Log.i("", "Success track has been favorite on Sound Cloud"); //we have succesfully updated the api with our local change inform the user !
            }
        }, new WebApiManager.OnErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = new String(error.networkResponse.data);
                Log.i("", errorMessage);
                if (AccountManager.getInstance(getContext()).isConnnectedToSoundCloud()) { //if the user is auhorized to use sound cloud there was an issue
                    Log.i("", "Fail track has not been favorite on Sound Cloud"); //There was an issue
                } else { //otherwise have them connect to soundcloud
                    Log.i("", "Fail track has not been favorite on Sound Cloud user not authorized"); //There was an issue user not authorized
                }
            }
        });
    }*/

    //TODO - implement in version 2.0 beta version
/*    public void updateRelateMixes(ArrayList<Collection> mCollections, ContentProviderClient provider){
        //update all mixes in the collection
        Cursor mixCursor;
        for(Collection collection : mCollections) {
            try {
                mixCursor = provider.query( //find if this mix exists ?
                        BrainBeatsContract.MixEntry.CONTENT_URI, //Get users
                        null,  //return everything
                        BrainBeatsContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                        new String[]{String.valueOf(collection.getId())},
                        null
                );
                if (mixCursor != null && mixCursor.getCount() != 0) { // this mix exists so update the record.
                    Mix mix = Constants.buildMixFromCursor(getContext(), mixCursor, 0);
                    mix.setRelatedTracksId(collection.getId());

                    int returnId = provider.update(
                            BrainBeatsContract.MixEntry.CONTENT_URI,
                            Constants.buildMixRecord(mix),
                            BrainBeatsContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                            new String[]{String.valueOf(collection.getId())});

                    mixCursor.close();
                } else{
                   // addMix(collection.getTitle(), collection.getArtworkUrl(), collection.getId(), collection.getId(),false,false,false, provider); // create this as a mix from a sound cloud track
                    if (mixCursor != null) {
                        mixCursor.close();
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }*/
}