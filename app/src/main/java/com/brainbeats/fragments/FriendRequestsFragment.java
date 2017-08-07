package com.brainbeats.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.R;
import com.brainbeats.adapters.FriendsAdapter;
import com.brainbeats.model.BrainBeatsUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendRequestsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FriendRequestsFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFirebasDatabaseReference;

    private ArrayList<BrainBeatsUser> friendRequestList;
    private RecyclerView mFriendRequestReyclerView;
    private FriendsAdapter mFriendsRequestAdapter;

    private TextView mAcceptFriendRequest;
    private TextView mEmptyDataPlaceholder;

    private OnFragmentInteractionListener mListener;

    public FriendRequestsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friend_requests, container, false);
        mFriendRequestReyclerView = (RecyclerView) v.findViewById(R.id.friend_request_list);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirebaseDatabase = mFirebaseDatabase.getInstance();
        mFirebasDatabaseReference = mFirebaseDatabase.getReference("userFriendRequest");

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
