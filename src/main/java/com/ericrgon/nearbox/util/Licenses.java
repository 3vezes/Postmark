package com.ericrgon.nearbox.util;

import com.ericrgon.nearbox.R;
import com.ericrgon.nearbox.model.License;
import com.google.common.collect.Lists;

import java.util.List;

public class Licenses {

    public static List<License> getLicenses(){
        return Lists.newArrayList(
                License.newInstance(R.string.android_support_header, R.string.android_support_copyright, R.string.apache_license),
                License.newInstance(R.string.gson_header, R.string.gson_copyright, R.string.apache_license),
                License.newInstance(R.string.retrofit_header, R.string.retrofit_copyright, R.string.apache_license),
                License.newInstance(R.string.butterknife_header, R.string.butterknife_copyright, R.string.apache_license),
                License.newInstance(R.string.picasso_header, R.string.picasso_copyright, R.string.apache_license),
                License.newInstance(R.string.guava_header, R.string.guava_copyright, R.string.apache_license),
                License.newInstance(R.string.photoview_header, R.string.photoview_copyright, R.string.apache_license)
        );

    }
}
