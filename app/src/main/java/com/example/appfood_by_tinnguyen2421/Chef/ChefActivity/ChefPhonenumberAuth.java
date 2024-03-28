package com.example.appfood_by_tinnguyen2421.Chef.ChefActivity;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.R;
import com.hbb20.CountryCodePicker;

public class ChefPhonenumberAuth extends AppCompatActivity {

    EditText num;
    CountryCodePicker cpp;
    Button SendOTP;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_phonenumber);
        initializeViews();
        setUpListeners();
    }

    private void setUpListeners() {
        SendOTP.setOnClickListener(v -> sendOtpProcess());
    }
    private void sendOtpProcess()
    {
        number=num.getText().toString().trim();
        String phonenumber= cpp.getSelectedCountryCodeWithPlus() + number;
        Intent intent=new Intent(ChefPhonenumberAuth.this, ChefPhoneSendOTP.class);
        intent.putExtra("phonenumber",phonenumber);
        startActivity(intent);
        finish();
    }
    private void initializeViews() {
        num=(EditText)findViewById(R.id.phonenumber);
        cpp=(CountryCodePicker)findViewById(R.id.Countrycode);
        cpp.setDefaultCountryUsingNameCode("VN");
        cpp.resetToDefaultCountry();
        SendOTP=(Button)findViewById(R.id.sendotp);
    }
}
