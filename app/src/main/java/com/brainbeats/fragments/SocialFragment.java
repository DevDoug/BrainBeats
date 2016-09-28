package com.brainbeats.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brainbeats.R;
import com.brainbeats.adapters.SocialAdapter;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.utils.Constants;

public class SocialFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mUserList;
    private OnFragmentInteractionListener mListener;

    public SocialFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_social, container, false);
        mUserList = (RecyclerView) v.findViewById(R.id.user_list);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(Constants.SOCIAL_LOADER, null, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mUserList.setLayoutManager(layoutManager);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.SOCIAL_LOADER:

                String rawQuery =
                        "SELECT * FROM " + BrainBeatsContract.UserEntry.TABLE_NAME + " INNER JOIN " + BrainBeatsContract.UserFollowersEntry.TABLE_NAME
                                + " ON " + "user." + BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID + " = " + "userfollowers." +
                                BrainBeatsContract.UserFollowersEntry.COLUMN_NAME_USER_FOLLOWER_ID;

                return new CursorLoader(
                        getActivity(),                      // Parent activity context
                        BrainBeatsContract.CONTENT_URI_RAW_QUERY,
                        null,  //return everything
                        rawQuery, //raw query sql
                        null, // select args
                        null                   // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        SocialAdapter mUserAdapter = new SocialAdapter(getContext(), data);
        mUserList.setAdapter(mUserAdapter);
        Log.i("com/brainbeats/data", String.valueOf(data.getColumnCount()));
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}