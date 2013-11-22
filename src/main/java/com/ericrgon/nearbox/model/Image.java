package com.ericrgon.nearbox.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Image implements Serializable{

    @SerializedName("hrefs")
    private Map<Integer,String> images = new HashMap<Integer, String>();

    public Map<Integer, String> getImages() {
        return images;
    }

    public String getLowestRes(){
        int smallest = Integer.MAX_VALUE;
        for(Integer integer : images.keySet()){
            if(integer < smallest){
                smallest = integer;
            }
        }
        return images.get(smallest);
    }

    @Override
    public String toString() {
        return "Image{" +
                "images=" + images +
                '}';
    }
}
