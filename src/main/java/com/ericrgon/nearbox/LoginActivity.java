package com.ericrgon.nearbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ericrgon.nearbox.model.Session;

import butterknife.InjectView;
import butterknife.Views;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends BaseFragmentActivity {

    @InjectView(R.id.username)
    EditText username;

    @InjectView(R.id.password)
    EditText password;

    @InjectView(R.id.login)
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Views.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Don't bother using anything from the base class. We're not logged in yet. 
        return true;
    }

    public void login(View view) {
        authenticate(username.getText().toString(),password.getText().toString(),new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                Intent letterList = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(letterList);
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(LoginActivity.this,"Invalid Login Credentials",Toast.LENGTH_LONG).show();
            }
        });
    }
}
