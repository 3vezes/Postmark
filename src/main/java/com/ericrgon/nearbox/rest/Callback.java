package com.ericrgon.nearbox.rest;

import com.ericrgon.nearbox.BaseFragmentActivity;
import com.ericrgon.nearbox.events.UnauthorizedEvent;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class Callback<T> implements retrofit.Callback<T>{
    @Override
    public void success(T t, Response response) {}

    @Override
    public void failure(RetrofitError retrofitError) {
        // Unauthorized
        if(retrofitError.getResponse().getStatus() == 401){
            //Let inform the base activity of this.
            BaseFragmentActivity.getBus().post(new UnauthorizedEvent(retrofitError));
        }
    }
}
