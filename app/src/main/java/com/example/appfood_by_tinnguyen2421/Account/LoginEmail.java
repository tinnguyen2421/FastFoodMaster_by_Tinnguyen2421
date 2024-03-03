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

//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class LoginEmail extends AppCompatActivity {


    TextInputLayout email, pass;
    Button Signout,SignInphone;
    TextView btnFogotPassword;
    DatabaseReference mDatabase;
    TextView btnRegisteration;
    FirebaseAuth FAuth;
    String em;
    String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        try {
            email = (TextInputLayout) findViewById(R.id.Lemail);
            pass = (TextInputLayout) findViewById(R.id.Lpassword);
            Signout = (Button) findViewById(R.id.button4);
            btnRegisteration = (TextView) findViewById(R.id.textView3);
            btnFogotPassword =(TextView)findViewById(R.id.forgotpass);
            SignInphone=(Button)findViewById(R.id.btnphone);

            FAuth = FirebaseAuth.getInstance();

            Signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    em = email.getEditText().getText().toString().trim();
                    pwd = pass.getEditText().getText().toString().trim();
                    if (isValid()) {

                        final ProgressDialog mDialog = new ProgressDialog(LoginEmail.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("Đang đăng nhập...");
                        mDialog.show();
                        FAuth.signInWithEmailAndPassword(em, pwd)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        mDialog.dismiss();
                                        FirebaseUser currentUser = FAuth.getCurrentUser();
                                        if (currentUser != null && currentUser.isEmailVerified()) {
                                            mDatabase = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getUid() + "/Role");
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
                                                                mDialog.dismiss();
                                                                Toast.makeText(LoginEmail.this, "Email chưa được đăng kí", Toast.LENGTH_SHORT).show();                                                                break;
                                                        }

                                                }



                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    // Xử lý khi có lỗi xảy ra
                                                }
                                            });
                                        } else {
                                            mDialog.dismiss();
                                            ReusableCodeForAll.ShowAlert(LoginEmail.this,"Lỗi đăng nhập","Email này chưa được đăng ki");                                        }
                                    } else {
                                        mDialog.dismiss();
                                        ReusableCodeForAll.ShowAlert(LoginEmail.this,"Lỗi đăng nhập","Thông tin tài khoản hoặc mật khẩu không chính xác");
                                    }
                                });

                    }
                }
            });

            btnRegisteration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent Register = new Intent(LoginEmail.this, CustomerRegisteration.class);
                    startActivity(Register);

                }
            });

            btnFogotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a=new Intent(LoginEmail.this, ForgotPassword.class);
                    startActivity(a);

                }
            });
            SignInphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(LoginEmail.this, LoginPhone.class);
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public boolean isValid() {
        email.setErrorEnabled(false);
        email.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");

        boolean isvalidemail=false,isvalidpassword=false,isvalid=false;
        if (TextUtils.isEmpty(em))
        {
            email.setErrorEnabled(true);
            email.setError("Email không được để trống");
        }
        else {
            if (em.matches(emailpattern))
            {
                isvalidemail=true;
            }
            else {
                email.setErrorEnabled(true);
                email.setError("Email không hợp lệ");
            }

        }
        if (TextUtils.isEmpty(pwd))
        {
            pass.setErrorEnabled(true);
            pass.setError("Mật khẩu không được để trống");
        }
        else {
            isvalidpassword=true;
        }
        isvalid = (isvalidemail && isvalidpassword) ? true : false;
        return isvalid;
    }
}


