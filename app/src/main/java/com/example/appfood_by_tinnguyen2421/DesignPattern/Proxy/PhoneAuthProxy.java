package com.example.appfood_by_tinnguyen2421.DesignPattern.Proxy;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthProxy {
    private Activity activity;
    public PhoneAuthProxy(Activity activity) {
        this.activity = activity;
    }
    public interface VerificationSentCallback {
        void onCodeSent(String verificationId);

        void onFailure(String message);
    }

    public interface VerificationCallback {
        void onSuccess();

        void onFailure(String message);
    }

    public void sendVerificationCode(String phoneNumber, VerificationSentCallback callback) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        callback.onCodeSent(verificationId);
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }



                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        callback.onFailure(e.getMessage());
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void verifyCode(String verificationId, String code, VerificationCallback callback) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }
}
