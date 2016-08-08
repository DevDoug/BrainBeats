package architecture;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.brainbeats.LibraryActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;
import com.brainbeats.SettingsActivity;
import com.brainbeats.SocialActivity;

/**
 * Created by Douglas on 4/21/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public DrawerLayout mNavigationDrawer;
    public Toolbar mToolBar;
    public ActionBarDrawerToggle mDrawerToggle;
    public NavigationView mNavView;

    //Feilds for testing sync adapter
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.example.android.datasync";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    public Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccount = CreateSyncAccount(this);

/*        if(AccountManager.getInstance(BaseActivity.this).getGlobalSyncRequired()){
            SyncManager.getInstance().updateAllTables(mAccount, BrainBeatsContract.CONTENT_AUTHORITY);
            AccountManager.getInstance(BaseActivity.this).setGlobalSyncRequired(false);
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //AccountManager.getInstance(BaseActivity.this).setGlobalSyncRequired(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
        mNavView = (NavigationView) findViewById(R.id.navView);
        getToolBar();
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        mNavigationDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragmentTag);
        if (fm.getFragments() != null) {
            fragmentTransaction.addToBackStack(fragmentTag);
        }
        fragmentTransaction.commit();
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
        mDrawerToggle.syncState();
    }

    public void navigateUpOrBack(Activity currentActivity, FragmentManager fm) {
        if(fm.getBackStackEntryCount() >= 1){ //if there are active fragments go up if not go back
            fm.popBackStackImmediate();
            if(fm.getBackStackEntryCount() == 0){
                getToolBar();
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                mDrawerToggle.syncState();
            }
        } else {
            currentActivity.onBackPressed();
        }
    }

    public static Account CreateSyncAccount(Context context) {
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        android.accounts.AccountManager accountManager = (android.accounts.AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            return newAccount;
        } else {
            return accountManager.getAccountsByType(ACCOUNT_TYPE)[0];
        }
    }
}