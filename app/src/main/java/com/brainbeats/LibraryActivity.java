package com.brainbeats;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;

import java.util.ArrayList;

import architecture.BaseActivity;

public class LibraryActivity extends BaseActivity implements LibraryFragment.OnFragmentInteractionListener {

    Fragment mLibraryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mLibraryFragment = new LibraryFragment();
        switchToLibraryFragment();
    }

    public void switchToLibraryFragment(){
        replaceFragment(mLibraryFragment, mLibraryFragment.getTag());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
