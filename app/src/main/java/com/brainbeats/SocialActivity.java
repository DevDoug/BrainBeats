package com.brainbeats;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brainbeats.adapters.FriendsAdapter;
import com.brainbeats.adapters.FriendsSearchAdapter;
import com.brainbeats.architecture.Application;
import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.fragments.FriendRequestsFragment;
import com.brainbeats.fragments.SocialFragment;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.FriendRequest;
import com.brainbeats.utils.Constants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SocialActivity extends BaseActivity implements SocialFragment.OnFragmentInteractionListener,
        FriendRequestsFragment.OnFragmentInteractionListener, View.OnClickListener {

    //Data
    private FirebaseDatabase mFirebaseDatabase;

    FirebaseRecyclerAdapter adapter;
    Query query = FirebaseDatabase.getInstance().getReference().child("users");

    Fragment mSocialFragment;
    Fragment mFriendRequestsFragment;

    AlertDialog friendUserDialog;
    LinearLayout addArtistContainer;
    RecyclerView mFriendSearchRecyclerView;
    TextView mArtistTitle;
    Button mAddFriendButton;
    CoordinatorLayout mCoordinatorLayout;

    BrainBeatsUser mAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        if (savedInstanceState == null) {
            mSocialFragment = new SocialFragment();
            mFriendRequestsFragment = new FriendRequestsFragment();
            switchToSocialFragment();
        }

        mMainActionFab = findViewById(R.id.main_action_fob);
        mCoordinatorLayout = findViewById(R.id.main_content_coordinator_layout);

        mMainActionFab.setImageDrawable(getDrawable(R.drawable.ic_add_white));
        mMainActionFab.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        navigateUpOrBack(this, fm);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.compareTo(Constants.GO_TO_ALL_FRIEND_REQUEST_URI) == 0) {
            switchToFriendRequestsFragment();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri, String userId) {
        if (uri.compareTo(Constants.ACCEPT_FRIEND_REQUEST_URI) == 0) {
            acceptFriendRequest(userId);
        }
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

    public void switchToSocialFragment() {
        replaceFragment(mSocialFragment, mSocialFragment.getTag());
    }

    public void switchToFriendRequestsFragment() {
        replaceFragment(mFriendRequestsFragment, mFriendRequestsFragment.getTag());
    }

    public void showAddFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_search_dialog, null);
        addArtistContainer = dialogView.findViewById(R.id.add_artist_container);
        mFriendSearchRecyclerView = dialogView.findViewById(R.id.friend_search__list);
        mArtistTitle = dialogView.findViewById(R.id.artist_name);
        mAddFriendButton = dialogView.findViewById(R.id.add_friend_button);
        EditText search = dialogView.findViewById(R.id.friend_search);

        mFriendSearchRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFriendSearchRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<BrainBeatsUser> options =
                new FirebaseRecyclerOptions.Builder<BrainBeatsUser>()
                        .setQuery(query, BrainBeatsUser.class)
                        .build();

        adapter = new FriendsSearchAdapter(this, options);
        mFriendSearchRecyclerView.setAdapter(adapter);

        adapter.startListening();

        mAddFriendButton.setOnClickListener(v -> addFriendRequest());
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    searchForFriends(search.getText().toString());
                }
                return true;
            }
        });

        builder.setView(dialogView);
        friendUserDialog = builder.create();
        friendUserDialog.show();
    }

    public void searchForFriends(String searchTerm) {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users").child("artistName").equalTo(searchTerm);

        mFriendSearchRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFriendSearchRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<BrainBeatsUser> options = new FirebaseRecyclerOptions
                        .Builder<BrainBeatsUser>()
                        .setQuery(query, BrainBeatsUser.class)
                        .build();

        adapter = new FriendsSearchAdapter(this, options);
        mFriendSearchRecyclerView.setAdapter(adapter);

        adapter.startListening();
    }

    public void populateAddArtistCard(BrainBeatsUser data) {
        mAddUser = data;
        mArtistTitle.setText(mAddUser.getUserName());
        mFriendSearchRecyclerView.setVisibility(View.GONE);
        addArtistContainer.setVisibility(View.VISIBLE);
    }

    public void addFriendRequest() {
        FriendRequest request = new FriendRequest("pending",FirebaseAuth.getInstance().getCurrentUser().getUid(),"RvMaDClGqohUHrYDgKp9u5i03OI3");

        DatabaseReference friendRequest = mFirebaseDatabase.getReference("friend_requests/" + "RvMaDClGqohUHrYDgKp9u5i03OI3");
        friendRequest
                .push()
                .setValue(request)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        friendUserDialog.dismiss();
                        Constants.buildInfoDialog(SocialActivity.this, "Request Sent", "Your friend request has been sent.");
                        sendFriendNotification();
                    } else {
                        Constants.buildInfoDialog(SocialActivity.this, "Error", "There was an issue when sending that request.");
                    }
                });
    }

    public void acceptFriendRequest(String userId) {
        //Add friend record to both users
        DatabaseReference currentUserRef = mFirebaseDatabase.getInstance().getReference().child("users/" + userId);
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BrainBeatsUser user = dataSnapshot.getValue(BrainBeatsUser.class);
                DatabaseReference friendsOfCurrentUser = mFirebaseDatabase.getReference("friends/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                friendsOfCurrentUser.push().setValue(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        DatabaseReference friendsOfSendingUser = mFirebaseDatabase.getReference("friends/" + userId);
        friendsOfSendingUser.push().setValue(((Application) this.getApplication()).getUserDetails());

        //Delete friend request record
/*        FirebaseDatabase Database = FirebaseDatabase.getInstance();
        Query reference = Database
                .getReference("friend_request/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild("userId")
                .equalTo(user.getUserId());


        reference.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null)
                    dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/

    }

    public void sendFriendNotification() {}
}