package com.ericrgon.nearbox.model;

import android.util.DisplayMetrics;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Image implements Serializable{

    @SerializedName("hrefs")
    private Map<Integer,String> images = new HashMap<Integer, String>();

    public Map<Integer, String> getImages() {
        return images;
    }

    public String getLowestRes(DisplayMetrics metrics){
        int displayDPI = metrics.densityDpi;

        List<Integer> sortedDPIList = getSortedDPIList();

        if (displayDPI < DisplayMetrics.DENSITY_HIGH || sortedDPIList.size() == 1) {
            return images.get(sortedDPIList.get(0));
        } else {
            return images.get(sortedDPIList.get(1));
        }
    }

    public String getHighestRes(){
        List<Integer> sortedDPIList = getSortedDPIList();
        int last = sortedDPIList.size() -1;
        return images.get(sortedDPIList.get(last));
    }

    private List<Integer> getSortedDPIList(){
        List<Integer> dpiList = Lists.newArrayList(images.keySet());
        Collections.sort(dpiList);
        return dpiList;
    }

    @Override
    public String toString() {
        return "Image{" +
                "images=" + images +
                '}';
    }
}
