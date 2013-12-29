package com.ericrgon.postmark.image;

import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.ericrgon.postmark.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class ActionBarIconTarget implements Target {

    private final ActionBar actionBar;
    private final Resources resources;

    public ActionBarIconTarget(ActionBar actionBar,Resources resources) {
        this.actionBar = actionBar;
        this.resources = resources;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(resources,bitmap);
        actionBar.setIcon(bitmapDrawable);
    }

    @Override
    public void onBitmapFailed(Drawable drawable) {
        //Default the action bar to the launcher icon.
        actionBar.setIcon(R.drawable.ic_launcher);
    }

    @Override
    public void onPrepareLoad(Drawable drawable) {
        //Just in case the image is slow to load.
        actionBar.setIcon(R.drawable.ic_launcher);
    }
}
