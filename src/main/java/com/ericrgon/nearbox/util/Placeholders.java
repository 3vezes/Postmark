package com.ericrgon.nearbox.util;

import com.ericrgon.nearbox.R;
import com.google.common.collect.Lists;

import java.util.List;

public final class Placeholders {

    private static int counter;
    private static List<Integer> colors = Lists.newArrayList(R.color.android_blue,
                                                                R.color.android_green,
                                                                R.color.android_purple,
                                                                R.color.android_yellow,
                                                                R.color.android_red);
    public static int getItemId(){
        return colors.get(++counter % colors.size());
    }
}
