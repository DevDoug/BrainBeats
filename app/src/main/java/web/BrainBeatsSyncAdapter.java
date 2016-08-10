package web;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
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

import architecture.AccountManager;
import data.BrainBeatsContract;
import data.BrainBeatsDbHelper;
import entity.Track;
import entity.UserCollection;
import entity.UserCollectionEntry;
import entity.UserPlaylistsResponse;
import model.Mix;
import model.Playlist;
import model.User;
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
        Log.i("In sync adapter", "in sync");

        int selectedTrackId = extras.getInt(Constants.KEY_EXTRA_SELECTED_TRACK_ID);
        int SYNC_TYPE = extras.getInt(Constants.KEY_EXTRA_SYNC_TYPE);
        int SYNC_ACTION = extras.getInt(Constants.KEY_EXTRA_SYNC_ACTION);

        switch (SYNC_TYPE) {
            case 0: //sync mixes
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
                                    Log.i("Favs", String.valueOf(userTracks.size()));
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
            case 1: //sync mix related
                /*WebApiManager.getRelatedTracks(getContext(), String.valueOf(selectedTrackId), new WebApiManager.OnObjectResponseListener() {
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
                });*/
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
                            for (UserPlaylistsResponse playlistsResponse : userPlaylists) {
                                try {
                                    Cursor playlistCursor = provider.query( //find if this mix exists
                                            BrainBeatsContract.MixPlaylistEntry.CONTENT_URI, //Get users
                                            null,  //return everything
                                            BrainBeatsContract.MixPlaylistEntry.COLUMN_NAME_PLAYLIST_SOUNDCLOUD_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
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
            case 3: // sync user's
                //TODO add get people the user is following and convert it to a Brain Beats user.
                WebApiManager.getUserFollowing(getContext(), AccountManager.getInstance(getContext()).getUserId(), new WebApiManager.OnObjectResponseListener() {
                    @Override
                    public void onObjectResponse(JSONObject object) {
                        Log.i("User fetch succsess", "success");
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
                                            addUser(collection, true, provider); // create this as a user from sound cloud
                                        Log.i("User Added", "Added");
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
        }
    }

    public void addMix(Track track, boolean inLibrary, boolean isFavorite, boolean inMixer, ContentProviderClient provider) {
        Mix newMix = Constants.buildMixRecordFromTrack(track);
        newMix.setMixFavorite((isFavorite) ? 1 : 0);
        newMix.setIsInLibrary((inLibrary) ? 1 : 0);
        newMix.setIsInMixer((inMixer) ? 1 : 0);
        newMix.setMixUserId(Integer.parseInt(AccountManager.getInstance(getContext()).getUserId()));

        try {
            Uri result = provider.insert(BrainBeatsContract.MixEntry.CONTENT_URI, Constants.buildMixRecord(newMix));
            //getContext().getContentResolver().notifyChange(BrainBeatsContract.MixEntry.CONTENT_URI, null, false);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addPlaylist(UserPlaylistsResponse playlistsResponse, ContentProviderClient provider) {
        Playlist playlist = new Playlist();
        playlist.setPlaylistTitle(playlistsResponse.getTitle());
        playlist.setSoundCloudId(playlistsResponse.getId());

        try {
            Uri result = provider.insert(BrainBeatsContract.MixPlaylistEntry.CONTENT_URI, Constants.buildPlaylistRecord(playlist));
            //getContext().getContentResolver().notifyChange(BrainBeatsContract.MixEntry.CONTENT_URI, null, false);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addUser(UserCollectionEntry collection, boolean isFollowing, ContentProviderClient provider) {
        User user = new User();
        user.setUserName(collection.getUsername());
        user.setSoundCloudUserId(collection.getId());

        try {
            Uri result = provider.insert(BrainBeatsContract.UserEntry.CONTENT_URI, Constants.buildUserRecord(user)); //insert user rec
            if (isFollowing) { //if the user is following this person add the record to the following table, now when quered
                Uri relatedResult = provider.insert(BrainBeatsContract.UserFollowersEntry.CONTENT_URI, Constants.buildUserFollowingRecord(AccountManager.getInstance(getContext()).getUserId(), String.valueOf(collection.getId())));
                Log.i("Add user following rec", "user collection Id " + String.valueOf(collection.getId()));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

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

    public void favoriteTrackOnSoundCloud(int trackId, ContentProviderClient provider) {
        WebApiManager.putUserFavorite(getContext(), architecture.AccountManager.getInstance(getContext()).getUserId(), String.valueOf(trackId), new WebApiManager.OnObjectResponseListener() {
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
                    //getContext().getContentResolver().notifyChange(Constants.FAVORITE_ERROR_URI, null, false);
                    Log.i("", "Fail track has not been favorite on Sound Cloud"); //There was an issue
                } else { //otherwise have them connect to soundcloud
                    //getContext().getContentResolver().notifyChange(Constants.NOT_LOGGED_IN_TO_SOUNDCLOUD_URI, null, false);
                    Log.i("", "Fail track has not been favorite on Sound Cloud user not authorized"); //There was an issue user not authorized
                }
            }
        });
    }
}