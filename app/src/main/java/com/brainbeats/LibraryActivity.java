package com.brainbeats;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import architecture.BaseActivity;

public class LibraryActivity extends BaseActivity {

    Fragment mLibraryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void switchToLibraryFragment(){
        //replaceFragment(mLibraryFragment, mDashboardFragment.getTag());
    }
}
