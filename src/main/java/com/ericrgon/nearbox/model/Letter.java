package com.ericrgon.nearbox.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Letter implements Serializable{

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEEE\nMMM d");

    @SerializedName("_id")
    private int identifier;

    @SerializedName("pages")
    private List<Page> pages = new ArrayList<Page>();

    @SerializedName("barcode_zip")
    private long zip;

    @SerializedName("delivered")
    private long deliveredDate;

    public List<Page> getPages() {
        return pages;
    }

    public long getZip() {
        return zip;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getDeliveredDate(){
        return DATE_FORMAT.format(new Date(deliveredDate * 1000));
    }

    @Override
    public String toString() {
        return "Letter{" +
                "pages=" + pages +
                ", zip=" + zip +
                '}';
    }


}
