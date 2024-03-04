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

public class SendOTP extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private EditText enterCodeEditText;
    private TextView resendTextView;
    private Button verifyButton;
    private Button resendButton;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendotp);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        phoneNumber = getIntent().getStringExtra("phonenumber").trim();
        sendVerificationCode(phoneNumber);

        enterCodeEditText = findViewById(R.id.phoneno);
        resendTextView = findViewById(R.id.text);
        resendButton = findViewById(R.id.Resendotp);
        resendButton.setVisibility(View.INVISIBLE);

        verifyButton = findViewById(R.id.Verify);
        verifyButton.setOnClickListener(v -> verifyCode());

        setupResendButton();
    }

    private void setupResendButton() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendTextView.setVisibility(View.VISIBLE);
                resendTextView.setText("Gửi lại code sau " + millisUntilFinished / 1000 + " giây");
            }

            @Override
            public void onFinish() {
                resendButton.setVisibility(View.VISIBLE);
                resendTextView.setVisibility(View.INVISIBLE);
            }
        }.start();

        resendButton.setOnClickListener(v -> {
            resendButton.setVisibility(View.INVISIBLE);
            sendVerificationCode(phoneNumber);
            startResendCountdown();
        });
    }

    private void startResendCountdown() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendTextView.setVisibility(View.VISIBLE);
                resendTextView.setText("Gửi lại code sau " + millisUntilFinished / 1000 + " giây");
            }

            @Override
            public void onFinish() {
                resendButton.setVisibility(View.VISIBLE);
                resendTextView.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    private void verifyCode() {
        String code = enterCodeEditText.getText().toString().trim();
        if (code.isEmpty() || code.length() < 6) {
            enterCodeEditText.setError("Vui lòng nhập đúng code");
            enterCodeEditText.requestFocus();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        if (currentUser != null) {
                            checkUserRole(currentUser.getUid());
                        } else {
                            ReusableCodeForAll.ShowAlert(SendOTP.this, "Lỗi đăng nhập", "Số điện thoại này chưa được đăng kí");
                        }
                    } else {
                        ReusableCodeForAll.ShowAlert(SendOTP.this, "Lỗi đăng nhập", "Mã xác thực không chính xác");
                    }
                });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(phoneAuthCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks phoneAuthCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        enterCodeEditText.setText(code);
                        verifyCode();
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(SendOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            };

    private void checkUserRole(String userId) {
        databaseReference.child(userId + "/Role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String role = dataSnapshot.getValue(String.class);
                switch (role) {
                    case "Chef":
                        startActivity(new Intent(SendOTP.this, ChefBottomNavigation.class));
                        finish();
                        break;
                    case "Customer":
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
    }
}
