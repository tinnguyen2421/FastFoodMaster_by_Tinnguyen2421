package com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.R;
import com.hbb20.CountryCodePicker;

public class CustomerPhonenumber extends AppCompatActivity {

    EditText edtPhoneNumber;
    CountryCodePicker cpp;
    Button btnSendOTP;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_phonenumber);
        initializeViews();
        setUpListeners();
    }

    private void setUpListeners() {
        btnSendOTP.setOnClickListener(v -> sendOtp());
    }

    private void sendOtp() {
        phoneNumber = edtPhoneNumber.getText().toString().trim();
        String phonenumber= cpp.getSelectedCountryCodeWithPlus() + phoneNumber;
        Intent intent=new Intent(CustomerPhonenumber.this, CustomerPhoneSendOTP.class);
        intent.putExtra("phonenumber",phonenumber);
        startActivity(intent);
        finish();
    }

    private void initializeViews() {
        edtPhoneNumber =(EditText)findViewById(R.id.phonenumber);
        cpp=(CountryCodePicker)findViewById(R.id.Countrycode);
        cpp.setDefaultCountryUsingNameCode("VN");
        cpp.resetToDefaultCountry();
        btnSendOTP =(Button)findViewById(R.id.sendotp);
    }
}
