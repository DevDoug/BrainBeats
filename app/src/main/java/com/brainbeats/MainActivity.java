package com.brainbeats;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import adapters.BeatAdapter;
import model.Beat;

public class MainActivity extends AppCompatActivity {

    private Toolbar mDashboardToolbar;
    //TODO: Replace dummy data with data returned by sound cloud.
    List<Beat> beatList = new ArrayList<>();
    private RecyclerView mBeatsGrid;
    private BeatAdapter mBeatAdapter;
    private GridLayoutManager mBeatGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBeatsGrid = (RecyclerView) findViewById(R.id.beats_grid);

        mDashboardToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDashboardToolbar.setNavigationIcon(R.drawable.ic_menu_white);
        mDashboardToolbar.setTitle(R.string.dashboard_title);
        setSupportActionBar(mDashboardToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mBeatsGrid = (RecyclerView) findViewById(R.id.beats_grid);

        mBeatAdapter = new BeatAdapter(this,beatList);
        mBeatGridLayoutManager = new GridLayoutManager(getApplicationContext(),4);
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

    public void getAlbumData(){
        beatList.add(new Beat());
        beatList.get(0).setBeatTitle("Beat Title One");
        beatList.add(new Beat());
        beatList.get(1).setBeatTitle("Beat Title Two");
        beatList.add(new Beat());
        beatList.get(2).setBeatTitle("Beat Title Three");
        mBeatAdapter.notifyDataSetChanged();
    }
}

