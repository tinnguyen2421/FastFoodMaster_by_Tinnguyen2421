package com.example.appfood_by_tinnguyen2421.Account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {

    private TextInputLayout currentPwdInputLayout, newPwdInputLayout, confirmPwdInputLayout;
    private Button changePwdButton;
    private TextView forgotPwdTextView;
    private String email;

    private DatabaseReference mDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_password);

        currentPwdInputLayout = findViewById(R.id.current_pwd);
        newPwdInputLayout = findViewById(R.id.new_pwd);
        confirmPwdInputLayout = findViewById(R.id.confirm_pwd);
        changePwdButton = findViewById(R.id.change);
        forgotPwdTextView = findViewById(R.id.forgot_pwd);

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
                .child(FirebaseAuth.getInstance().getUid() + "/Role");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String role = dataSnapshot.getValue(String.class);
                changePassword(role);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void changePassword(String role) {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference(role).child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                email = userModel.getEmailID();

                changePwdButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String curPwd = currentPwdInputLayout.getEditText().getText().toString().trim();
                        String newPwd = newPwdInputLayout.getEditText().getText().toString().trim();
                        String confirmPwd = confirmPwdInputLayout.getEditText().getText().toString().trim();

                        if (isValid(curPwd, newPwd, confirmPwd)) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            AuthCredential credential = EmailAuthProvider.getCredential(email, curPwd);

                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(newPwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseDatabase.getInstance().getReference(role).child(userId)
                                                            .child("Password").setValue(newPwd);
                                                    FirebaseDatabase.getInstance().getReference(role).child(userId)
                                                            .child("ConfirmPassword").setValue(confirmPwd);
                                                    ReusableCodeForAll.ShowAlert(ChangePassword.this,"Thành Công","Đổi mật khẩu thành công !");
                                                } else {
                                                    Toast.makeText(ChangePassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(ChangePassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });

                forgotPwdTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChangePassword.this, ForgotPassword.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean isValid(String curPwd, String newPwd, String confirmPwd) {
        newPwdInputLayout.setErrorEnabled(false);
        newPwdInputLayout.setError("");
        confirmPwdInputLayout.setErrorEnabled(false);
        confirmPwdInputLayout.setError("");

        boolean isValidNewPassword = false, isValidConfirmPassword = false, isValid = false;

        if (TextUtils.isEmpty(newPwd)) {
            newPwdInputLayout.setErrorEnabled(true);
            newPwdInputLayout.setError("Mật khẩu mới không được bỏ trống");
        } else {
            if (newPwd.length() < 6) {
                newPwdInputLayout.setErrorEnabled(true);
                newPwdInputLayout.setError("Mật khẩu quá yếu");
                confirmPwdInputLayout.setError("Mật khẩu quá yếu");
            } else if (newPwd.length() > 30) {
                newPwdInputLayout.setErrorEnabled(true);
                newPwdInputLayout.setError("Mật khẩu không vượt quá 30 ký tự");
                confirmPwdInputLayout.setError("Mật khẩu không vượt quá 30 ký tự");
            } else {
                isValidNewPassword = true;
            }
        }

        if (TextUtils.isEmpty(confirmPwd)) {
            confirmPwdInputLayout.setErrorEnabled(true);
            confirmPwdInputLayout.setError("Xác nhận mật khẩu không được để trống");
        } else {
            if (!newPwd.equals(confirmPwd)) {
                newPwdInputLayout.setErrorEnabled(true);
                newPwdInputLayout.setError("Mật khẩu không khớp");
                confirmPwdInputLayout.setError("Mật khẩu không khớp");
            } else {
                isValidConfirmPassword = true;
            }
        }

        isValid = isValidNewPassword && isValidConfirmPassword;
        return isValid;
    }
}
