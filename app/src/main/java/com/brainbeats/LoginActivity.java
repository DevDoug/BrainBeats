package com.brainbeats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.brainbeats.fragments.LoginFragment;
import com.brainbeats.fragments.RegisterFragment;

/**
 * Login screen
 * Should allow the user to login with both sound cloud and with Brain Beats
 */
public class LoginActivity extends AppCompatActivity {

    public static String TAG = "LoginActivity";

    public Fragment mLoginFragment;
    public Fragment mRegisterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            mLoginFragment = new LoginFragment();
            mRegisterFragment = new RegisterFragment();
            switchToLoginFragment();
        }
    }

    public void switchToLoginFragment() {
        replaceFragment(mLoginFragment, "LoginFragTag");
    }

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
}