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

import com.example.appfood_by_tinnguyen2421.BottomNavigation.ChefBottomNavigation;
import com.example.appfood_by_tinnguyen2421.BottomNavigation.CustomerBottomNavigation;
import com.example.appfood_by_tinnguyen2421.BottomNavigation.DeliveryBottomNavigation;
import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.ReusableCodeForAll;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class SendOTP extends AppCompatActivity {

    String verificationId;
    FirebaseAuth FAuth;
    Button verify;
    TextView txt;
    String phonenumber;
    DatabaseReference mDatabase;
    Button Resend;
    EditText entercode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendotp);

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
                    entercode.setError("Vui lòng nhập đúng code");
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
        FAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = FAuth.getCurrentUser();
                        if (currentUser != null) {
                            mDatabase = FirebaseDatabase.getInstance().getReference("User").child(currentUser.getUid() + "/Role");
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String role = dataSnapshot.getValue(String.class);
                                    switch (role) {
                                        case "Chef":
                                            startActivity(new Intent(SendOTP.this, ChefBottomNavigation.class));
                                            finish();
                                            break;
                                        case "UserModel":
                                            startActivity(new Intent(SendOTP.this, CustomerBottomNavigation.class));
                                            finish();
                                            break;
                                        case "DeliveryPerson":
                                            startActivity(new Intent(SendOTP.this, DeliveryBottomNavigation.class));
                                            finish();
                                            break;
                                        default:
                                            Toast.makeText(SendOTP.this, "Số điện thoại này chưa được đăng kí", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Xử lý khi có lỗi xảy ra
                                }
                            });
                        } else {
                            ReusableCodeForAll.ShowAlert(SendOTP.this,"Lỗi đăng nhập","Số điện thoại này chưa được đăng ki");
                        }
                    } else {
                        ReusableCodeForAll.ShowAlert(SendOTP.this,"Lỗi đăng nhập","Mã xác thực không chính xác");
                    }
                });
    }


// het
    private void sendverificationcode(String number) {

        //PhoneAuthProvider.getInstance().verifyPhoneNumber(
                //number,
                //60L,
              //  TimeUnit.SECONDS,
            //    (Activity) TaskExecutors.MAIN_THREAD,
          //      mCallBack
        //);
        //het
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

            Toast.makeText(SendOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}

