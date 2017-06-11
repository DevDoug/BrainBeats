package com.brainbeats.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.brainbeats.R;
import com.brainbeats.architecture.AccountManager;

public class SettingFragment extends Fragment {

    public static final String TAG = "SettingsFragment";

    public CheckBox mIncogCheckBox;
    public CheckBox mSyncWithSoundCloud;
    private OnFragmentInteractionListener mListener;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        //mIncogCheckBox = (CheckBox) v.findViewById(R.id.is_incognito_checkbox);
        mSyncWithSoundCloud = (CheckBox) v.findViewById(R.id.sync_with_sound_cloud);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mIncogCheckBox.setChecked(AccountManager.getInstance(getContext()).getIsIncognito());
        mSyncWithSoundCloud.setChecked(AccountManager.getInstance(getContext()).getIsSyncedToSoundCloud());
/*
        mIncogCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AccountManager.getInstance(getContext()).isInCognito(mIncogCheckBox.isChecked());
            }
        });*/

        mSyncWithSoundCloud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //called when the up affordance/carat in actionbar is pressed
                AccountManager.getInstance(getContext()).setSyncToSoundCloud(mSyncWithSoundCloud.isChecked());
            }
        });
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
}
