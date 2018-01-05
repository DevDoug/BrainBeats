package com.brainbeats.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.LoginActivity;
import com.brainbeats.R;
import com.brainbeats.adapters.MixerAdapter;
import com.brainbeats.architecture.AccountManager;
import com.brainbeats.model.Mix;
import com.brainbeats.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MixerFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private Query mFirebasDatabaseReference;

    private ArrayList<Mix> mixList;
    private RecyclerView mMixRecyclerView;
    private MixerAdapter mMixAdapter;
    private TextView mEmptyDataPlaceholder;

    private OnFragmentInteractionListener mListener;
    public AlertDialog mAddOptionsDialog;

    public MixerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mixer, container, false);
        mMixRecyclerView = v.findViewById(R.id.mixer_list);
        mEmptyDataPlaceholder = v.findViewById(R.id.empty_text);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebasDatabaseReference = mFirebaseDatabase
                .getReference("mixes/")
                .orderByChild("artistId")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mixList = new ArrayList<>();

        mMixRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMixRecyclerView.setLayoutManager(layoutManager);

        mMixAdapter = new MixerAdapter(getContext(), mixList);
        mMixRecyclerView.setAdapter(mMixAdapter);

        updateMixes();

        mFirebasDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    mEmptyDataPlaceholder.setVisibility(View.VISIBLE);
                    mMixRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onFragmentInteraction(Constants.MIX_SHOW_FAB);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_global, menu);
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

    public void updateMixes() {
        mFirebasDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mixList.add(dataSnapshot.getValue(Mix.class));
                mMixAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Mix mix = dataSnapshot.getValue(Mix.class);
                int index = getItemIndex(mix);
                mixList.set(index, mix);
                mMixAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Mix mix = dataSnapshot.getValue(Mix.class);
                int index = getItemIndex(mix);
                mixList.remove(index);
                mMixAdapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private int getItemIndex(Mix mix) {
        int index = -1;

        for (int i = 0; i < mixList.size(); i++) {
            if (mixList.get(i).getMixTitle().equalsIgnoreCase(mix.getMixTitle())) {
                index = i;
            }
        }

        return index;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}