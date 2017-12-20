package com.brainbeats.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.LoginActivity;
import com.brainbeats.R;
import com.brainbeats.adapters.FriendsAdapter;
import com.brainbeats.architecture.AccountManager;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.FriendRequest;
import com.brainbeats.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SocialFragment extends Fragment implements View.OnClickListener {

    private FirebaseDatabase mFirebaseDatabase;
    private Query mFriendsReference;
    private Query mUserFriendsReference;

    private String mQueryText = "";

    private OnFragmentInteractionListener mListener;
    private SearchView.OnQueryTextListener queryListener;

    private ArrayList<BrainBeatsUser> friendList;
    private RecyclerView mFriendListReyclerView;
    private FriendsAdapter mFriendsAdapter;
    private CardView mPendingFriendsCard;
    private TextView mAcceptFriendRequest;
    private TextView mAllRequestsText;
    private TextView mAddFriendArtistName;
    private TextView mAddFriendArtistDescription;
    private TextView mEmptyDataPlaceholder;

    String friendToAddId;

    public SocialFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_social, container, false);
        mFriendListReyclerView = v.findViewById(R.id.user_list);
        mPendingFriendsCard = v.findViewById(R.id.pending_friend_container);
        mAcceptFriendRequest = v.findViewById(R.id.accept_friend_request);
        mAllRequestsText = v.findViewById(R.id.all_friend_requests);
        mAddFriendArtistName = v.findViewById(R.id.artist_name);
        mAddFriendArtistDescription = v.findViewById(R.id.artist_description);
        mEmptyDataPlaceholder = v.findViewById(R.id.empty_text);


        mAcceptFriendRequest.setOnClickListener(this);
        mAllRequestsText.setOnClickListener(this);

/*        queryListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQueryText = query;
                Cursor usersCursor = getActivity().getContentResolver().query(
                        BrainBeatsContract.UserEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null, // select args
                        null
                );

                usersCursor.close();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };*/
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFriendsReference = mFirebaseDatabase.getReference("friends/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        friendList = new ArrayList<>();

        mFriendListReyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFriendListReyclerView.setLayoutManager(layoutManager);

        mFriendsAdapter = new FriendsAdapter(getContext(), friendList);
        mFriendListReyclerView.setAdapter(mFriendsAdapter);

        mFriendsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    mEmptyDataPlaceholder.setVisibility(View.VISIBLE);
                    mFriendListReyclerView.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        updateFriends();
        searchForPendingFriendRequests();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_social, menu);
/*        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(queryListener);

        if (!mQueryText.equalsIgnoreCase("")) {
            searchMenuItem.expandActionView();
            ((SearchView) searchMenuItem.getActionView()).setQuery(mQueryText, true);
        }

        // Define the queryListener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mQueryText = "";
                getActivity().getContentResolver().query(BrainBeatsContract.CONTENT_URI_RAW_QUERY,
                        null,
                        null,  //return everything
                        null, //raw query sql
                        null, // select args
                        null
                );

                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };*/

        // Assign the queryListener to that action item
        //MenuItemCompat.setOnActionExpandListener(searchMenuItem, expandListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AccountManager.getInstance(getContext()).forceLogout(getContext());
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.accept_friend_request:
                mListener.onFragmentInteraction(Constants.ACCEPT_FRIEND_REQUEST_URI, friendToAddId);
                break;
            case R.id.all_friend_requests:
                mListener.onFragmentInteraction(Constants.GO_TO_ALL_FRIEND_REQUEST_URI);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void onFragmentInteraction(Uri uri, String userId);
    }

    public void updateFriends() {
        mFriendsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                BrainBeatsUser user = dataSnapshot.getValue(BrainBeatsUser.class);
                if(user != null && user.getUserId() != null) {
                    if(!user.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        friendList.add(user);
                        mFriendsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                BrainBeatsUser user = dataSnapshot.getValue(BrainBeatsUser.class);
                if(user != null) {
                    if(!user.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        int index = getItemIndex(user);
                        friendList.set(index, user);
                        mFriendsAdapter.notifyItemChanged(index);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                BrainBeatsUser user = dataSnapshot.getValue(BrainBeatsUser.class);
                if(user != null) {
                    if(!user.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        int index = getItemIndex(user);
                        friendList.remove(index);
                        mFriendsAdapter.notifyItemRemoved(index);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private int getItemIndex(BrainBeatsUser user) {
        int index = -1;

        for (int i = 0; i < friendList.size(); i++) {
            if (friendList.get(i).getUserId().equalsIgnoreCase(user.getUserId())) {
                index = i;
            }
        }

        return index;
    }

    public void searchForPendingFriendRequests(){
        mUserFriendsReference = mFirebaseDatabase.getReference("friend_requests/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        mUserFriendsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() != 0) {

                    mPendingFriendsCard.setVisibility(View.VISIBLE);
                    mAllRequestsText.setVisibility(View.VISIBLE);

                    for(DataSnapshot child: dataSnapshot.getChildren()) {
                        FriendRequest friendRequest = child.getValue(FriendRequest.class);
                        friendToAddId = friendRequest.getSenderId();
                        populateCurrentAddFriendData(friendRequest);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void populateCurrentAddFriendData(FriendRequest friendRequest){
        DatabaseReference userRef = mFirebaseDatabase.getInstance().getReference().child("users/" + friendRequest.senderId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BrainBeatsUser user = dataSnapshot.getValue(BrainBeatsUser.class);
                mAddFriendArtistName.setText(user.getArtistName());
                mAddFriendArtistDescription.setText(user.getArtistDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
