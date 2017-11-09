package com.brainbeats;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.brainbeats.architecture.AccountManager;
import com.brainbeats.entity.SoundCloudUser;
import com.brainbeats.fragments.AddNewArtistInfoFragment;
import com.brainbeats.fragments.LoginFragment;
import com.brainbeats.fragments.RegisterFragment;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.utils.Constants;
import com.brainbeats.web.WebApiManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Login screen
 * Should allow the user to login with both sound cloud and with Brain Beats
 */
public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {

    public static String TAG = "LoginActivity";

    FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUserReference;

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

        mFirebaseAuth = FirebaseAuth.getInstance();
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users");;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.compareTo(Constants.GO_TO_REGISTER_NEW_USER_URI) == 0) {
            switchToRegisterFragment();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri, String username, String password) {
        if (uri.compareTo(Constants.CREATE_NEW_USER_URI) == 0) {
            createNewFirebaseUser(username, password);
        }
    }

    public void switchToLoginFragment() {
        replaceFragment(mLoginFragment, "LoginFragTag");
    }

    public void switchToArtistInfoFragment() {
        replaceFragment(mNewArtistFragment, "ArtistFragTag");
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

    public void createNewFirebaseUser(String username, String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                    if (task.isSuccessful()) {
                        mFirebaseAuth.signInWithEmailAndPassword(username, password)
                                .addOnCompleteListener(LoginActivity.this, signInTask -> {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                    addNewBrainBeatsUser(username);

                                    if (!task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        } catch (Exception e) {
                                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                                            Toast.makeText(LoginActivity.this, "Auth Failed:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Log.w(TAG, "Register:failed", task.getException());
                            Constants.buildInfoDialog(LoginActivity.this, "Unable To Create Account", e.getMessage());
                        }
                    }
                });

    }

    public void addNewBrainBeatsUser(String username) {
        String Uid = mFirebaseAuth.getCurrentUser().getUid();
        Map<String, Object> brainBeatsUser = new HashMap<>();
        brainBeatsUser.put(Uid, new BrainBeatsUser(mFirebaseAuth.getCurrentUser().getUid(), username));

        mUserReference.updateChildren(brainBeatsUser, (databaseError, databaseReference) -> {
            if (databaseError != null)  //if there was an error tell the user
                Constants.buildInfoDialog(LoginActivity.this, "Unable To Login", "There was an issue saving that user to the database");
            else
                switchToArtistInfoFragment();
        });
    }
}