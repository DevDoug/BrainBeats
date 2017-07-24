package com.brainbeats;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.fragments.SocialFragment;
import com.brainbeats.fragments.UserProfileFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SocialActivity extends BaseActivity implements SocialFragment.OnFragmentInteractionListener,  View.OnClickListener {

    //Data
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFirebasDatabaseReference;

    Fragment mSocialFragment;
    Fragment mUserProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mFirebaseDatabase = mFirebaseDatabase.getInstance();
        mFirebasDatabaseReference = mFirebaseDatabase.getReference("users");

        mMainActionFab = (FloatingActionButton) findViewById(R.id.main_action_fob);

        if (savedInstanceState == null) {
            mSocialFragment = new SocialFragment();
            mUserProfileFragment = new UserProfileFragment();
            switchToSocialFragment();
        }

        mMainActionFab.setImageDrawable(getDrawable(R.drawable.ic_add_white));
        mMainActionFab.setOnClickListener(this);
    }

    public void switchToSocialFragment() {
        replaceFragment(mSocialFragment, mSocialFragment.getTag());
    }

    public void switchToUserProfileFragment() {
        replaceFragment(mUserProfileFragment, mUserProfileFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_global, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_action_fob:
                showAddFriendDialog();
                break;
        }
    }

    public void showAddFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = ((Activity) this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_search_dialog, null);
        SearchView search = (SearchView) dialogView.findViewById(R.id.friend_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
    }
}