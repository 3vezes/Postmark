package com.ericrgon.postmark.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.FileProvider;

import com.ericrgon.postmark.model.Letter;
import com.ericrgon.postmark.model.Page;
import com.ericrgon.postmark.util.CacheUtil;
import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ShareAsyncTask extends AsyncTask<Letter,Void,ArrayList<Uri>>{

    private final Context context;
    private final File cacheDirectory;

    public ShareAsyncTask(Context activity) {
        this.context = activity;
        this.cacheDirectory = CacheUtil.getLetterCacheDir(activity);
    }

    @Override
    protected ArrayList<Uri> doInBackground(Letter... params) {
        ArrayList<Uri> cacheURIs = Lists.newArrayList();

        Letter letter = params[0];

        //Download Items to Cache directory.
        for(Page page : letter.getPages()){
            String highestResImageURL = page.getImages().getHighestRes();
            try {
                if(!isCancelled()){
                    File letterTempFile = File.createTempFile("letter",".png",cacheDirectory);
                    FileOutputStream letterOutputStream = new FileOutputStream(letterTempFile);
                    Picasso.with(context).load(highestResImageURL).get().compress(Bitmap.CompressFormat.PNG,100,letterOutputStream);
                    letterOutputStream.close();
                    cacheURIs.add(FileProvider.getUriForFile(context, CacheUtil.LETTER_CACHE_FILE_PROVIDER, letterTempFile));
                }
            } catch (IOException e) { e.printStackTrace(); }
        }

        return cacheURIs;
    }


}
