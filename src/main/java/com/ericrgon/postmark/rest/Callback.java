package com.ericrgon.postmark.rest;

import com.ericrgon.postmark.BaseFragmentActivity;
import com.ericrgon.postmark.events.UnauthorizedEvent;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class Callback<T> implements retrofit.Callback<T>{
    @Override
    public void success(T t, Response response) {}

    @Override
    public void failure(RetrofitError retrofitError) {
        // Unauthorized
        if(retrofitError.getResponse() != null && retrofitError.getResponse().getStatus() == 401){
            //Let inform the base activity of this.
            BaseFragmentActivity.getBus().post(new UnauthorizedEvent(retrofitError));
        }
    }
}
