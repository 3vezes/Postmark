package com.ericrgon.nearbox.model;

import com.google.gson.annotations.SerializedName;

public class Stack {

    @SerializedName("_id")
    private final String id;

    @SerializedName("label")
    private final String label;

    @SerializedName("icon")
    private final String bills;

    @SerializedName("total")
    private final String total;

    public Stack(String id, String label, String bills, String total) {
        this.id = id;
        this.label = label;
        this.bills = bills;
        this.total = total;
    }
}
