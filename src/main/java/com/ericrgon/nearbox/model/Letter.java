package com.ericrgon.nearbox.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Letter {

    @SerializedName("pages")
    private List<Page> pages = new ArrayList<Page>();

    @SerializedName("barcode_zip")
    private long zip;

    @Override
    public String toString() {
        return "Letter{" +
                "pages=" + pages +
                ", zip=" + zip +
                '}';
    }
}
