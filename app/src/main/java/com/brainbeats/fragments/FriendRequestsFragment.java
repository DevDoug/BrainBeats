package com.brainbeats.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.R;
import com.brainbeats.SocialActivity;
import com.brainbeats.adapters.FriendRequestAdapter;
import com.brainbeats.adapters.FriendsAdapter;
import com.brainbeats.adapters.FriendsSearchAdapter;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.FriendRequest;
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

import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendRequestsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FriendRequestsFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;

    FriendRequestAdapter adapter;
    Query query = FirebaseDatabase.getInstance().getReference().child("friend_requests/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

    private RecyclerView mFriendRequestReyclerView;
    private ArrayList<BrainBeatsUser> friendList;
    private Query mFriendsRequestReference;
    TextView mEmptyDataPlaceholder;

    private OnFragmentInteractionListener mListener;

    public FriendRequestsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friend_requests, container, false);
        mFriendRequestReyclerView = v.findViewById(R.id.friend_request_list);
        mEmptyDataPlaceholder = v.findViewById(R.id.empty_text);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFriendsRequestReference = mFirebaseDatabase.getReference("friend_requests/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        friendList = new ArrayList<>();

        mFriendRequestReyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFriendRequestReyclerView.setLayoutManager(layoutManager);

        adapter = new FriendRequestAdapter(getContext(), friendList);
        mFriendRequestReyclerView.setAdapter(adapter);

        mFriendsRequestReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    mEmptyDataPlaceholder.setVisibility(View.VISIBLE);
                    mFriendRequestReyclerView.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        updateFriendsRequests();
    }

    public void updateFriendsRequests() {
        mFriendsRequestReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FriendRequest request = dataSnapshot.getValue(FriendRequest.class);
                if(request != null) {
                    DatabaseReference userRef = mFirebaseDatabase.getInstance().getReference().child("users/" + request.senderId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            BrainBeatsUser user = dataSnapshot.getValue(BrainBeatsUser.class);
                            friendList.add(user);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                BrainBeatsUser user = dataSnapshot.getValue(BrainBeatsUser.class);
                if(user != null) {
                    if(!user.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        int index = getItemIndex(user);
                        friendList.set(index, user);
                        adapter.notifyItemChanged(index);
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
                        adapter.notifyItemRemoved(index);
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

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        Drawable up = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_up));
        DrawableCompat.setTint(up, getResources().getColor(R.color.theme_primary_text_color));
        toolbar.setNavigationIcon(up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ((SocialActivity) getActivity()).navigateUpOrBack(getActivity(), fm);
            }
        });
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
