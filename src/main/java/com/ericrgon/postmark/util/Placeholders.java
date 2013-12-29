package com.ericrgon.postmark.util;

import com.ericrgon.postmark.R;
import com.google.common.collect.Lists;

import java.util.List;

public final class Placeholders {

    private static int counter;
    private static List<Integer> colors = Lists.newArrayList(R.color.blue);

    public static int getItemId(){
        return colors.get(++counter % colors.size());
    }
}
