package com.ericrgon.postmark.events;


import retrofit.RetrofitError;

public class UnauthorizedEvent {

    private final RetrofitError retrofitError;

    public UnauthorizedEvent(RetrofitError retrofitError) {
        this.retrofitError = retrofitError;
    }

    public RetrofitError getRetrofitError() {
        return retrofitError;
    }
}
