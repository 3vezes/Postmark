package com.ericrgon.postmark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

    @InjectView(R.id.digit1)
    EditText digit1;

    @InjectView(R.id.digit2)
    EditText digit2;

    @InjectView(R.id.digit3)
    EditText digit3;

    @InjectView(R.id.digit4)
    EditText digit4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        Views.inject(this);

        pinMessage.setText(getIntent().getIntExtra(PIN_MESSAGE,R.string.please_enter_your_pin));
    }

    @Override
    protected void onResume() {
        super.onResume();
        current = 0;
    }

    public void onDigitClicked(View view) {
        TextView digit = (TextView) view;

        if(digit.getId() == R.id.back){
            //User pressed delete.
            setDigit(current,"");
            if(current > 0){
                current--;
            }
        } else {
            pin[current] = digit.getText().charAt(0);
            current++;
            setDigit(current,"1");
        }

        if(current == PIN_LENGTH){
            Intent intent = getIntent();
            intent.putExtra(PinActivity.PIN_DATA,new String(pin));
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    private void setDigit(int index, String value){
        switch (index){
            case 1:
                digit1.setText(value);
                break;
            case 2:
                digit2.setText(value);
                break;
            case 3:
                digit3.setText(value);
                break;
            case 4:
                digit4.setText(value);
                break;
        }
    }
}
