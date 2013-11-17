package com.ericrgon.nearbox;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ericrgon.nearbox.model.Letter;
import com.ericrgon.nearbox.model.Session;
import com.ericrgon.nearbox.rest.OutboxMailService;

import java.util.List;

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
                mailService.getMail(OutboxMailService.Status.UNSORTED,session.getSid(),new Callback<List<Letter>>() {
                    @Override
                    public void success(List<Letter> letters, Response response) {
                        Log.d("DEB",letters.toString());
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        int i = 0;
                    }
                });

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(LoginActivity.this,"Invalid Login Credentials",Toast.LENGTH_LONG).show();
            }
        });
    }
}
