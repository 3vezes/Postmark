package com.ericrgon.nearbox.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Stack implements Serializable{

    @SerializedName("_id")
    private final String id;

    @SerializedName("label")
    private final String label;

    @SerializedName("icon")
    private final String icon;

    @SerializedName("total")
    private final String total;

    public Stack(String id, String label, String icon, String total) {
        this.id = id;
        this.label = label;
        this.icon = icon;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }

    public String getTotal() {
        return total;
    }
}
