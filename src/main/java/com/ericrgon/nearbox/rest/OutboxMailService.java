package com.ericrgon.nearbox.rest;

import com.ericrgon.nearbox.model.Session;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface OutboxMailService {

    public static final String URL = "https://outbox-carrier.herokuapp.com";

    @FormUrlEncoded
    @POST("/session")
    public void authenticate(@Field("username") String username,@Field("password") String password, Callback<Session> sessionCallback);
}
