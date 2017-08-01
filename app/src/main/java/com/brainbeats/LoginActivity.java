package com.brainbeats;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.brainbeats.fragments.ArtistInfoFragment;
import com.brainbeats.fragments.LoginFragment;
import com.brainbeats.fragments.RegisterFragment;
import com.brainbeats.utils.Constants;

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
            mNewArtistFragment = new ArtistInfoFragment();
            switchToLoginFragment();
        }
    }

    public void switchToLoginFragment() {
        replaceFragment(mLoginFragment, "LoginFragTag");
    }

    public void switchToArtistInfoFragment(){replaceFragment(mNewArtistFragment, "ArtistFragTag");}

    public void switchToRegisterFragment() {
        replaceFragment(mRegisterFragment, "RegisterFragTag");
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragmentTag);
        if (fm.getFragments() != null) {
            fragmentTransaction.addToBackStack(fragmentTag);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.compareTo(Constants.GO_TO_REGISTER_NEW_USER_URI) == 0) {
            switchToRegisterFragment();
        } else if (uri.compareTo(Constants.SHOW_NEW_ARTIST_INFO) == 0) {
            switchToArtistInfoFragment();
        }
    }
}