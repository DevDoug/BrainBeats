package web;

import android.accounts.Account;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.VolleyError;
import com.brainbeats.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import architecture.AccountManager;
import data.MixContract;
import data.MixDbHelper;
import entity.Collection;
import entity.RelatedTracksResponse;
import entity.Track;
import model.Mix;
import utils.Constants;

/**
 * Created by douglas on 7/28/2016.
 */
public class OfflineSyncManager {

    private static OfflineSyncManager mInstance;
    private static Context mContext;

    public OfflineSyncManager(Context context) {
        mContext = context;
    }

    public static synchronized OfflineSyncManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new OfflineSyncManager(context);
        }
        return mInstance;
    }

    public void performSyncOnLocalDb(CoordinatorLayout coordinatorLayout,Bundle extras, ContentResolver provider){

        int selectedTrackId = extras.getInt(Constants.KEY_EXTRA_SELECTED_TRACK_ID);
        String selectedTrackTitle = extras.getString(Constants.KEY_EXTRA_SELECTED_TRACK_TITLE);
        String selectedTrackAlbumUrl = extras.getString(Constants.KEY_EXTRA_SELECTED_TRACK_ALBUM_COVER_ART);
        int SYNC_TYPE = extras.getInt(Constants.KEY_EXTRA_SYNC_TYPE);
        int updateMixAction = extras.getInt(Constants.KEY_EXTRA_SYNC_ACTION);

        switch (SYNC_TYPE){
            case 0: //sync mixes
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

                        switch (updateMixAction) {
                            case 0: //add to lib
                                if (mixCursor.getCount() != 0) { // this mix exists so update the record.
                                    Mix mix = Constants.buildMixFromCursor(mContext, mixCursor, 0);
                                    mix.setIsInLibrary(1);
                                    updateMixRecord(provider,mix,selectedTrackId);
                                    showSnackMessage(coordinatorLayout,R.string.error_this_mix_is_already_in_library);
                                } else{
                                    addMix(selectedTrackTitle, selectedTrackAlbumUrl, selectedTrackId, selectedTrackId,true,false,false, provider); // create this as a mix from a sound cloud track
                                    showSnackMessage(coordinatorLayout,R.string.song_added_to_library_snack_message);
                                }
                                break;
                            case 1: //add to fav
                                if (mixCursor.getCount() != 0) { // this mix exists so update the record.
                                    Mix mix = Constants.buildMixFromCursor(mContext, mixCursor, 0);
                                    if(mix.getMixFavorite() != 1) { //if mix is not fav
                                        mix.setMixFavorite(1);
                                        updateMixRecord(provider,mix,selectedTrackId);
                                        showSnackMessage(coordinatorLayout,R.string.song_added_to_favorites_snack_message);
                                    } else { //mix exists and is already a favorite
                                        showSnackMessage(coordinatorLayout,R.string.error_this_mix_is_already_a_favorite);
                                    }
                                } else{
                                    addMix(selectedTrackTitle, selectedTrackAlbumUrl, selectedTrackId, selectedTrackId,true,true,false, provider); // create this as a mix from a sound cloud track
                                    showSnackMessage(coordinatorLayout,R.string.song_added_to_favorites_snack_message);
                                }

                                if(AccountManager.getInstance(mContext).getIsSyncedToSoundCloud()){
                                    Bundle settingsBundle = new Bundle();
                                    settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE,0);
                                    settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_ACTION,1); //Action add mix to user fav on sc.
                                    settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                                    settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                                }

                                break;
                            case 2: //change rating
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //TODO after updating locally tell the syncadapter to update the server if it can be updated on server E.X. user favorites can be added to SC
                break;
            case 1: //sync mix related
                break;
            case 2: //sync mix playlist
                break;
            case 3: // sync user's
                break;
            default:
                break;
        }

    }

    public void addMix(String selectedTrackTitle, String selectedTrackAlbumUrl, int selectedTrackId, int relatedTracksId, boolean inLibrary, boolean isFavorite, boolean inMixer, ContentResolver provider){
        Track insertTrack = new Track();
        insertTrack.setTitle(selectedTrackTitle);
        insertTrack.setArtworkURL(selectedTrackAlbumUrl);
        insertTrack.setUserFavorite(isFavorite);
        insertTrack.setID(selectedTrackId);

        Mix newMix = Constants.buildMixRecordFromTrack(insertTrack);
        newMix.setRelatedTracksId(relatedTracksId);
        newMix.setIsInLibrary((inLibrary) ? 1 :0 );
        newMix.setIsInMixer((inMixer) ? 1 :0);

        Log.i("New Mix", "Adding new mix");
        try {
            Uri result = provider.insert(MixContract.MixEntry.CONTENT_URI, Constants.buildMixRecord(newMix));
           // context.getContentResolver().notifyChange(MixContract.MixEntry.CONTENT_URI, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMixRecord(ContentResolver provider, Mix mix, int selectedTrackId){
        try {
            int returnId = provider.update(
                    MixContract.MixEntry.CONTENT_URI,
                    Constants.buildMixRecord(mix),
                    MixContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + MixDbHelper.WHERE_CLAUSE_EQUAL,
                    new String[]{String.valueOf(selectedTrackId)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSnackMessage(CoordinatorLayout layout, int messageResource){
        Snackbar createdSnack;
        createdSnack = Snackbar.make(layout, messageResource, Snackbar.LENGTH_LONG);
        createdSnack.show();
    }


}