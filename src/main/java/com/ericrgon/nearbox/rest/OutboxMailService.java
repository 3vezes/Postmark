package com.ericrgon.nearbox.rest;

import com.ericrgon.nearbox.model.Letter;
import com.ericrgon.nearbox.model.Session;
import com.ericrgon.nearbox.model.Stack;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface OutboxMailService {

    public static final String URL = "https://outbox-carrier.herokuapp.com";

    @FormUrlEncoded
    @POST("/session")
    public void authenticate(@Field("username") String username,@Field("password") String password, Callback<Session> sessionCallback);

    public enum Status {UNSORTED("unsorted", "Inbox"),TODO("todo", "To-Do");
        private final String status;
        private final String title;

        Status(String status, String title) {
            this.status = status;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return status;
        }
    }

    @GET("/v0/mail")
    public void getMail(@Query("status") Status status, Callback<List<Letter>> callback);

    @GET("/v0/mail")
    public void getStack(@Query("stack") String stackName, Callback<List<Letter>> callback);

    @GET("/v0/stacks")
    public void getFolders(Callback<List<Stack>> callback);

}
