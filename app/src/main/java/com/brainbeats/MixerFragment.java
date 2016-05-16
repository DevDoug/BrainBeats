package com.brainbeats;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.BeatAdapter;
import adapters.MixerAdapter;
import model.Beat;
import utils.Constants;

public class MixerFragment extends Fragment implements View.OnClickListener {

    List<Beat> beatList = new ArrayList<>();
    private RecyclerView mMixerList;
    private MixerAdapter mMixerAdapter;
    private OnFragmentInteractionListener mListener;
    private RecyclerView.LayoutManager mLayoutManager;
    public FloatingActionButton mAddNewBeatButton;

    public MixerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mixer, container, false);
        mMixerList = (RecyclerView) v.findViewById(R.id.mixer_list);
        ((TextView)v.findViewById(R.id.separator_title)).setText("History");
        mAddNewBeatButton = (FloatingActionButton) v.findViewById(R.id.mixer_fob);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMixerAdapter = new MixerAdapter(getContext(),beatList);
        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        mMixerList.setLayoutManager(mLayoutManager);
        mMixerList.setAdapter(mMixerAdapter);

        mAddNewBeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBeatMix();
            }
        });
        getBeatData();
    }

    //TODO: Replace dummy data with real data from sound cloud
    public void getBeatData(){
        beatList.add(new Beat());
        beatList.get(0).setBeatTitle("Focus");
        beatList.add(new Beat());
        beatList.get(1).setBeatTitle("Meditation");
        beatList.add(new Beat());
        beatList.get(2).setBeatTitle("Relaxation");
        beatList.add(new Beat());
        beatList.get(3).setBeatTitle("Yoga");
        mMixerAdapter.notifyDataSetChanged();
    }

    public void addNewBeatMix(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = (View) inflater.inflate(R.layout.custom_dialog_layout, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText(getContext().getString(R.string.new_beat_title));
        ((TextView)dialogView.findViewById(R.id.option_1)).setText(getResources().getStringArray(R.array.new_beat_options)[0]);
        ((TextView)dialogView.findViewById(R.id.option_1)).setOnClickListener(this);
        ((TextView)dialogView.findViewById(R.id.option_2)).setText(getResources().getStringArray(R.array.new_beat_options)[1]);
        ((TextView)dialogView.findViewById(R.id.option_2)).setOnClickListener(this);
        ((TextView)dialogView.findViewById(R.id.option_3)).setText(getResources().getStringArray(R.array.new_beat_options)[2]);
        ((TextView)dialogView.findViewById(R.id.option_3)).setOnClickListener(this);
        ((TextView)dialogView.findViewById(R.id.option_4)).setText(getResources().getStringArray(R.array.new_beat_options)[3]);
        ((TextView)dialogView.findViewById(R.id.option_4)).setOnClickListener(this);
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.option_1:
                Toast.makeText(getContext(), "Mixing from search", Toast.LENGTH_LONG).show();
                break;
            case R.id.option_2:
                Toast.makeText(getContext(), "Mixing from popular", Toast.LENGTH_LONG).show();
                break;
            case R.id.option_3:
                Toast.makeText(getContext(), "Mixing from library", Toast.LENGTH_LONG).show();
                break;
            case R.id.option_4:
                Toast.makeText(getContext(), "Mixing from two songs", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}