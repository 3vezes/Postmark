package com.ericrgon.nearbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ericrgon.nearbox.model.Session;
import com.ericrgon.nearbox.rest.OutboxMailService;
import com.google.common.eventbus.EventBus;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BaseFragmentActivity extends FragmentActivity {

    private OutboxMailService mailService;

    private final EventBus eventBus = new EventBus();

    private static String sessionID = "";

    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(OutboxMailService.URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade requestFacade) {
                        requestFacade.addQueryParam("sid",sessionID);
                    }
                })
                .build();

        mailService = restAdapter.create(OutboxMailService.class);
    }


    protected OutboxMailService getMailService() {
        return mailService;
    }

    protected void authenticate(String username,String password, final Callback<Session> sessionCallback){
        getMailService().authenticate(username,password,new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                sessionID = session.getSid();
                sessionCallback.success(session,response);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                sessionCallback.failure(retrofitError);
            }
        });
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus.register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                //Launch login.
                Intent login = new Intent(this,LoginActivity.class);
                startActivity(login);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
