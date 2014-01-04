package com.ericrgon.postmark.util;

import android.content.Context;

import java.io.File;


public class CacheUtil {

    public static final String LETTER_CACHE_FILE_PROVIDER = "com.ericrgon.postmark.fileprovider";

    public static File getLetterCacheDir(Context context){
        File cache = new File(context.getCacheDir() + "/letters/");
        cache.mkdirs();
        return cache;
    }

    public static void clearLetterCacheDir(final Context context){
        new Thread(){
            @Override
            public void run() {
                super.run();
                deleteDirectory(getLetterCacheDir(context));
            }
        }.start();
    }

    private static void deleteDirectory(File directory){
        if(directory.isDirectory()){
            for(String item : directory.list()){
                new File(directory,item).delete();
            }
            directory.delete();
        }
    }
}
