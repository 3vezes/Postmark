package com.ericrgon.nearbox.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Image {

    @SerializedName("hrefs")
    Map<Integer,String> images = new HashMap<Integer, String>();

    @Override
    public String toString() {
        return "Image{" +
                "images=" + images +
                '}';
    }
}
