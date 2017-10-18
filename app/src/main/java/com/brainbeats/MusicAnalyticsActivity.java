package com.brainbeats;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.fragments.MusicAnalyticsFragment;

public class MusicAnalyticsActivity extends BaseActivity implements View.OnClickListener,MusicAnalyticsFragment.OnFragmentInteractionListener {

    Fragment mMusicAnalyticsFragment;

    public FloatingActionButton mMainActionFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (savedInstanceState == null) {
            mMusicAnalyticsFragment = new MusicAnalyticsFragment();
            switchToMusicAnalyticsFragment();
        }

        mMainActionFab = (FloatingActionButton) findViewById(R.id.main_action_fob);

        mMainActionFab.setImageDrawable(getDrawable(R.drawable.ic_m));
        mMainActionFab.setOnClickListener(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    public void switchToMusicAnalyticsFragment(){
        replaceFragment(mMusicAnalyticsFragment, mMusicAnalyticsFragment.getTag());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_action_fob:
                Toast.makeText(this, "This is where your music's analytics will be displayed ! ", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
