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
    private Query mFirebasDatabaseReference;
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
    private TextView mEmptyDataPlaceholder;

    public SocialFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_social, container, false);
        mFriendListReyclerView = (RecyclerView) v.findViewById(R.id.user_list);
        mPendingFriendsCard = (CardView) v.findViewById(R.id.pending_friend_container);
        mAcceptFriendRequest = (TextView) v.findViewById(R.id.accept_friend_request);
        mAllRequestsText = (TextView) v.findViewById(R.id.all_friend_requests);


        mAcceptFriendRequest.setOnClickListener(this);
        mAllRequestsText.setOnClickListener(this);

        queryListener = new SearchView.OnQueryTextListener() {
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
        };
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirebaseDatabase = mFirebaseDatabase.getInstance();
        mFirebasDatabaseReference = mFirebaseDatabase.getReference("users").orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mUserFriendsReference = mFirebaseDatabase.getReference("userFriendRequest");
        mUserFriendsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() != 0) {
                    mPendingFriendsCard.setVisibility(View.VISIBLE);
                    mAllRequestsText.setVisibility(View.VISIBLE);


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        friendList = new ArrayList<>();

        mFriendListReyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFriendListReyclerView.setLayoutManager(layoutManager);

        mFriendsAdapter = new FriendsAdapter(getContext(), friendList);
        mFriendListReyclerView.setAdapter(mFriendsAdapter);

        updateFriends();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_social, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
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
        };

        // Assign the queryListener to that action item
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, expandListener);
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
                mListener.onFragmentInteraction(Constants.ACCEPT_FRIEND_REQUEST_URI);
                break;
            case R.id.all_friend_requests:
                mListener.onFragmentInteraction(Constants.GO_TO_ALL_FRIEND_REQUEST_URI);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void updateFriends() {
        mFirebasDatabaseReference.addChildEventListener(new ChildEventListener() {
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
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
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
}
