package com.ericrgon.nearbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Captures a four digit pin from the user.
 */
public class PinActivity extends BaseFragmentActivity {

    public static final String PIN_DATA = "pin";
    public static final String PIN_MESSAGE = "pinMessage";

    private static final int PIN_LENGTH = 4;

    private final char[] pin = new char[PIN_LENGTH];
    private int current = 0;

    @InjectView(R.id.message)
    TextView pinMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        Views.inject(this);

        pinMessage.setText(getIntent().getIntExtra(PIN_MESSAGE,R.string.please_enter_your_pin));
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

        if(current == PIN_LENGTH){
            Intent intent = getIntent();
            intent.putExtra(PinActivity.PIN_DATA,new String(pin));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
