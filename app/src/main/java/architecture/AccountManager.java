package architecture;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.brainbeats.BuildConfig;

import utils.Constants;

/**
 * Created by douglas on 6/5/2016.
 * Singleton account manager
 */
public class AccountManager  {

    private static AccountManager mAccountInstance;
    private Context mContext;

    public static final String PACKAGE_NAME                 = BuildConfig.APPLICATION_ID;   //"com.brainbeats";
    private static final String SOUND_CLOUD_AUTH_TOKEN      = PACKAGE_NAME + ".SOUND_CLOUD_AUTH_TOKEN";
    private static final String BRAIN_BEATS_USER_ID         = PACKAGE_NAME + ".BRAIN_BEATS_USER_ID";

    private AccountManager(Context context) {
        this.mContext = context;
    }

    public static synchronized AccountManager getInstance(Context context) {
        if (mAccountInstance == null) {
            mAccountInstance = new AccountManager(context);
        }
        return mAccountInstance;
    }

    public boolean isLoggedIn() {
        boolean result = false;
        String userId = getUserId();
        if (userId != null)
            result = true;
        return result;
    }

    public boolean isConnnectedToSoundCloud() {
        boolean result = false;
        String accessToken = getAccessToken();
        if (accessToken != null) {
            result = true;
        }
        return result;
    }

    public String getAccessToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String token = sharedPreferences.getString(SOUND_CLOUD_AUTH_TOKEN, null);
        return token;
    }

    public void setAccessToken(String token) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SOUND_CLOUD_AUTH_TOKEN, token);
        editor.commit();
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userId = sharedPreferences.getString(BRAIN_BEATS_USER_ID, null);
        return userId;
    }

    public void setUserId(String userId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BRAIN_BEATS_USER_ID, userId);
        editor.commit();
    }

    public void removeUserId() {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.remove(BRAIN_BEATS_USER_ID);
        editor.commit();
    }

    public boolean getIsIncognito() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean isIncog = sharedPreferences.getBoolean(Constants.KEY_EXTRA_IS_INCOGNITO_PREF, false);
        return isIncog;
    }

    public boolean getIsSyncedToSoundCloud() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean isSyncedToSC = sharedPreferences.getBoolean(Constants.KEY_EXTRA_IS_SYNCED_TO_SC, false);
        return isSyncedToSC;
    }

    public void setSyncToSoundCloud(boolean isSyncToSc) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.KEY_EXTRA_IS_SYNCED_TO_SC, isSyncToSc);
        editor.commit();
    }

    public void isInCognito(boolean isIncog) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.KEY_EXTRA_IS_INCOGNITO_PREF, isIncog);
        editor.commit();
    }

    public void forceLogout(Context context) {
        removeUserId();
    }

    public boolean getGlobalSyncRequired() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean isGlobalSyncRequired = sharedPreferences.getBoolean(Constants.KEY_EXTRA_IS_GLOBAL_SYNC_REQUIRED, true);
        return isGlobalSyncRequired;
    }

    public void setGlobalSyncRequired(boolean isGlobalSyncRequired) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.KEY_EXTRA_IS_GLOBAL_SYNC_REQUIRED, isGlobalSyncRequired);
        editor.commit();
    }

    public boolean getDisplayCurrentSongView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean isDisplayFadeInPlayingNotification = sharedPreferences.getBoolean(Constants.KEY_EXTRA_IS_DISPLAY_CURRENT_PLAYING_SONG, false);
        return isDisplayFadeInPlayingNotification;
    }

    public void setDisplayCurrentSongView(boolean isDisplayFadeInPlayingNotification) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.KEY_EXTRA_IS_DISPLAY_CURRENT_PLAYING_SONG, isDisplayFadeInPlayingNotification);
        editor.commit();
    }
}
