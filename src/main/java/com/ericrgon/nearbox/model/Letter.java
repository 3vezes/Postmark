package com.ericrgon.nearbox.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Letter implements Serializable{

    @SerializedName("pages")
    private List<Page> pages = new ArrayList<Page>();

    @SerializedName("barcode_zip")
    private long zip;

    public List<Page> getPages() {
        return pages;
    }

    public long getZip() {
        return zip;
    }

    @Override
    public String toString() {
        return "Letter{" +
                "pages=" + pages +
                ", zip=" + zip +
                '}';
    }
}
