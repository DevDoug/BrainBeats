package com.brainbeats.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.R;
import com.brainbeats.adapters.LibraryMixAdapter;
import com.brainbeats.adapters.LibraryPlaylistAdapter;
import com.brainbeats.model.Mix;
import com.brainbeats.model.Playlist;
import com.brainbeats.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LibraryTabFragment extends Fragment {


    //Data
    private FirebaseDatabase mFirebaseDatabase;
    private Query mFirebasDatabaseReference;

    private ArrayList<Mix> mixList;
    private ArrayList<Playlist> playLists;
    private RecyclerView mMixRecyclerView;
    private LibraryMixAdapter mLibraryMixAdapter;
    private LibraryPlaylistAdapter mLibraryPlaylistAdapter;
    public int mDataType;
    public String mFilter = "";
    private TextView mEmptyDataPlaceholder;

    public LibraryTabFragment() {
        // Required empty public constructor
    }

    public static LibraryTabFragment newInstance(Constants.LibraryDataType dataType, String tabFilter) {
        LibraryTabFragment tabFragment = new LibraryTabFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_LIBRARY_DATA_TYPE, dataType.getCode());
        args.putString(Constants.KEY_EXTRA_LIBRARY_FILTER_TEXT, tabFilter);
        tabFragment.setArguments(args);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mDataType = args.getInt(Constants.KEY_LIBRARY_DATA_TYPE);
            mFilter = args.getString(Constants.KEY_EXTRA_LIBRARY_FILTER_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_library_tab, container, false);
        mMixRecyclerView = (RecyclerView) v.findViewById(R.id.library_content_list);
        mEmptyDataPlaceholder = (TextView) v.findViewById(R.id.empty_text);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirebaseDatabase = mFirebaseDatabase.getInstance();

        mixList = new ArrayList<>();
        playLists = new ArrayList<>();

        mMixRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMixRecyclerView.setLayoutManager(layoutManager);

        if(mDataType == 0)
            mFirebasDatabaseReference = mFirebaseDatabase.getReference("mixes/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        else if(mDataType == 1)
            mFirebasDatabaseReference = mFirebaseDatabase.getReference("playlists/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        else
            mFirebasDatabaseReference = mFirebaseDatabase.getReference("mixes_favorites/" + FirebaseAuth.getInstance().getCurrentUser().getUid());


        if(mDataType == 0 || mDataType == 2) {
            mLibraryMixAdapter = new LibraryMixAdapter(getContext(), mixList);
            mMixRecyclerView.setAdapter(mLibraryMixAdapter);
            updateMixes();
        } else {
            mLibraryPlaylistAdapter = new LibraryPlaylistAdapter(getContext(), playLists);
            mMixRecyclerView.setAdapter(mLibraryPlaylistAdapter);
            updatePlaylists();
        }

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


    public void updateMixes() {
        mFirebasDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mixList.add(dataSnapshot.getValue(Mix.class));
                mLibraryMixAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Mix mix = dataSnapshot.getValue(Mix.class);
                int index = getMixItemIndex(mix);
                mixList.set(index, mix);
                mLibraryMixAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Mix mix = dataSnapshot.getValue(Mix.class);
                int index = getMixItemIndex(mix);
                mixList.remove(index);
                mLibraryMixAdapter.notifyItemRemoved(index);

                if(index == 1){
                    mEmptyDataPlaceholder.setVisibility(View.VISIBLE);
                    mMixRecyclerView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void updatePlaylists(){
        mFirebasDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                playLists.add(dataSnapshot.getValue(Playlist.class));
                mLibraryPlaylistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Playlist playlist = dataSnapshot.getValue(Playlist.class);
                int index = getPlaylistItemIndex(playlist);
                playLists.set(index, playlist);
                mLibraryMixAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Playlist playlist = dataSnapshot.getValue(Playlist.class);
                int index = getPlaylistItemIndex(playlist);
                playLists.remove(index);
                mLibraryPlaylistAdapter.notifyItemRemoved(index);

                if(index == 1){
                    mEmptyDataPlaceholder.setVisibility(View.VISIBLE);
                    mMixRecyclerView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    private int getMixItemIndex(Mix mix) {
        int index = -1;

        for (int i = 0; i < mixList.size(); i++) {
            if (mixList.get(i).getMixTitle().equalsIgnoreCase(mix.getMixTitle())) {
                index = i;
            }
        }

        return index;
    }

    private int getPlaylistItemIndex(Playlist playlist) {
        int index = -1;

        for (int i = 0; i < playLists.size(); i++) {
            if (playLists.get(i).getPlaylistTitle().equalsIgnoreCase(playlist.getPlaylistTitle())) {
                index = i;
            }
        }

        return index;
    }
}
