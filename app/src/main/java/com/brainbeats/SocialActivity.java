package com.brainbeats;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.fragments.SocialFragment;
import com.brainbeats.fragments.UserProfileFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
        EditText search = (EditText) dialogView.findViewById(R.id.friend_search);
        TextView.OnEditorActionListener searchListener = (searchView, actionId, event) -> {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                searchForFriends(searchView.getText().toString());
            }
            return true;
        };
        search.setOnEditorActionListener(searchListener);
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void searchForFriends(String searchTerm){
        mFirebasDatabaseReference.orderByChild(searchTerm).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}