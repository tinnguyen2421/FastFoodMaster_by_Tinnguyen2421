package com.example.appfood_by_tinnguyen2421.Account;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.DesignPattern.Proxy.PhoneAuthProxy;
import com.example.appfood_by_tinnguyen2421.MainMenu;
import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class VerifyPhone extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth FAuth;
    private Button verify;
    private Button resend;
    private TextView countdownText;
    private EditText enterCode;
    private String phoneNumber;
    private PhoneAuthProxy phoneAuthProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_verify_phone);

        FAuth = FirebaseAuth.getInstance();
        phoneAuthProxy = new PhoneAuthProxy(this);

        phoneNumber = getIntent().getStringExtra("phonenumber").trim();

        enterCode = findViewById(R.id.phoneno);
        countdownText = findViewById(R.id.text);
        resend = findViewById(R.id.Resendotp);
        verify = findViewById(R.id.Verify);
        //statusText = findViewById(R.id.statusText);

        resend.setVisibility(View.INVISIBLE);
        countdownText.setVisibility(View.INVISIBLE);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend.setVisibility(View.INVISIBLE);
                String code = enterCode.getText().toString().trim();
                if ( code.length() < 6) { // Kiểm tra xem mã xác thực đã được nhập hay chưa
                    enterCode.setError("Vui lòng nhập đúng code");
                    enterCode.requestFocus();
                }
                else
                {
                    verifyCode(code);
                }

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode();
            }
        });

        startCountdownTimer();
        sendVerificationCode();
    }

    private void verifyCode(String code) {
        phoneAuthProxy.verifyCode(verificationId, code, new PhoneAuthProxy.VerificationCallback() {
            @Override
            public void onSuccess() {

                Intent intent = new Intent(VerifyPhone.this, MainMenu.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message) {
            }
        });
    }

    private void resendVerificationCode() {
        startCountdownTimer();
        sendVerificationCode();
    }

    private void startCountdownTimer() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownText.setVisibility(View.VISIBLE);
                countdownText.setText("Gửi lại Code sau " + millisUntilFinished / 1000 + " giây");
            }

            @Override
            public void onFinish() {
                resend.setVisibility(View.VISIBLE);
                countdownText.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    private void sendVerificationCode() {
        phoneAuthProxy.sendVerificationCode(phoneNumber, new PhoneAuthProxy.VerificationSentCallback() {
            @Override
            public void onCodeSent(String verificationId) {
                VerifyPhone.this.verificationId = verificationId;
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}

