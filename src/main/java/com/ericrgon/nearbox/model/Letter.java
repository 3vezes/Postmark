package com.ericrgon.nearbox.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Letter implements Serializable{

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEEE MMM d");

    @SerializedName("_id")
    private int identifier;

    @SerializedName("pages")
    private List<Page> pages = new ArrayList<Page>();

    @SerializedName("barcode_zip")
    private long zip;

    @SerializedName("delivered")
    private long deliveredDate;

    @SerializedName("created")
    private long createdDate;

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

    /**
     * An item is achieved if 30 days has passed
     * since it was created.
     * @return
     */
    public boolean isArchived(){
        DateTime now = DateTime.now();
        DateTime created = new DateTime(createdDate * 1000);
        return Days.daysBetween(created, now).getDays() >= 30;
    }

    @Override
    public String toString() {
        return "Letter{" +
                "pages=" + pages +
                ", zip=" + zip +
                '}';
    }


}
