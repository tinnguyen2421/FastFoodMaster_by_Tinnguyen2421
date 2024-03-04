package com.example.appfood_by_tinnguyen2421.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.BottomNavigation.ChefBottomNavigation;
import com.example.appfood_by_tinnguyen2421.BottomNavigation.CustomerBottomNavigation;
import com.example.appfood_by_tinnguyen2421.BottomNavigation.DeliveryBottomNavigation;
import com.example.appfood_by_tinnguyen2421.CustomerAccount.CustomerRegisteration;
import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.ReusableCodeForAll;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginEmail extends AppCompatActivity {

    TextInputLayout emailInput, passInput;
    Button signInButton, signInPhoneButton;
    TextView forgotPasswordText, registerText;
    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        initializeViews();
        setupButtonClickListeners();
    }

    private void initializeViews() {
        emailInput = findViewById(R.id.Lemail);
        passInput = findViewById(R.id.Lpassword);
        signInButton = findViewById(R.id.button4);
        registerText = findViewById(R.id.textView3);
        forgotPasswordText = findViewById(R.id.forgotpass);
        signInPhoneButton = findViewById(R.id.btnphone);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupButtonClickListeners() {
        signInButton.setOnClickListener(v -> signIn());
        registerText.setOnClickListener(v -> startActivity(new Intent(LoginEmail.this, CustomerRegisteration.class)));
        forgotPasswordText.setOnClickListener(v -> startActivity(new Intent(LoginEmail.this, ForgotPassword.class)));
        signInPhoneButton.setOnClickListener(v -> startActivity(new Intent(LoginEmail.this, LoginPhone.class)));
    }

    private void signIn() {
        email = emailInput.getEditText().getText().toString().trim();
        password = passInput.getEditText().getText().toString().trim();
        if (isValid()) {
            ProgressDialog progressDialog = createProgressDialog();
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            handleSignInSuccess(progressDialog);
                        } else {
                            handleSignInFailure(progressDialog);
                        }
                    });
        }
    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(LoginEmail.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang đăng nhập...");
        return progressDialog;
    }

    private void handleSignInSuccess(ProgressDialog progressDialog) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            checkUserRole(progressDialog);
        } else {
            progressDialog.dismiss();
            ReusableCodeForAll.ShowAlert(LoginEmail.this, "Lỗi đăng nhập", "Thông tin tài khoản hoặc mật khẩu không chính xác");
        }
    }

    private void handleSignInFailure(ProgressDialog progressDialog) {
        progressDialog.dismiss();
        ReusableCodeForAll.ShowAlert(LoginEmail.this, "Lỗi đăng nhập", "Thông tin tài khoản hoặc mật khẩu không chính xác");
    }

    private void checkUserRole(ProgressDialog progressDialog) {
        mDatabase = FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getUid() + "/Role");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String role = dataSnapshot.getValue(String.class);
                switch (role) {
                    case "Chef":
                        startActivity(new Intent(LoginEmail.this, ChefBottomNavigation.class));
                        finish();
                        break;
                    case "Customer":
                        startActivity(new Intent(LoginEmail.this, CustomerBottomNavigation.class));
                        finish();
                        break;
                    case "DeliveryPerson":
                        startActivity(new Intent(LoginEmail.this, DeliveryBottomNavigation.class));
                        finish();
                        break;
                    default:
                        progressDialog.dismiss();
                        Toast.makeText(LoginEmail.this, "Email chưa được đăng kí", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }

    private boolean isValid() {
        emailInput.setErrorEnabled(false);
        emailInput.setError("");
        passInput.setErrorEnabled(false);
        passInput.setError("");

        boolean isValidEmail = false, isValidPassword = false;
        if (TextUtils.isEmpty(email)) {
            emailInput.setErrorEnabled(true);
            emailInput.setError("Email không được để trống");
        } else {
            if (email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                isValidEmail = true;
            } else {
                emailInput.setErrorEnabled(true);
                emailInput.setError("Email không hợp lệ");
            }
        }
        if (TextUtils.isEmpty(password)) {
            passInput.setErrorEnabled(true);
            passInput.setError("Mật khẩu không được để trống");
        } else {
            isValidPassword = true;
        }
        return isValidEmail && isValidPassword;
    }
}
