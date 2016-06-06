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

    public static final String PACKAGE_NAME                 = BuildConfig.APPLICATION_ID;   //"com.brainbeats";
    public static final String BRAIN_BEATS_AUTH_TOKEN       = PACKAGE_NAME + ".BRAIN_BEATS_AUTH_TOKEN";

    private AccountManager(Context context){
        this.mContext = context;
    }

    public static synchronized AccountManager getInstance(Context context){
        if (mAccountInstance == null) {
            mAccountInstance = new AccountManager(context);
        }
        return mAccountInstance;
    }

    public static void setAccessToken(String token){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BRAIN_BEATS_AUTH_TOKEN, token);
        editor.commit();
    }
}
