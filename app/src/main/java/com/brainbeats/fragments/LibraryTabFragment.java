package com.brainbeats.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.R;
import com.brainbeats.adapters.LibraryMixAdapter;
import com.brainbeats.adapters.LibraryPlaylistAdapter;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.data.BrainBeatsDbHelper;
import com.brainbeats.entity.Track;
import com.brainbeats.model.Mix;
import com.brainbeats.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class LibraryTabFragment extends Fragment {


    //Data
    private FirebaseDatabase mFirebaseDatabase;
    private Query mFirebasDatabaseReference;

    private ArrayList<Mix> mixList;
    private RecyclerView mMixRecyclerView;
    private LibraryMixAdapter mLibraryMixAdapter;
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
        mFirebasDatabaseReference = mFirebaseDatabase.getReference("mixes/" + FirebaseAuth.getInstance().getCurrentUser().getUid());//.equalTo("SfiyOZBXB6S8hjC4qvE2TLcvahu2");

        mixList = new ArrayList<>();

        mMixRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMixRecyclerView.setLayoutManager(layoutManager);

        mLibraryMixAdapter = new LibraryMixAdapter(getContext(), mixList);
        mMixRecyclerView.setAdapter(mLibraryMixAdapter);

        updateMixes();
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
                int index = getItemIndex(mix);
                mixList.set(index, mix);
                mLibraryMixAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Mix mix = dataSnapshot.getValue(Mix.class);
                int index = getItemIndex(mix);
                mixList.remove(index);
                mLibraryMixAdapter.notifyItemRemoved(index);
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
}
