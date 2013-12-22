package com.ericrgon.nearbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.Views;

/**
 * Captures a four digit pin from the user.
 */
public class PinActivity extends BaseFragmentActivity {

    public static final String NEW_PIN = "newPin";
    public static final String PIN_DATA = "pin";

    private char[] pin = new char[4];

    private int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        Views.inject(this);
    }


    public void onDigitClicked(View view) {
        TextView digit = (TextView) view;

        if(digit.getId() == R.id.back){
            //User pressed delete.
            current--;
        } else {
            pin[current] = digit.getText().charAt(0);
            current++;
        }

        if(current == 4){
            Intent intent = new Intent();
            intent.putExtra(PinActivity.PIN_DATA,new String(pin));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
