package com.brainbeats;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.VolleyError;
import com.brainbeats.architecture.AccountManager;
import com.brainbeats.entity.SoundCloudUser;
import com.brainbeats.fragments.AddNewArtistInfoFragment;
import com.brainbeats.fragments.LoginFragment;
import com.brainbeats.fragments.RegisterFragment;
import com.brainbeats.utils.Constants;
import com.brainbeats.web.WebApiManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Login screen
 * Should allow the user to login with both sound cloud and with Brain Beats
 */
public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {

    public static String TAG = "LoginActivity";

    public Fragment mLoginFragment;
    public Fragment mRegisterFragment;
    public Fragment mNewArtistFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            mLoginFragment = new LoginFragment();
            mRegisterFragment = new RegisterFragment();
            mNewArtistFragment = new AddNewArtistInfoFragment();
            switchToLoginFragment();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.compareTo(Constants.GO_TO_REGISTER_NEW_USER_URI) == 0) {
            switchToRegisterFragment();
        } else if (uri.compareTo(Constants.SHOW_NEW_ARTIST_INFO) == 0) {
            switchToArtistInfoFragment();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri returnData = intent.getData();
        String uriFrag = returnData.getFragment();
        HashMap<String, String> map = Constants.mapQueryParams(uriFrag);

        AccountManager.getInstance(this).setAccessToken(map.get(Constants.HASH_KEY_ACCESS_TOKEN));
        WebApiManager.getSoundCloudSelf(this, map.get(Constants.HASH_KEY_ACCESS_TOKEN), new WebApiManager.OnObjectResponseListener() {
            @Override
            public void onObjectResponse(JSONObject object) {
                Log.i(getClass().getSimpleName(), "Response = " + object.toString());
                Gson gson = new Gson();
                Type token = new TypeToken<SoundCloudUser>() {
                }.getType();
                try {
                    SoundCloudUser soundCloudUser = gson.fromJson(object.toString(), token);
                    createFirebaseUserFromSoundCloudUser(soundCloudUser);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new WebApiManager.OnErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    public void switchToLoginFragment() {
        replaceFragment(mLoginFragment, "LoginFragTag");
    }

    public void switchToArtistInfoFragment(){replaceFragment(mNewArtistFragment, "ArtistFragTag");}

    public void switchToRegisterFragment() {replaceFragment(mRegisterFragment, "RegisterFragTag");}

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragmentTag);
        if (fm.getFragments() != null) {
            fragmentTransaction.addToBackStack(fragmentTag);
        }
        fragmentTransaction.commit();
    }

    public void createFirebaseUserFromSoundCloudUser(SoundCloudUser soundCloudUser){

    }
}