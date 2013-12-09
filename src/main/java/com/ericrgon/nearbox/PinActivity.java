package com.ericrgon.nearbox;

import android.content.Intent;
import android.os.Bundle;

import butterknife.Views;

/**
 * Captures a four digit pin from the user.
 */
public class PinActivity extends BaseFragmentActivity {

    public static final String NEW_PIN = "newPin";
    public static final String PIN_DATA = "pin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        Views.inject(this);
        Intent intent = new Intent();
        intent.putExtra(PinActivity.PIN_DATA,"1234");

        setResult(RESULT_OK, intent);
        finish();
    }
}
