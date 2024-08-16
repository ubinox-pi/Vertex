package com.vertex.io;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Timer;

public class Otp extends Dialog {

    private int otp;
    private EditText text1;
    private EditText text2;
    private EditText text3;
    private EditText text4;
    private EditText text5;
    private EditText text6;
    private Button verify;

    private final Timer timer = new Timer();

    private String number;

    private final int resendTime = 29;
    private boolean isResendEnabled = false;

    private int selectedETposition = 0;

    private TextView resendBtn;
    private LinearLayout resendOtp;
    private TextView timeOtp;
    private TextView phoneNumber;

    public Otp(@NonNull Context context, String setNumber) {
        super(context);
        setNumber(setNumber);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_layout);

        resendBtn = findViewById(R.id.code_resend);
        resendOtp = findViewById(R.id.resend_otp);
        timeOtp = findViewById(R.id.time_otp);
        phoneNumber = findViewById(R.id.phone_number);
        phoneNumber.setText("+91"+number);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        verify = findViewById(R.id.verify);

        text1.addTextChangedListener(textWatcher);
        text2.addTextChangedListener(textWatcher);
        text3.addTextChangedListener(textWatcher);
        text4.addTextChangedListener(textWatcher);
        text5.addTextChangedListener(textWatcher);
        text6.addTextChangedListener(textWatcher);

        showKeyboard(text1);
        startCountDown();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("resend_otp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("resend_otp", 3);
        editor.apply();

        resendBtn.setOnClickListener(v ->{
            int resend_otp = sharedPreferences.getInt("resend_otp", 0);
            if (resend_otp>0)
            {
                resend_otp--;
                editor.putInt("resend_otp", resend_otp);
                editor.apply();
                startCountDown();
            }
            else
            {
                Toast.makeText(getContext(), "You have exceeded the maximum limit", Toast.LENGTH_LONG).show();
            }
        });


        verify.setOnClickListener(v -> {
            otp = Integer.parseInt(text1.getText().toString() + text2.getText().toString() + text3.getText().toString() + text4.getText().toString() + text5.getText().toString() + text6.getText().toString());
            if (text1.getText().toString().isEmpty() && text2.getText().toString().isEmpty() && text3.getText().toString().isEmpty() && text4.getText().toString().isEmpty() && text5.getText().toString().isEmpty() && text6.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please Enter OTP", Toast.LENGTH_SHORT).show();
            }
            if (otp == 123456) {
                Toast.makeText(getContext(), "OTP Verified", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length()>0)
            {
                if (selectedETposition == 0)
                {
                    selectedETposition = 1;
                    showKeyboard(text2);
                }
                else if (selectedETposition == 1)
                {
                    selectedETposition = 2;
                    showKeyboard(text3);
                }
                else if (selectedETposition == 2)
                {
                    selectedETposition = 3;
                    showKeyboard(text4);
                }
                else if (selectedETposition == 3)
                {
                    selectedETposition = 4;
                    showKeyboard(text5);
                }
                else if (selectedETposition == 4)
                {
                    selectedETposition = 5;
                    showKeyboard(text6);
                }
            }
        }
    };

    private void showKeyboard(EditText text1) {
        text1.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(text1, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void startCountDown(){
        isResendEnabled = false;
        CountDownTimer countDownTimer = new CountDownTimer(resendTime*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendBtn.setVisibility(View.GONE);
                resendOtp.setVisibility(View.VISIBLE);
                final Handler handler = new Handler(getContext().getApplicationContext().getMainLooper());
                handler.post(() ->{
                    timeOtp.setText(String.valueOf(Integer.parseInt(timeOtp.getText().toString()) - 1));
                });
            }

            @Override
            public void onFinish() {
                timer.cancel();
                timeOtp.setText("29");
                resendBtn.setVisibility(View.VISIBLE);
                resendOtp.setVisibility(View.GONE);
            }
        }.start();
    }

    public void setNumber(String number)
    {
        this.number = number;
        setPhoneNumber();
    }

    private void setPhoneNumber()
    {

//        phoneNumber = findViewById(R.id.phone_number);
//        phoneNumber.setText("+91"+number);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DEL)
        {
            if (selectedETposition == 6)
            {
                selectedETposition = 5;
                showKeyboard(text5);
            }
            else if (selectedETposition == 5)
            {
                selectedETposition = 4;
                showKeyboard(text4);
            }
            else if (selectedETposition == 4)
            {
                selectedETposition = 3;
                showKeyboard(text3);
            }
            else if (selectedETposition == 3)
            {
                selectedETposition = 2;
                showKeyboard(text2);
            }
            else if (selectedETposition == 2)
            {
                selectedETposition = 1;
                showKeyboard(text1);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
