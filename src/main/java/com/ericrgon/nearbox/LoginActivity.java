package com.ericrgon.nearbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ericrgon.nearbox.model.Session;
import com.ericrgon.nearbox.rest.OutboxMailService;

import butterknife.InjectView;
import butterknife.Views;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends FragmentActivity{

    @InjectView(R.id.username)
    EditText username;

    @InjectView(R.id.password)
    EditText password;

    @InjectView(R.id.login)
    Button login;

    private OutboxMailService mailService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Views.inject(this);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(OutboxMailService.URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mailService = restAdapter.create(OutboxMailService.class);
    }


    public void login(View view) {
        mailService.authenticate(username.getText().toString(),password.getText().toString(),new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                Intent letterList = new Intent(LoginActivity.this,LetterListActivity.class);
                letterList.putExtra(LetterListActivity.SESSION,session.getSid());
                startActivity(letterList);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(LoginActivity.this,"Invalid Login Credentials",Toast.LENGTH_LONG).show();
            }
        });
    }
}
