package architecture;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import com.brainbeats.R;
import utils.Constants;
import java.util.ArrayList;

/**
 * Created by Douglas on 4/21/2016.
 */
public class BaseActivity extends AppCompatActivity {

    private DrawerLayout mNavigationDrawer;
    private Toolbar mToolBar;
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();
    public ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNavDrawer();
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void setUpNavDrawer(){
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawer.setStatusBarBackgroundColor(getResources().getColor(R.color.theme_primary_dark_color));

        mDrawerToggle = new ActionBarDrawerToggle(this,mNavigationDrawer, mToolBar, R.string.nav_drawer_open_text, R.string.nav_drawer_closed_text);
        mNavigationDrawer.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle.syncState();

        if (mToolBar != null) {
            mToolBar.setNavigationIcon(R.drawable.ic_ab_drawer);
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigationDrawer.openDrawer(GravityCompat.START);
                }
            });
        }

        populateNavDrawer();
    }

    public Toolbar getToolBar(){
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(R.drawable.ic_navigation_drawer);
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigationDrawer.openDrawer(GravityCompat.START);
                }
            });
        }
        return mToolBar;
    }

    private void populateNavDrawer() {
        mNavDrawerItems.add(Constants.NAVDRAWER_ITEM_DASHBOARD);
        mNavDrawerItems.add(Constants.NAVDRAWER_ITEM_LIBRARY);
        mNavDrawerItems.add(Constants.NAVDRAWER_ITEM_MIXER);
        mNavDrawerItems.add(Constants.NAVDRAWER_ITEM_SOCIAL);
    }

    protected boolean isNavDrawerOpen() {
        return mNavigationDrawer != null && mNavigationDrawer.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mNavigationDrawer != null) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        }
    }

    public  void toggleNavDrawerIcon(){
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.syncState();
    }
}