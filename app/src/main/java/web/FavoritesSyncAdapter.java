package web;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.VolleyError;
import com.brainbeats.R;

import org.json.JSONObject;

import architecture.AccountManager;
import data.MixContract;
import data.MixDbHelper;
import entity.Track;
import model.Mix;
import utils.Constants;

/**
 * Created by douglas on 7/21/2016.
 */
public class FavoritesSyncAdapter extends AbstractThreadedSyncAdapter {

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    private Context mContext;
    /**
     * Set up the sync adapter
     */
    public FavoritesSyncAdapter(Context context, boolean autoInitialize) {
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
    public FavoritesSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
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
        if(extras != null){
            int selectedTrackId = extras.getInt(Constants.KEY_EXTRA_SELECTED_TRACK_ID);
            String selectedTrackTitle = extras.getString(Constants.KEY_EXTRA_SELECTED_TRACK_TITLE);

            try {
                final Cursor mixCursor = provider.query(
                        MixContract.MixEntry.CONTENT_URI, //Get mixes
                        null,  //return everything
                        MixContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + MixDbHelper.WHERE_CLAUSE_EQUAL,
                        new String[]{String.valueOf(selectedTrackId)},
                        null
                );

                if (mixCursor != null) {
                    mixCursor.moveToFirst();

                    Log.i("RowCount", String.valueOf(mixCursor.getCount()));

                    if (mixCursor.getCount() == 0) { //this track has not been found convert it to a mix.
                        Track insertTrack = new Track();
                        insertTrack.setTitle(selectedTrackTitle);
                        insertTrack.setArtworkURL("");
                        insertTrack.setUserFavorite(false);
                        insertTrack.setID(selectedTrackId);
                        Uri result = provider.insert(MixContract.MixEntry.CONTENT_URI, Constants.buildMixRecord(Constants.buildMixRecordFromTrack(insertTrack)));
                        if (ContentUris.parseId(result) != -1) {
                            final Cursor newMixCursor = provider.query(
                                    MixContract.MixEntry.CONTENT_URI, //Get mixes
                                    null,  //return everything
                                    MixContract.MixEntry._ID + MixDbHelper.WHERE_CLAUSE_EQUAL,
                                    new String[]{String.valueOf(ContentUris.parseId(result))},
                                    null
                            );
                            if (newMixCursor != null){
                                newMixCursor.moveToFirst();
                                favoriteTrackOnSoundCloud(selectedTrackId, newMixCursor, provider);
                            }
                        } else
                            getContext().getContentResolver().notifyChange(Constants.FAVORITE_ERROR_URI, null, false);
                    } else { // this track is already a mix so favorite it.
                        favoriteTrackOnSoundCloud(selectedTrackId, mixCursor, provider);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void favoriteTrackOnSoundCloud(long selectedTrackId,Cursor mixCursor,ContentProviderClient provider){
        WebApiManager.putUserFavorite(getContext(), architecture.AccountManager.getInstance(getContext()).getUserId(), String.valueOf(selectedTrackId), new WebApiManager.OnObjectResponseListener() {
            @Override
            public void onObjectResponse(JSONObject object) {
                Log.i("", "Success");
                try {
                    Mix mix = Constants.buildMixFromCursor(getContext(),mixCursor,0);
                    mix.setMixItemFavorite(1); // set this mix as a favorite in the db.
                    Log.i("SoundCloudID", String.valueOf(mixCursor.getInt(mixCursor.getColumnIndex(MixContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID))));
                    int returnId = provider.update(MixContract.MixEntry.CONTENT_URI,Constants.buildMixRecord(mix), MixDbHelper.DB_SC_ID_FIELD + MixDbHelper.WHERE_CLAUSE_EQUAL + selectedTrackId,null);
                    if(returnId != -1){
                        getContext().getContentResolver().notifyChange(Constants.FAVORITE_SUCCESS_URI, null, false);
                        mixCursor.close();
                    }
                    else
                        Log.i("info","transaction failed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new WebApiManager.OnErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = new String(error.networkResponse.data);
                Log.i("", errorMessage);
                if (AccountManager.getInstance(getContext()).isConnnectedToSoundCloud()) { //if the user is auhorized to use sound cloud there was an issue
                    getContext().getContentResolver().notifyChange(Constants.FAVORITE_ERROR_URI, null, false);
                } else { //otherwise have them connect to soundcloud
                    getContext().getContentResolver().notifyChange(Constants.NOT_LOGGED_IN_TO_SOUNDCLOUD_URI, null, false);
                }
                mixCursor.close();

            }
        });
    }
}