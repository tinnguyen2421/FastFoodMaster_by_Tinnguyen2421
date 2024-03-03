package com.example.appfood_by_tinnguyen2421.Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.ChefAccount.ChefRegisteration;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class LoginPhone extends AppCompatActivity {
    TextInputLayout phonenumb;
    EditText num;
    Button sendotp,signinemail;
    TextView txtsignup;
    CountryCodePicker cpp;
    FirebaseAuth FAuth;
    String numberr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginphone);

        num=(EditText)findViewById(R.id.number);
        sendotp=(Button)findViewById(R.id.otp);
        cpp=(CountryCodePicker)findViewById(R.id.CountryCode);
        cpp.setDefaultCountryUsingNameCode("VN");
        cpp.resetToDefaultCountry();
        signinemail=(Button)findViewById(R.id.btnEmail);
        txtsignup=(TextView)findViewById(R.id.acsignup);
        FAuth=FirebaseAuth.getInstance();


            sendotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        numberr = num.getText().toString().trim();
                        if(numberr.isEmpty()||numberr.length()>11||numberr.length()<10)
                        {
                            num.setError("Vui lòng không để trống và nhập đúng số điện thoại");
                        }
                        else {
                            String phonenumber = cpp.getSelectedCountryCodeWithPlus() + numberr;
                            Intent b = new Intent(LoginPhone.this, SendOTP.class);
                            b.putExtra("phonenumber", phonenumber);
                            startActivity(b);
                            finish();
                        }
                    }

            });

        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(LoginPhone.this, ChefRegisteration.class);
                startActivity(a);
                finish();
            }
        });

        signinemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent em=new Intent(LoginPhone.this, LoginEmail.class);
                startActivity(em);
                finish();
            }
        });

    }

}
