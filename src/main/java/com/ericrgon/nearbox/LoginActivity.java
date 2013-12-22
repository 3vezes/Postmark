package com.ericrgon.nearbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ericrgon.nearbox.model.Session;
import com.ericrgon.nearbox.rest.Callback;

import javax.crypto.SecretKey;

import butterknife.InjectView;
import butterknife.Views;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends BaseFragmentActivity {

    public static final int GENERATED_PIN = 0;
    public static final int CONFIRM_PIN = 1;
    public static final int VALIDATE_PIN = 2;

    public static final String FIRST_PIN = "firstPin";

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

        if (isPasswordSave()) {
            Intent pinValidateIntent = new Intent(this, PinActivity.class);
            startActivityForResult(pinValidateIntent, VALIDATE_PIN);
        }

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || event.getAction() == KeyEvent.ACTION_DOWN) {
                    login.performClick();
                }

                return false;
            }
        });

        loginCallback = new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                super.success(session, response);
                if (rememberMe.isChecked()) {
                    //Fire the intent to create and capture the users pin.
                    Intent pinIntent = buildPinIntentWithMessage(R.string.please_set_your_pin);
                    startActivityForResult(pinIntent, GENERATED_PIN);
                } else {
                    startHomeActivity();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                super.failure(retrofitError);
                Toast.makeText(LoginActivity.this, getString(R.string.invalid_login_credentials), Toast.LENGTH_LONG).show();
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
        if (requestCode == GENERATED_PIN && resultCode == RESULT_OK && data.hasExtra(PinActivity.PIN_DATA)) {
            //Store the pin and ask for a confirmation
            Intent pinIntent = buildPinIntentWithMessage(R.string.please_confirm_your_pin);
            pinIntent.putExtra(FIRST_PIN,data.getStringExtra(PinActivity.PIN_DATA));
            startActivityForResult(pinIntent, CONFIRM_PIN);
        } else if (requestCode == CONFIRM_PIN && resultCode == RESULT_OK && data.hasExtra(PinActivity.PIN_DATA)) {
            //Get the pin number
            String firstPin = data.getStringExtra(FIRST_PIN);
            String secondPin = data.getStringExtra(PinActivity.PIN_DATA);

            if(firstPin.equals(secondPin)){
                //Accept the pin and generate a new set of keys.
                SecretKey key = generateKey(firstPin);
                encrypt(key, ENCRYPTED_USER_PREF, username.getText().toString());
                encrypt(key, ENCRYPTED_PASSWORD_PREF, password.getText().toString());

                startHomeActivity();
            } else {
                Toast.makeText(this,"PINs don't match try again.",Toast.LENGTH_LONG).show();
                Intent pinIntent = buildPinIntentWithMessage(R.string.please_set_your_pin);
                startActivityForResult(pinIntent,GENERATED_PIN);
            }
        } else if (requestCode == VALIDATE_PIN && resultCode == RESULT_OK && data.hasExtra(PinActivity.PIN_DATA)) {
            //Get the pin number.
            String pin = data.getStringExtra(PinActivity.PIN_DATA);

            //Authenticate with pin.
            authenticate(pin, loginCallback);
        }
    }

    private Intent buildPinIntentWithMessage(int message){
        Intent pinIntent = new Intent(this,PinActivity.class);
        pinIntent.putExtra(PinActivity.PIN_MESSAGE,message);
        return pinIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Don't bother using anything from the base class. We're not logged in yet. 
        return true;
    }

    public void login(View view) {
        authenticate(username.getText().toString(), password.getText().toString(), loginCallback);
    }

    private void startHomeActivity() {
        Intent letterList = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(letterList);
        finish();

    }
}
