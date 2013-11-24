package com.ericrgon.nearbox.rest;

import com.ericrgon.nearbox.model.Letter;
import com.ericrgon.nearbox.model.Session;
import com.ericrgon.nearbox.model.Stack;
import com.ericrgon.nearbox.model.Todo;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface OutboxMailService {

    public static final String URL = "https://outbox-carrier.herokuapp.com";

    @FormUrlEncoded
    @POST("/session")
    public void authenticate(@Field("username") String username,@Field("password") String password, Callback<Session> sessionCallback);

    public enum Status {UNSORTED("unsorted", "Inbox"),TODO("todo", "To-Do"),REQUESTED("requested","Requested");
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

    @POST("/v0/mail/{id}/todo")
    public void todo(@Path("id") int letterID, @Query("_") long timeStamp, @Body Todo todo, Callback<Letter> callback);

    @POST("/v0/mail/{id}/request")
    public void request(@Path("id") int letterID, @Query("_") long timeStamp, Callback<Letter> callback);

    @GET("/v0/mail")
    public void getMail(@Query("status") Status status, Callback<List<Letter>> callback);

    @GET("/v0/mail")
    public void getStack(@Query("stack") String stackName, Callback<List<Letter>> callback);

    @GET("/v0/stacks")
    public void getStacks(Callback<List<Stack>> callback);

    @POST("/v0/mail/{id}/shred")
    public void shred(@Path("id") int letterID,Callback<Letter> callback);

    /**
     * @param stackName The destination stack name.
     * @param letterID The letter id to move.
     * @param timestamp Invoke System System.currentTimeMillis() . Still trying to figure out a way to remove this.
     */
    @POST("/v0/stacks/{stackName}/{letterID}?_method=PUT")
    public void moveLetterToStack(@Path("stackName") String stackName, @Path("letterID") int letterID, @Query("_") long timestamp,Callback<Void> callback);
}
