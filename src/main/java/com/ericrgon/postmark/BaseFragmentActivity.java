package com.ericrgon.postmark;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ericrgon.postmark.events.UnauthorizedEvent;
import com.ericrgon.postmark.model.Session;
import com.ericrgon.postmark.rest.Callback;
import com.ericrgon.postmark.rest.OutboxMailService;
import com.ericrgon.postmark.util.SecurityUtil;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.net.UnknownHostException;

import javax.crypto.SecretKey;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BaseFragmentActivity extends FragmentActivity {

    private static final String CREDENTIALS_PREF_FILE = "CREDENTIALS";

    private static final String SALT_PREF = "salt";

    protected static final String ENCRYPTED_USER_PREF = "user";
    protected static final String ENCRYPTED_PASSWORD_PREF = "password";

    private OutboxMailService mailService;

    private static final EventBus eventBus = new EventBus();

    private boolean isRefreshable = false;
    private boolean isShareable = false;

    private static String sessionID = "";

    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(OutboxMailService.URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade requestFacade) {
                        requestFacade.addQueryParam("sid", sessionID);
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
        authenticate(username, password, sessionCallback);
    }

    protected void authenticate(String username,String password, final Callback<Session> sessionCallback){
        getMailService().authenticate(username,password,new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                super.success(session,response);
                sessionID = session.getSid();
                sessionCallback.success(session,response);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                super.failure(retrofitError);
                sessionCallback.failure(retrofitError);
            }
        });
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu,menu);
        menu.findItem(R.id.menu_refresh).setVisible(isRefreshable);
        menu.findItem(R.id.menu_share).setVisible(isShareable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_about:
                startActivity(new Intent(this,LicenseListActivity.class));
                return true;
            case R.id.menu_logout:
                logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void logout() {
        //Nuke the user preferences.
        SharedPreferences.Editor editor = getSharedPreferences(CREDENTIALS_PREF_FILE, MODE_PRIVATE).edit();
        editor.clear().apply();

        //Launch login.
        startLoginActivity();
    }

    private byte[] getSalt(){
        SharedPreferences preferences = getSharedPreferences(CREDENTIALS_PREF_FILE, MODE_PRIVATE);
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
        SharedPreferences.Editor editor = getSharedPreferences(CREDENTIALS_PREF_FILE, MODE_PRIVATE).edit();
        editor.putString(destinationPreference,Base64.encodeToString(encryptedValue, Base64.DEFAULT));
        editor.apply();
    }

    protected String decrypt(SecretKey key, String destinationPreference){
        SharedPreferences preferences = getSharedPreferences(CREDENTIALS_PREF_FILE, MODE_PRIVATE);
        String encryptedValue = preferences.getString(destinationPreference, "");
        return SecurityUtil.decrypt(key, Base64.decode(encryptedValue,Base64.DEFAULT));
    }

    protected boolean isPasswordSave() {
        SharedPreferences preferences = getSharedPreferences(CREDENTIALS_PREF_FILE, MODE_PRIVATE);
        return preferences.contains(ENCRYPTED_PASSWORD_PREF) && !preferences.getString(ENCRYPTED_PASSWORD_PREF,"").isEmpty();
    }

    public static EventBus getBus(){
        return eventBus;
    }

    @Subscribe
    public void unauthorizedEvent(UnauthorizedEvent unauthorizedEvent){
        if(isPasswordSave()){
            //Attempt to re-authenticate the user
            startLoginActivity();
        } else {
            logout();
        }
    }

    protected void setRefreshable(boolean isRefreshable) {
        this.isRefreshable = isRefreshable;
    }

    protected void setShareable(boolean isShareable) {
        this.isShareable = isShareable;
    }

    protected void toastInvalidCredentials(RetrofitError error){
        if(error.getCause() instanceof UnknownHostException){
            Toast.makeText(this, getString(R.string.network_issue), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.invalid_login_credentials), Toast.LENGTH_SHORT).show();
        }
    }

    private void startLoginActivity() {
        Intent login = new Intent(this,LoginActivity.class);
        startActivity(login);
        finish();
    }
}
