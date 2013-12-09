package com.ericrgon.nearbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ericrgon.nearbox.model.Session;
import com.ericrgon.nearbox.rest.OutboxMailService;
import com.ericrgon.nearbox.util.SecurityUtil;
import com.google.common.eventbus.EventBus;

import javax.crypto.SecretKey;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BaseFragmentActivity extends FragmentActivity {

    private static final String SALT_PREF = "salt";

    protected static final String ENCRYPTED_USER_PREF = "user";
    protected static final String ENCRYPTED_PASSWORD_PREF = "password";

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

    protected void authenticate(String pin, final Callback<Session> sessionCallback){
        //Decrypt the user information and validate.
        SecretKey key = generateKey(pin);
        String username = decrypt(key,ENCRYPTED_USER_PREF);
        String password = decrypt(key,ENCRYPTED_PASSWORD_PREF);
        authenticate(username,password,sessionCallback);
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

    private byte[] getSalt(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        byte[] salt;
        if(preferences.contains(SALT_PREF)){
            salt = Base64.decode(preferences.getString(SALT_PREF,""),Base64.DEFAULT);
        } else {
            //Generate a new salt if one doesn't exist.
            salt = SecurityUtil.generateSalt().getEncoded();
            SharedPreferences.Editor editor = preferences.edit().putString(SALT_PREF,Base64.encodeToString(salt,Base64.DEFAULT));
            editor.apply();
        }

        return salt;
    }

    protected SecretKey generateKey(String pin){
        byte[] salt = getSalt();
        char[] pinArray = pin.toCharArray();

        SecretKey key = SecurityUtil.generateKey(pinArray, salt);

        return key;
    }

    protected void encrypt(SecretKey key, String destinationPreference, String value){
        byte[] encryptedValue = SecurityUtil.encrypt(key, value);
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(destinationPreference,Base64.encodeToString(encryptedValue, Base64.DEFAULT));
        editor.apply();
    }

    protected String decrypt(SecretKey key, String destinationPreference){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String encryptedValue = preferences.getString(destinationPreference,"");
        return SecurityUtil.decrypt(key, Base64.decode(encryptedValue,Base64.DEFAULT));
    }

    protected boolean isPasswordSave() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        return preferences.contains(ENCRYPTED_PASSWORD_PREF) && !preferences.getString(ENCRYPTED_PASSWORD_PREF,"").isEmpty();
    }
}
