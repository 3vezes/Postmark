package com.ericrgon.nearbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ericrgon.nearbox.model.Session;

import javax.crypto.SecretKey;

import butterknife.InjectView;
import butterknife.Views;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends BaseFragmentActivity {

    public static final int GENERATED_PIN = 0;
    public static final int VALIDATE_PIN = 1;

    @InjectView(R.id.username)
    EditText username;

    @InjectView(R.id.password)
    EditText password;

    @InjectView(R.id.login)
    Button login;

    @InjectView(R.id.remember)
    CheckBox rememberMe;

    @InjectView(R.id.logoLayout)
    View logoLayout;

    private Callback<Session> loginCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Views.inject(this);

        if(isPasswordSave()){
            Intent pinValidateIntent = new Intent(this,PinActivity.class);
            startActivityForResult(pinValidateIntent,VALIDATE_PIN);
        }

        loginCallback = new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                if(rememberMe.isChecked()){
                    //Fire the intent to create and capture the users pin.
                    Intent pinIntent = new Intent(getApplication(),PinActivity.class);
                    startActivityForResult(pinIntent,GENERATED_PIN);
                } else {
                    startHomeActivity();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(LoginActivity.this,"Invalid Login Credentials",Toast.LENGTH_LONG).show();
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        logoLayout.setAlpha(0);
        logoLayout.animate()
                .alpha(1.0f)
                .setDuration(800)
                .setStartDelay(600)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GENERATED_PIN && resultCode == RESULT_OK && data.hasExtra(PinActivity.PIN_DATA)){
            //Get the pin number
            String pin = data.getStringExtra(PinActivity.PIN_DATA);

            SecretKey key = generateKey(pin);

            encrypt(key ,ENCRYPTED_USER_PREF,username.getText().toString());
            encrypt(key, ENCRYPTED_PASSWORD_PREF,password.getText().toString());

            startHomeActivity();
        } else if(requestCode == VALIDATE_PIN && resultCode == RESULT_OK && data.hasExtra(PinActivity.PIN_DATA)){
            //Get the pin number.
            String pin = data.getStringExtra(PinActivity.PIN_DATA);

            //Authenticate with pin.
            authenticate(pin,loginCallback);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Don't bother using anything from the base class. We're not logged in yet. 
        return true;
    }

    public void login(View view) {
        authenticate(username.getText().toString(),password.getText().toString(),loginCallback);
    }

    private void startHomeActivity(){
        Intent letterList = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(letterList);
        finish();

    }
}
