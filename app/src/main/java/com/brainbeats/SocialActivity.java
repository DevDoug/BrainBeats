package com.brainbeats;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.fragments.SocialFragment;
import com.brainbeats.fragments.UserProfileFragment;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.FriendRequest;
import com.brainbeats.model.Mix;
import com.brainbeats.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SocialActivity extends BaseActivity implements SocialFragment.OnFragmentInteractionListener,  View.OnClickListener {

    //Data
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersReference;
    private DatabaseReference mUserFriendsReference;

    Fragment mSocialFragment;
    Fragment mUserProfileFragment;

    AlertDialog friendUserDialog;
    LinearLayout addArtistContainer;
    TextView mArtistTitle;
    Button mAddFriendButton;

    BrainBeatsUser mAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (savedInstanceState == null) {
            mSocialFragment = new SocialFragment();
            mUserProfileFragment = new UserProfileFragment();
            switchToSocialFragment();
        }

        mFirebaseDatabase = mFirebaseDatabase.getInstance();
        mUsersReference = mFirebaseDatabase.getReference("users");

        mMainActionFab = (FloatingActionButton) findViewById(R.id.main_action_fob);
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
        addArtistContainer = (LinearLayout) dialogView.findViewById(R.id.add_artist_container);
        mArtistTitle = (TextView) dialogView.findViewById(R.id.artist_name);
        mAddFriendButton = (Button) dialogView.findViewById(R.id.add_friend_button);
        EditText search = (EditText) dialogView.findViewById(R.id.friend_search);
        TextView.OnEditorActionListener searchListener = (searchView, actionId, event) -> {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                searchForFriends(searchView.getText().toString());
            }
            return true;
        };
        mAddFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendRequest();
            }
        });
        search.setOnEditorActionListener(searchListener);
        builder.setView(dialogView);
        friendUserDialog = builder.create();
        friendUserDialog.show();
    }

    public void searchForFriends(String searchTerm){
        mUsersReference.orderByChild(searchTerm).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                populateAddArtistCard(dataSnapshot);
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

    public void populateAddArtistCard(DataSnapshot data){
        mAddUser = data.getValue(BrainBeatsUser.class);
        mArtistTitle.setText(mAddUser.getUserName());
        addArtistContainer.setVisibility(View.VISIBLE);
    }

    public void addFriendRequest(){
        DatabaseReference friendRequest = mFirebaseDatabase.getReference("FriendRequest");

        Map<String, Object> request = new HashMap<String, Object>();
        request.put(mAddUser.getUserId(), new FriendRequest(FirebaseAuth.getInstance().getCurrentUser().getUid(), mAddUser.getUserId(), FriendRequest.FriendRequestStatus.Pending));

        friendRequest.updateChildren(request, (databaseError, databaseReference) -> {
            if(databaseError != null) { //if there was an error tell the user
                Constants.buildInfoDialog(SocialActivity.this, "Error", "There was an issue when sending that request.");
            } else {
                friendUserDialog.dismiss();
                Constants.buildInfoDialog(SocialActivity.this, "Request Sent", "Your friend request has been sent.");
            }
        });
    }
}