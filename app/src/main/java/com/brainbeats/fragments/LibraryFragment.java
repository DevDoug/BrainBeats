package com.brainbeats.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brainbeats.R;

import com.brainbeats.adapters.ViewPagerAdapter;

import com.brainbeats.utils.Constants;

public class LibraryFragment extends Fragment {

    public TabLayout mTabLayout;
    public ViewPager mViewPager;
    private OnFragmentInteractionListener mListener;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_library, container, false);
        mTabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) v.findViewById(R.id.base_viewpager);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewPager();
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(LibraryTabFragment.newInstance(Constants.LibraryDataType.Songs,""), getResources().getStringArray(R.array.library_tab_titles)[0]);
        adapter.addFragment(LibraryTabFragment.newInstance(Constants.LibraryDataType.Playlists,""), getResources().getStringArray(R.array.library_tab_titles)[1]);
        adapter.addFragment(LibraryTabFragment.newInstance(Constants.LibraryDataType.Favorites,""), getResources().getStringArray(R.array.library_tab_titles)[2]);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void updateTabFilter(String query) {
        LibraryTabFragment frag = (LibraryTabFragment) ((ViewPagerAdapter) mViewPager.getAdapter()).getItem(mViewPager.getCurrentItem());
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_EXTRA_LIBRARY_FILTER_TEXT,query);
        getLoaderManager().restartLoader(Constants.LibraryDataType.Songs.getCode(), bundle, frag);
    }
}
