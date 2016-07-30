package web;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import data.MixContract;
import utils.Constants;

/**
 * Created by douglas on 7/28/2016.
 */
public class SyncManager {

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;

    private static SyncManager mInstance;

    public SyncManager() {
    }

    public static synchronized SyncManager getInstance() {
        if (mInstance == null) {
            mInstance = new SyncManager();
        }
        return mInstance;
    }

    public void updateAllTables(Account mAccount, String authority){
        //perform an immediate sync on all tables
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE,Constants.SyncDataType.Mixes.getCode());
        settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_ACTION,Constants.SyncDataAction.UpdateMix.getCode());
        ContentResolver.requestSync(mAccount, MixContract.CONTENT_AUTHORITY, settingsBundle); // Sync mixes

        settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE,Constants.SyncDataType.Mixes.getCode());
        settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_ACTION,Constants.SyncDataAction.UpdateFavorite.getCode());
        ContentResolver.requestSync(mAccount, MixContract.CONTENT_AUTHORITY, settingsBundle); // Sync favorites

        settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE,Constants.SyncDataType.Playlists.getCode());
        ContentResolver.requestSync(mAccount, MixContract.CONTENT_AUTHORITY, settingsBundle); //Sync Playlists

        //ContentResolver.addPeriodicSync(mAccount, authority, Bundle.EMPTY, SYNC_INTERVAL); // sets our sync adapter to go after a period of time.
    }
}