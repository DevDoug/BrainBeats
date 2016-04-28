package architecture;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

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
    private Toolbar mActionBarToolbar;     // Primary toolbar and drawer toggle
    private ViewGroup mDrawerItemsListContainer;
    private View[] mNavDrawerItemViews = null;     // views that correspond to each navdrawer item, null if not yet created
    private ActionBarDrawerToggle mDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNavDrawer();
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void setUpNavDrawer(){
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawer.setStatusBarBackgroundColor(getResources().getColor(R.color.theme_primary_dark_color));
        mDrawerToggle = new ActionBarDrawerToggle(this,mNavigationDrawer,R.string.nav_drawer_open_text, R.string.nav_drawer_closed_text);

        if (mActionBarToolbar != null) {
            mActionBarToolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
            mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigationDrawer.openDrawer(GravityCompat.START);
                }
            });
        }

        populateNavDrawer();


/*        mNavigationDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                // run deferred action, if we have one
*//*                if (mDeferredOnDrawerClosedRunnable != null) {
                    mDeferredOnDrawerClosedRunnable.run();
                    mDeferredOnDrawerClosedRunnable = null;
                }
                if (mAccountBoxExpanded) {
                    mAccountBoxExpanded = false;
                    setupAccountBoxToggle();
                }
                onNavDrawerStateChanged(false, false);*//*
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //onNavDrawerStateChanged(true, false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //onNavDrawerStateChanged(isNavDrawerOpen(), newState != DrawerLayout.STATE_IDLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //onNavDrawerSlide(slideOffset);
            }
        });

        //mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // populate the nav drawer with the correct items
        populateNavDrawer();*/
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
        createNavDrawerItems();
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void createNavDrawerItems() {
/*        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
        if (mDrawerItemsListContainer == null) {
            return;
        }

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemsListContainer.removeAllViews();
        int i = 0;
        for (int itemId : mNavDrawerItems) {
            mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
            mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
            ++i;
        }*/
    }
/*
    private View makeNavDrawerItem(final int itemId, ViewGroup container) {
        NavDrawerItemView item = (NavDrawerItemView) getLayoutInflater().inflate(R.layout.navdrawer_item, container, false);
        item.setContent(NAVDRAWER_ICON_RES_ID[itemId], NAVDRAWER_TITLE_RES_ID[itemId]);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavDrawerItemClicked(itemId);
            }
        });
        return item;
    }*/
}
