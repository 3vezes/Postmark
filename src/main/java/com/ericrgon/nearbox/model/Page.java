package com.ericrgon.nearbox.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Page {

    @SerializedName("dpi_list")
    private List<Integer> dpis = new ArrayList<Integer>();

    @SerializedName("image")
    private Image images = new Image();

    @Override
    public String toString() {
        return "Page{" +
                "dpis=" + dpis +
                ", images=" + images +
                '}';
    }
}
