package com.ericrgon.nearbox.util;

import com.ericrgon.nearbox.R;
import com.google.common.collect.Lists;

import java.util.List;

public final class Placeholders {

    private static int counter;
    private static List<Integer> colors = Lists.newArrayList(R.color.blue,
                                                                R.color.green,
                                                                R.color.red,
                                                                R.color.gray);
    public static int getItemId(){
        return colors.get(++counter % colors.size());
    }
}
