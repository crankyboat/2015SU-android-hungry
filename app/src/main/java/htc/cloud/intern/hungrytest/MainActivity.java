package htc.cloud.intern.hungrytest;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import htc.cloud.intern.hungrytest.dailymatch.DailyMatchFragment;
import htc.cloud.intern.hungrytest.dailymatch.editorFrag;
import htc.cloud.intern.hungrytest.nearby.MapFragment;


public class MainActivity extends ActionBarActivity {


    public static FragmentManager mFragmentManager;
    public UserState mUserState;
    private Toolbar mToolbar;
    private Context mContext;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    // TODO
    private ArrayList<Fragment> mNavigationFragments;
    private htc.cloud.intern.hungrytest.nearbyapi.MapFragment mNearbyApiFragment;
    private PlaceholderFragment mFavoriteFragment;
    private DailyMatchFragment mDailyMatchFragment;
    private PlaceholderFragment mPlaceholderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mUserState = new UserState(this.getBaseContext());
        mFragmentManager = getSupportFragmentManager();

        // Setup Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Setup Navigation
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setUpNavigationFragments();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override public boolean onNavigationItemSelected(MenuItem menuItem) {

//                Toast.makeText(mContext, menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();

                Fragment selectedFragment;
                switch (menuItem.getItemId()) {
                    case R.id.drawer_nearbyapi:
                        selectedFragment = mNearbyApiFragment;
                        break;
                    case R.id.drawer_favourite:
                        selectedFragment = mFavoriteFragment;
                        break;
                    case R.id.drawer_match:
                        selectedFragment = mDailyMatchFragment;
                        break;
                    default:
                        selectedFragment = mPlaceholderFragment;
                }
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, selectedFragment)
                        .commit();
//                mFragmentManager.beginTransaction()
//                        .show(selectedFragment)
//                        .commit();

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                return true;
            }
        });
        /*
        mFragmentManager.beginTransaction()
                .replace(R.id.container, mNearbyApiFragment)
                .commit();
//        mFragmentManager.beginTransaction()
//                .show(mNearbyFragment)
//                .commit();
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSectionAttached(TextView textView, CharSequence name) {

        if (textView != null) {
            Toast.makeText(this, textView.toString(), Toast.LENGTH_LONG).show();
            textView.setText(name);
        }

    }

    private void setUpNavigationFragments() {

        // TODO
        mNearbyApiFragment = htc.cloud.intern.hungrytest.nearbyapi.MapFragment.newInstance(0);
        mFavoriteFragment = PlaceholderFragment.newInstance("Favorite");
        mDailyMatchFragment = DailyMatchFragment.newInstance("Daily Match");
        mPlaceholderFragment = PlaceholderFragment.newInstance("Placeholder");

//        mFragmentManager.beginTransaction()
//                .add(R.id.container, mNearbyApiFragment)
//                .add(R.id.container, mFavoriteFragment)
//                .add(R.id.container, mDailyMatchFragment)
//                .add(R.id.container, mPlaceholderFragment)
//                .hide(mNearbyFragment)
//                .hide(mNearbyApiFragment)
//                .hide(mFavoriteFragment)
//                .hide(mDailyMatchFragment)
//                .hide(mPlaceholderFragment)
//                .commit();

    }

}