package com.example.appfood_by_tinnguyen2421.Account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.ChefAccount.ChefRegisteration;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class LoginPhone extends AppCompatActivity {

    TextInputLayout phoneNumberLayout;
    EditText phoneNumberEditText;
    Button sendOTPButton, signInWithEmailButton;
    TextView signUpTextView;
    CountryCodePicker countryCodePicker;
    FirebaseAuth firebaseAuth;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginphone);

        initializeViews();
        setupOnClickListeners();
    }

    private void initializeViews() {

        phoneNumberEditText = findViewById(R.id.number);
        sendOTPButton = findViewById(R.id.otp);
        countryCodePicker = findViewById(R.id.CountryCode);
        countryCodePicker.setDefaultCountryUsingNameCode("VN");
        countryCodePicker.resetToDefaultCountry();
        signInWithEmailButton = findViewById(R.id.btnEmail);
        signUpTextView = findViewById(R.id.acsignup);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupOnClickListeners() {
        sendOTPButton.setOnClickListener(v -> sendOTP());
        signUpTextView.setOnClickListener(v -> chefRegister());
        signInWithEmailButton.setOnClickListener(v -> emailLogin());
    }

    private void sendOTP() {
        phoneNumber = phoneNumberEditText.getText().toString().trim();
        if (phoneNumber.isEmpty() || phoneNumber.length() > 11 || phoneNumber.length() < 10) {
            phoneNumberLayout.setError("Vui lòng không để trống và nhập đúng số điện thoại");
        } else {
            String completePhoneNumber = countryCodePicker.getSelectedCountryCodeWithPlus() + phoneNumber;
            Intent intent = new Intent(LoginPhone.this, SendOTP.class);
            intent.putExtra("phonenumber", completePhoneNumber);
            startActivity(intent);
            finish();
        }
    }

    private void chefRegister() {
        Intent intent = new Intent(LoginPhone.this, ChefRegisteration.class);
        startActivity(intent);
        finish();
    }

    private void emailLogin() {
        Intent intent = new Intent(LoginPhone.this, LoginEmail.class);
        startActivity(intent);
        finish();
    }
}
