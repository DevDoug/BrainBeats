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
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.VolleyError;
import com.brainbeats.R;

import org.json.JSONObject;

import architecture.AccountManager;
import data.MixContract;
import data.MixDbHelper;
import entity.Track;
import utils.Constants;

/**
 * Created by douglas on 7/21/2016.
 */
public class BrainBeatsSyncAdapter extends AbstractThreadedSyncAdapter {

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    /**
     * Set up the sync adapter
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
    /*
     * Put the data transfer code here.
     */
        if(extras != null){
            int selectedTrackId = extras.getInt(Constants.KEY_EXTRA_SELECTED_TRACK_ID);

            Cursor mixCursor = null;
            try {
                mixCursor = provider.query(
                        MixContract.MixEntry.CONTENT_URI, //Get mixes
                        null,  //return everything
                        MixContract.MixEntry._ID + MixDbHelper.WHERE_CLAUSE_EQUAL,
                        new String[]{String.valueOf(selectedTrackId)},
                        null
                );
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if (mixCursor != null) { //if this mix is present then favorite it otherwise it is a sound cloud track and needs to be converted to a mix
                mixCursor.moveToFirst();

                WebApiManager.putUserFavorite(getContext(), architecture.AccountManager.getInstance(getContext()).getUserId(), String.valueOf(selectedTrackId), new WebApiManager.OnObjectResponseListener() {
                    @Override
                    public void onObjectResponse(JSONObject object) {
                        Log.i("", "Success");
/*                    try {
                        int returnId = provider.update(MixContract.MixEntry.CONTENT_URI, Constants.buildMixRecord(Constants.buildMixRecordFromTrack(selectedTrack)), MixDbHelper.DB_SC_ID_FIELD + selectedTrack.getID(), null);
                        if(returnId != -1)
                            Log.i("info","transaction complete");
                        else
                            Log.i("info","transaction failed");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }*/
                    }
                }, new WebApiManager.OnErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = new String(error.networkResponse.data);
                        Log.i("", errorMessage);
                        if (AccountManager.getInstance(getContext()).isConnnectedToSoundCloud()) { //if the user is auhorized to use sound cloud there was an issue

                        } else { //otherwise have them connect to soundcloud
                            Constants.buildConfirmDialog(getContext(), getContext().getString(R.string.sound_cloud_login_message));
                        }
                    }
                });

                mixCursor.close();
            }
        }
    }
}
