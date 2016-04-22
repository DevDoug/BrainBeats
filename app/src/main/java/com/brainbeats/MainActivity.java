package com.brainbeats;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import adapters.BeatAdapter;
import architecture.BaseActivity;
import model.Beat;
import utils.Constants;

public class MainActivity extends BaseActivity {

    List<Beat> beatList = new ArrayList<>();
    private RecyclerView mBeatsGrid;
    private BeatAdapter mBeatAdapter;
    private GridLayoutManager mBeatGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBeatsGrid = (RecyclerView) findViewById(R.id.beats_grid);

        Toolbar mDashboardToolbar = getToolBar();
        mDashboardToolbar.setTitle(R.string.dashboard_title);

        mBeatAdapter = new BeatAdapter(this,beatList);
        mBeatGridLayoutManager = new GridLayoutManager(getApplicationContext(), Constants.GRID_SPAN_COUNT);
        mBeatsGrid.setLayoutManager(mBeatGridLayoutManager);
        mBeatsGrid.setAdapter(mBeatAdapter);

        getAlbumData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //TODO: Replace dummy data with real data from sound cloud
    public void getAlbumData(){
        beatList.add(new Beat());
        beatList.get(0).setBeatTitle("Focus");
        beatList.get(0).setBeatAlbumCoverArt(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder));
        beatList.add(new Beat());
        beatList.get(1).setBeatTitle("Meditation");
        beatList.get(1).setBeatAlbumCoverArt(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder));
        beatList.add(new Beat());
        beatList.get(2).setBeatTitle("Relaxation");
        beatList.get(2).setBeatAlbumCoverArt(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder));
        beatList.add(new Beat());
        beatList.get(3).setBeatTitle("Yoga");
        beatList.get(3).setBeatAlbumCoverArt(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder));
        mBeatAdapter.notifyDataSetChanged();
    }


    public void loadBeatDetailFragment(){
/*        FragmentManager fragManager = getSupportFragmentManager();
        DashboardDetailFragment dashDetailFragment = new DashboardDetailFragment();
        FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
        fragmentTransaction.add(dashDetailFragment, )*/

    }
}

