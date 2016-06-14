package architecture;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.brainbeats.BuildConfig;

/**
 * Created by douglas on 6/5/2016.
 */
public class AccountManager  {

    private static AccountManager mAccountInstance;
    private static Context mContext;
    private static boolean mIsConnected;

    public static final String PACKAGE_NAME                         = BuildConfig.APPLICATION_ID;   //"com.brainbeats";
    private static final String BRAIN_BEATS_AUTH_TOKEN              = PACKAGE_NAME + ".BRAIN_BEATS_AUTH_TOKEN";
    private static final String BRAIN_BEATS_SOUND_CLOUD_USER_ID     = PACKAGE_NAME + ".BRAIN_BEATS_SOUND_CLOUD_USER_ID";

    private AccountManager(Context context){
        this.mContext = context;
    }

    public static synchronized AccountManager getInstance(Context context){
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

    public String getAccessToken(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String token = sharedPreferences.getString(BRAIN_BEATS_AUTH_TOKEN, null);
        return token;
    }

    public void setAccessToken(String token){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BRAIN_BEATS_AUTH_TOKEN, token);
        editor.commit();
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userId = sharedPreferences.getString(BRAIN_BEATS_SOUND_CLOUD_USER_ID, null);
        return userId;
    }

    public void setUserId(String userId){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BRAIN_BEATS_SOUND_CLOUD_USER_ID, userId);
        editor.commit();
    }
}
