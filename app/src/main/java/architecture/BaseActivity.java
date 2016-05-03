package architecture;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.brainbeats.DashboardFragment;
import com.brainbeats.LibraryActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;
import com.brainbeats.SettingsActivity;
import com.brainbeats.SocialActivity;

import utils.Constants;
import java.util.ArrayList;

/**
 * Created by Douglas on 4/21/2016.
 */
public class BaseActivity extends AppCompatActivity {

    private DrawerLayout mNavigationDrawer;
    private Toolbar mToolBar;
    public ActionBarDrawerToggle mDrawerToggle;
    public NavigationView mNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getToolBar();
        setUpNavDrawer();
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
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mNavView = (NavigationView) findViewById(R.id.navView);

        setSupportActionBar(mToolBar);
        mDrawerToggle = new ActionBarDrawerToggle(this,  mNavigationDrawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mNavigationDrawer.addDrawerListener(mDrawerToggle);

        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_dashboard:
                        createBackStack(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case R.id.action_library:
                        createBackStack(new Intent(getApplicationContext(), LibraryActivity.class));
                        break;
                    case R.id.action_mixer:
                        createBackStack(new Intent(getApplicationContext(), MixerActivity.class));
                        break;
                    case R.id.action_social:
                        createBackStack(new Intent(getApplicationContext(), SocialActivity.class));
                        break;
                    case R.id.action_settings:
                        createBackStack(new Intent(getApplicationContext(), SettingsActivity.class));
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        mDrawerToggle.syncState();
    }

    public void createBackStack(Intent backStackIntent){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder taskSstackBuilder = TaskStackBuilder.create(this);
            taskSstackBuilder.addNextIntentWithParentStack(backStackIntent);
            taskSstackBuilder.startActivities();
        } else {
            startActivity(backStackIntent);
            finish();
        }
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
    }
}