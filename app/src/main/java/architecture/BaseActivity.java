package architecture;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
    }

    public Toolbar getToolBar(){
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(R.drawable.ic_ab_drawer);
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
        //createNavDrawerItems();
    }

    protected boolean isNavDrawerOpen() {
        return mNavigationDrawer != null && mNavigationDrawer.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mNavigationDrawer != null) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        }
    }

/*    private void createNavDrawerItems() {
        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
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
        }
    }*/

}
