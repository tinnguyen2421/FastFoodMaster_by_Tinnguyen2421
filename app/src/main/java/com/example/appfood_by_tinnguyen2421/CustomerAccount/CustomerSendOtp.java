package com.example.appfood_by_tinnguyen2421.CustomerAccount;

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

import com.example.appfood_by_tinnguyen2421.BottomNavigation.CustomerFoodPanel_BottomNavigation;
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
public class CustomerSendOtp extends AppCompatActivity {


    String verificationId;
    FirebaseAuth FAuth;
    Button verify, Resend;
    TextView txt;
    String phonenumber;
    EditText entercode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_send_otp);

        phonenumber = getIntent().getStringExtra("phonenumber").trim();
        sendverificationcode(phonenumber);

        entercode = (EditText) findViewById(R.id.phoneno);
        txt = (TextView) findViewById(R.id.text);
        Resend = (Button) findViewById(R.id.Resendotp);
        FAuth = FirebaseAuth.getInstance();
        Resend.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);
        verify = (Button) findViewById(R.id.Verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Resend.setVisibility(View.INVISIBLE);
                String code = entercode.getText().toString().trim();
                if (code.isEmpty() && code.length() < 6) {
                    entercode.setError("Vui lòng nhập đúng code ");
                    entercode.requestFocus();
                    return;
                }
                verifyCode(code);
            }

        });

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txt.setVisibility(View.VISIBLE);
                txt.setText("Gửi lại code sau " + millisUntilFinished / 1000 + " giây");
            }

            @Override
            public void onFinish() {
                Resend.setVisibility(View.VISIBLE);
                txt.setVisibility(View.INVISIBLE);

            }
        }.start();

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Resend.setVisibility(View.INVISIBLE);
                Resendotp(phonenumber);

                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        txt.setVisibility(View.VISIBLE);
                        txt.setText("Gửi lại code sau " + millisUntilFinished / 1000 + " giây");
                    }

                    @Override
                    public void onFinish() {
                        Resend.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.INVISIBLE);

                    }
                }.start();

            }
        });

    }

    private void Resendotp(String phonenumber) {

        sendverificationcode(phonenumber);
    }


    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInwithCredential(credential);
    }

    private void signInwithCredential(PhoneAuthCredential credential) {
        try {
            FAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(CustomerSendOtp.this, CustomerFoodPanel_BottomNavigation.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ReusableCodeForAll.ShowAlert(CustomerSendOtp.this, "Lỗi", task.getException().getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void sendverificationcode(String number) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallBack).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                entercode.setText(code);
                verifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(CustomerSendOtp.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                number,
//                60,
//                TimeUnit.SECONDS,
//                TaskExecutors.MAIN_THREAD,
//                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    @Override
//                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                        super.onCodeSent(s, forceResendingToken);
//
//                        verificationId = s;
//
//                    }
//
//                    @Override
//                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//
//
//                        String code = phoneAuthCredential.getSmsCode();
//                        if (code != null) {
//                            entercode.setText(code);
//                            verifyCode(code);
//                        }
//                    }
//
//                    @Override
//                    public void onVerificationFailed(FirebaseException e) {
//
//                        Toast.makeText(CustomerSendOtp.this, "THis 2:" + e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }
//        );

}
