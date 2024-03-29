package com.example.appfood_by_tinnguyen2421.ChefAccount;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.Account.VerifyPhone;
import com.example.appfood_by_tinnguyen2421.Account.LoginEmail;
import com.example.appfood_by_tinnguyen2421.Account.LoginPhone;
import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefRegisteration extends AppCompatActivity {

    String[] TP_HCM = {"Q1", "Q2", "Q3","Q4","Q5","Q6","Q7","Q8","Q9","Q10","Q11","Q12","Quận Tân Bình","Quận Bình Tân","Quận Phú Nhuận","Quận Bình Thạnh"};
    String[] TP_HàNội = {"Quận Hoàn Kiếm", "Quận Ba Đình", "Quận Đống Đa"};
    String[] TiềnGiang = {"Huyện Chợ Gạo", "Huyện Tân Phú Đông", "Huyện Gò Công"};


    String[] Q1 = {"Phường Bến Nghé", "Phường Bến Thành", "Phường Cô Giang", "Phường Cầu Kho", "Phường Cầu Ông Lãnh", "Phường Nguyễn Cư Trinh", "Phường Nguyễn Thái Bình", "Phường Phạm Ngũ Lão",
            "Phường Tân Định", "Phường ĐaKao"};


    String[] Q2 = {"Phường An Khánh", "Phường An Lợi Đông", " Phường An Phú", "PBình An", "Phường Bình Khánh","Phường Bình Trưng Đông","Phường Bình Trưng Tây","Phường Cát Lái","Phường Thạnh Mỹ Lợi","Phường Thảo Điền","Phường Thủ Thiêm"};
    String[] Q3 = {"P1", "P2", "P3", "P4", "P5", "P9", "P10", "P11", "P12", "P13", "P14"};

    TextInputLayout Fname, Lname, Email, Pass, cfpass, mobileno, houseno;
    Spinner statespin, Cityspin, Suburban;
    Button signup, emailLogin, phoneLogin;
    CountryCodePicker Cpp;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String fname;
    String lname;
    String emailid;
    String password;
    String confirmpassword;
    String mobile;
    String address;
    String role = "Chef";
    String city;
    String district;
    String ward;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_registeration);
        Fname = (TextInputLayout) findViewById(R.id.Firstname);
        Lname = (TextInputLayout) findViewById(R.id.Lastname);
        Email = (TextInputLayout) findViewById(R.id.Email);
        Pass = (TextInputLayout) findViewById(R.id.Pwd);
        cfpass = (TextInputLayout) findViewById(R.id.Cpass);
        mobileno = (TextInputLayout) findViewById(R.id.Mobileno);
        houseno = (TextInputLayout) findViewById(R.id.houseNo);
        statespin = (Spinner) findViewById(R.id.Statee);
        Cityspin = (Spinner) findViewById(R.id.Citys);
        Suburban = (Spinner) findViewById(R.id.Suburban);
        signup = (Button) findViewById(R.id.Signup);
        emailLogin = (Button) findViewById(R.id.emaill);
        phoneLogin = (Button) findViewById(R.id.phone);
        Cpp = (CountryCodePicker) findViewById(R.id.CountryCode);
        Cpp.setDefaultCountryUsingNameCode("VN");
        Cpp.resetToDefaultCountry();

        statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                city = value.toString().trim();
                if (city.equals("Thành Phố Hồ Chí Minh")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : TP_HCM) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefRegisteration.this, android.R.layout.simple_spinner_item, list);

                    Cityspin.setAdapter(arrayAdapter);
                }
                if (city.equals("Thành Phố Hà Nội")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : TP_HàNội) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefRegisteration.this, android.R.layout.simple_spinner_item, list);

                    Cityspin.setAdapter(arrayAdapter);
                }
                if (city.equals("Tỉnh Tiền Giang")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : TiềnGiang) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefRegisteration.this, android.R.layout.simple_spinner_item, list);

                    Cityspin.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Cityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                district = value.toString().trim();
                if (district.equals("Q1")) {
                    ArrayList<String> listt = new ArrayList<>();
                    for (String text : Q1) {
                        listt.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefRegisteration.this, android.R.layout.simple_spinner_item, listt);
                    Suburban.setAdapter(arrayAdapter);
                }

                if (district.equals("Q2")) {
                    ArrayList<String> listt = new ArrayList<>();
                    for (String text : Q2) {
                        listt.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefRegisteration.this, android.R.layout.simple_spinner_item, listt);
                    Suburban.setAdapter(arrayAdapter);
                }

                if (district.equals("Q3")) {
                    ArrayList<String> listt = new ArrayList<>();
                    for (String text : Q3) {
                        listt.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefRegisteration.this, android.R.layout.simple_spinner_item, listt);
                    Suburban.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Suburban.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                ward = value.toString().trim();
                if (ward.equals("Phường Bến Nghé")) {
                    ArrayList<String> listt = new ArrayList<>();
                    for (String text : Q1) {
                        listt.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefRegisteration.this, android.R.layout.simple_spinner_item, listt);
                    Suburban.setAdapter(arrayAdapter);
                }

                if (Suburban.equals("Phường An Khánh")) {
                    ArrayList<String> listt = new ArrayList<>();
                    for (String text : Q2) {
                        listt.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefRegisteration.this, android.R.layout.simple_spinner_item, listt);
                    Suburban.setAdapter(arrayAdapter);
                }

                if (Suburban.equals("P1")) {
                    ArrayList<String> listt = new ArrayList<>();
                    for (String text : Q3) {
                        listt.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefRegisteration.this, android.R.layout.simple_spinner_item, listt);
                    Suburban.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        databaseReference = firebaseDatabase.getInstance().getReference("Chef");
        FAuth = FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fname = Fname.getEditText().getText().toString().trim();
                lname = Lname.getEditText().getText().toString().trim();
                emailid = Email.getEditText().getText().toString().trim();
                mobile = mobileno.getEditText().getText().toString().trim();
                password = Pass.getEditText().getText().toString().trim();
                confirmpassword = cfpass.getEditText().getText().toString().trim();
                address = houseno.getEditText().getText().toString().trim();



                if (isValid()) {

                    final ProgressDialog mDialog = new ProgressDialog(ChefRegisteration.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Đang đăng kí, vui lòng đợi !");
                    mDialog.show();
                    FAuth.createUserWithEmailAndPassword(emailid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                    String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);
                                final HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("Role", role);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        HashMap<String, String> hashMappp = new HashMap<>();
                                        hashMappp.put("Address", address);
                                        hashMappp.put("District", district);
                                        hashMappp.put("ConfirmPassword", confirmpassword);
                                        hashMappp.put("EmailID", emailid);
                                        hashMappp.put("FirstName", fname);
                                        hashMappp.put("LastName", lname);
                                        hashMappp.put("PhoneNumber", mobile);
                                        hashMappp.put("Password", password);
                                        hashMappp.put("City", city);
                                        hashMappp.put("Ward", ward);
                                        firebaseDatabase.getInstance().getReference("Chef")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(hashMappp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mDialog.dismiss();
                                                        FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChefRegisteration.this);
                                                                    builder.setMessage("Đăng kí thành công, hãy xác nhận email của bạn");
                                                                    builder.setCancelable(false);
                                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            dialog.dismiss();
                                                                            String phonenumber = Cpp.getSelectedCountryCodeWithPlus() + mobile;
                                                                            Intent b = new Intent(ChefRegisteration.this, VerifyPhone.class);
                                                                            b.putExtra("phonenumber", phonenumber);
                                                                            startActivity(b);
                                                                            finish();

                                                                        }
                                                                    });
                                                                    AlertDialog alert = builder.create();
                                                                    alert.show();

                                                                } else {
                                                                    mDialog.dismiss();
                                                                    ReusableCodeForAll.ShowAlert(ChefRegisteration.this, "Lỗi", task.getException().getMessage());

                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                });


                            } else {
                                mDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(ChefRegisteration.this, "Lỗi", "Email này đã được đăng kí sử dụng ");
                            }

                        }
                    });

                }

            }

        });

        emailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ChefRegisteration.this, LoginEmail.class);
                startActivity(i);
                finish();
            }
        });

        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(ChefRegisteration.this, LoginPhone.class);
                startActivity(e);
                finish();
            }
        });


    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid() {
        Email.setErrorEnabled(false);
        Email.setError("");
        Fname.setErrorEnabled(false);
        Fname.setError("");
        Lname.setErrorEnabled(false);
        Lname.setError("");
        Pass.setErrorEnabled(false);
        Pass.setError("");
        mobileno.setErrorEnabled(false);
        mobileno.setError("");
        cfpass.setErrorEnabled(false);
        cfpass.setError("");
        houseno.setErrorEnabled(false);
        houseno.setError("");

        boolean isValidname = false, isValidemail = false, isvalidpassword = false, isvalidconfirmpassword = false, isvalid = false, isvalidmobileno = false, isvalidlname = false, isvalidhousestreetno = false;
        if (TextUtils.isEmpty(fname)) {
            Fname.setErrorEnabled(true);
            Fname.setError("Họ và tên lót của bạn không được để trống");
        } else {
            if (fname.length() > 30) {
                Fname.setErrorEnabled(true);
                Fname.setError("Họ và tên lót không vượt quá 30 ký tự");
            } else if (fname.length() < 3){
                Fname.setErrorEnabled(true);
                Fname.setError("Họ và tên lót phải lớn hơn 3 ký tự");
            }
            else {
                isValidname = true;
            }
        }
        if (TextUtils.isEmpty(lname)) {
            Lname.setErrorEnabled(true);
            Lname.setError("Tên của bạn không được để trống");
        } else {
            if (lname.length() > 30) {
                Lname.setErrorEnabled(true);
                Lname.setError("Tên không vượt quá 30 ký tự");

            }
            else {
                isvalidlname = true;
            }
        }
        if (TextUtils.isEmpty(emailid)) {
            Email.setErrorEnabled(true);
            Email.setError("Email không được để trống");
        } else {
            if (emailid.matches(emailpattern)) {
                isValidemail = true;
            } else {
                Email.setErrorEnabled(true);
                Email.setError("Hãy nhập địa chỉ chính xác !!!");
            }

        }
        if (TextUtils.isEmpty(password)) {
            Pass.setErrorEnabled(true);
            Pass.setError("Mật khẩu không được để trống ");
        } else {
            if (password.length() < 6) {
                Pass.setErrorEnabled(true);
                Pass.setError("Mật khẩu quá yếu ! Hãy nhặp mật khẩu lớn hơn 6 ký tự");
            } else if (password.length() > 14) {
                Pass.setErrorEnabled(true);
                Pass.setError("Mật khẩu không được dài hơn 14 ký tự");
                
            } else {
                isvalidpassword = true;
            }
        }
        if (TextUtils.isEmpty(confirmpassword)) {
            cfpass.setErrorEnabled(true);
            cfpass.setError("Xác nhận mật khẩu không được để trống");
        } else {
            if (!password.equals(confirmpassword)) {
                cfpass.setErrorEnabled(true);
                cfpass.setError("Mật khẩu không khớp");
            } else {
                isvalidconfirmpassword = true;
            }
        }
        if (TextUtils.isEmpty(mobile)) {
            mobileno.setErrorEnabled(true);
            mobileno.setError("Số điện thoại không được để trống");
        } else {
            if (mobile.length() < 10) {
                mobileno.setErrorEnabled(true);
                mobileno.setError("Số điện thoại không tồn tại");
            } else if (mobile.length() > 11) {
                mobileno.setErrorEnabled(true);
                mobileno.setError("Số điện thoại không tồn tại");
            }
            else {isvalidmobileno = true;}
            }
        //
        if (TextUtils.isEmpty(address)) {
            houseno.setErrorEnabled(true);
            houseno.setError("Địa chỉ không được để trống");
        } else {
            if (address.length() < 5) {
                houseno.setErrorEnabled(true);
                houseno.setError("Địa chỉ không được ít hơn 5 kí tự");
            } else if (address.length() > 30) {
                houseno.setErrorEnabled(true);
                houseno.setError("Địa chỉ không được quá 30 kí tự");
            }
            else {isvalidhousestreetno = true;}
        }


        isvalid = (isValidname && isvalidlname && isValidemail && isvalidconfirmpassword && isvalidpassword && isvalidmobileno  && isvalidhousestreetno) ? true : false;
        return isvalid;
    }


}



