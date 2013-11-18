package com.ericrgon.nearbox.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page implements Serializable{

    @SerializedName("dpi_list")
    private List<Integer> dpis = new ArrayList<Integer>();

    @SerializedName("image")
    private Image images = new Image();

    public List<Integer> getDpis() {
        return dpis;
    }

    public Image getImages() {
        return images;
    }

    @Override
    public String toString() {
        return "Page{" +
                "dpis=" + dpis +
                ", images=" + images +
                '}';
    }
}
