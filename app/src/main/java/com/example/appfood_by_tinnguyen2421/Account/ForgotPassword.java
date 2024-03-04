package com.example.appfood_by_tinnguyen2421.Account;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private TextInputLayout forgetPasswordLayout;
    private Button resetPassWordButton;
    private FirebaseAuth firebaseAuth;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgetPasswordLayout = findViewById(R.id.Emailid);
        resetPassWordButton = findViewById(R.id.button2);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPassWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = forgetPasswordLayout.getEditText().getText().toString().trim();
                if (isValid()) {
                    final ProgressDialog progressDialog = new ProgressDialog(ForgotPassword.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Đang thực hiện...");
                    progressDialog.show();

                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                ReusableCodeForAll.ShowAlert(ForgotPassword.this, "", "Mật khẩu đã được gửi đến Email của bạn");
                            } else {
                                ReusableCodeForAll.ShowAlert(ForgotPassword.this, "Lỗi", task.getException().getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean isValid() {
        forgetPasswordLayout.setErrorEnabled(false);
        forgetPasswordLayout.setError("");
        boolean isValidEmail = false, isValid = false;

        if (TextUtils.isEmpty(email)) {
            forgetPasswordLayout.setErrorEnabled(true);
            forgetPasswordLayout.setError("Email không được để trống");
        } else {
            if (email.matches(emailPattern)) {
                isValidEmail = true;
            } else {
                forgetPasswordLayout.setErrorEnabled(true);
                forgetPasswordLayout.setError("Email không hợp lệ");
            }
        }

        isValid = isValidEmail;
        return isValid;
    }

    private static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
}
