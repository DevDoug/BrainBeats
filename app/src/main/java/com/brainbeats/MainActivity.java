package com.brainbeats;

import android.location.Location;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import architecture.BaseActivity;
import entity.Track;
import fragments.DashboardDetailFragment;
import fragments.DashboardFragment;
import utils.BeatLearner;
import web.WebApiManager;

public class MainActivity extends BaseActivity implements DashboardFragment.OnFragmentInteractionListener, DashboardDetailFragment.OnFragmentInteractionListener{

    Fragment mDashboardFragment;
    Fragment mDashboardDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mDashboardFragment = new DashboardFragment();
        mDashboardDetailFragment = new DashboardDetailFragment();
        switchToDashboardFragment();
    }

    public void switchToDashboardFragment(){
        replaceFragment(mDashboardFragment, mDashboardFragment.getTag());
    }

    public void loadBeatDetailFragment(){
        toggleNavDrawerIcon();
        replaceFragment(mDashboardDetailFragment, mDashboardDetailFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
