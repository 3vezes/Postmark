package com.ericrgon.nearbox;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.ericrgon.nearbox.model.Letter;

public class HomeActivity extends BaseFragmentActivity implements LetterListFragment.Callbacks{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerFragment drawerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Nav drawer behaviour
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.open_nav,
                R.string.close_nav){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        Fragment fragment = new LetterGridFragment();

        drawerFragment = new DrawerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.left_drawer,drawerFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onItemSelected(Letter id) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
