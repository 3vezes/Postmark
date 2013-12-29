package com.ericrgon.postmark.model;

import com.google.gson.annotations.SerializedName;

public class Session {

    @SerializedName("expire_after")
    private final int expireAfter;

    @SerializedName("sid")
    private final String sid;

    @SerializedName("username")
    private final String userName;

    public Session(int expireAfter, String sid, String userName) {
        this.expireAfter = expireAfter;
        this.sid = sid;
        this.userName = userName;
    }

    public int getExpireAfter() {
        return expireAfter;
    }

    public String getSid() {
        return sid;
    }

    public String getUserName() {
        return userName;
    }
}
