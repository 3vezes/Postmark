package com.ericrgon.nearbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.ericrgon.nearbox.image.ActionBarIconTarget;
import com.ericrgon.nearbox.model.Letter;
import com.ericrgon.nearbox.model.Stack;
import com.ericrgon.nearbox.rest.OutboxMailService;
import com.google.common.eventbus.Subscribe;
import com.squareup.picasso.Picasso;

public class HomeActivity extends BaseFragmentActivity implements LetterGridFragment.Callbacks {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerFragment drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Display refresh button
        setRefreshable(true);

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

        drawerFragment = new DrawerFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.left_drawer,drawerFragment).commit();

        statusSelected(new DrawerFragment.StatusSelectedEvent(OutboxMailService.Status.UNSORTED));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onItemSelected(Letter id) {
        Intent letterDetailActivity = new Intent(this,LetterDetailActivity.class);
        letterDetailActivity.putExtra(LetterDetailFragment.LETTER_ID,id);
        startActivity(letterDetailActivity);
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

    @Subscribe
    public void stackSelected(DrawerFragment.StackSelectedEvent item){
        Bundle bundle = new Bundle();
        bundle.putSerializable(LetterGridFragment.FOLDER_ITEM,item.getStack());
        replaceContent(bundle, item.getStack().getLabel());
    }

    @Subscribe
    public void statusSelected(DrawerFragment.StatusSelectedEvent item){
        Bundle bundle = new Bundle();
        bundle.putSerializable(LetterGridFragment.STATUS_ITEM,item.getStatus());
        replaceContent(bundle,item.getStatus().getTitle());
    }

    private void replaceContent(Bundle bundle,String title){
        LetterGridFragment gridFragment = new LetterGridFragment();
        gridFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,gridFragment).commit();
        drawerLayout.closeDrawers();
        setTitle(title);

        if(bundle.containsKey(LetterGridFragment.STATUS_ITEM)){
            //Drawable is locally available. Piece of cake
            OutboxMailService.Status status = (OutboxMailService.Status) bundle.getSerializable(LetterGridFragment.STATUS_ITEM);
            getActionBar().setIcon(status.getDrawable());
        } else if (bundle.containsKey(LetterGridFragment.FOLDER_ITEM)){
            //Drawable is available from the network and must be pulled down.
            Stack stack = (Stack) bundle.getSerializable(LetterGridFragment.FOLDER_ITEM);
            ActionBarIconTarget actionBarIconTarget = new ActionBarIconTarget(getActionBar(),getResources());
            Picasso.with(this).load(stack.getIconURL()).into(actionBarIconTarget);

        }
    }
}
