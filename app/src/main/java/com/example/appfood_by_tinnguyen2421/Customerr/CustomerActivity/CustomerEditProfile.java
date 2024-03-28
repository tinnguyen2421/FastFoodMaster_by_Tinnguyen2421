package com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.Account.ChangePassword;
import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.DesignPattern.Command.ChangePasswordCommand;
import com.example.appfood_by_tinnguyen2421.DesignPattern.Command.ChangePhoneNumberCommand;
import com.example.appfood_by_tinnguyen2421.DesignPattern.Command.Command;
import com.example.appfood_by_tinnguyen2421.DesignPattern.Command.UpdateInformationCommand;
import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.ReusableCodeForAll;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerEditProfile extends AppCompatActivity {

    String[] TP_HCM = {"Q1", "Q2", "Q3","Q4","Q5","Q6","Q7","Q8","Q9","Q10","Q11","Q12","Quận Tân Bình","Quận Bình Tân","Quận Phú Nhuận","Quận Bình Thạnh"};
    String[] TP_HàNội = {"Quận Hoàn Kiếm", "Quận Ba Đình", "Quận Đống Đa"};
    String[] TiềnGiang = {"Huyện Chợ Gạo", "Huyện Tân Phú Đông", "Huyện Gò Công"};


    String[] Q1 = {"Phường Bến Nghé", "Phường Bến Thành", "Phường Cô Giang", "Phường Cầu Kho", "Phường Cầu Ông Lãnh", "Phường Nguyễn Cư Trinh", "Phường Nguyễn Thái Bình", "Phường Phạm Ngũ Lão",
            "Phường Tân Định", "Phường ĐaKao"};


    String[] Q2 = {"Phường An Khánh", "Phường An Lợi Đông", " Phường An Phú", "PBình An", "Phường Bình Khánh","Phường Bình Trưng Đông","Phường Bình Trưng Tây","Phường Cát Lái","Phường Thạnh Mỹ Lợi","Phường Thảo Điền","Phường Thủ Thiêm"};
    String[] Q3 = {"P1", "P2", "P3", "P4", "P5", "P9", "P10", "P11", "P12", "P13", "P14"};

    EditText firstname, lastname, address;
    Spinner State, City, Suburban;
    TextView mobileno, Email;
    Button Update;
    LinearLayout password;
    DatabaseReference databaseReference, data;
    FirebaseDatabase firebaseDatabase;
    String city, district, ward, email, passwordd, confirmpass;
    private Command updateInformationCommand;
    private Command changePasswordCommand;
    private Command changePhoneNumberCommand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_edit_profile);
        initializeViews();
        loadInformation();
        setUpCommands();
        setupListeners();

    }
    private void executeCommand(Command command) {
        command.execute();
    }
    private void setupListeners() {
        Update.setOnClickListener(v -> executeCommand(updateInformationCommand));

        password.setOnClickListener(v -> executeCommand(changePasswordCommand));

        mobileno.setOnClickListener(v -> executeCommand(changePhoneNumberCommand));
    }
    private void setUpCommands() {
        updateInformationCommand = new UpdateInformationCommand(this);
        changePasswordCommand = new ChangePasswordCommand(this);
        changePhoneNumberCommand = new ChangePhoneNumberCommand(this);
    }
    private void loadInformation() {
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final UserModel userModel = dataSnapshot.getValue(UserModel.class);
                firstname.setText(userModel.getFirstName());
                lastname.setText(userModel.getLastName());
                address.setText(userModel.getAddress());
                mobileno.setText(userModel.getPhoneNumber());
                Email.setText(userModel.getEmailID());
                State.setSelection(getIndexByString(State, userModel.getCity()));
                State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object value = parent.getItemAtPosition(position);
                        city = value.toString().trim();
                        if (city.equals("Thành Phố Hồ Chí Minh")) {
                            ArrayList<String> list = new ArrayList<>();
                            for (String text : TP_HCM) {
                                list.add(text);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CustomerEditProfile.this, android.R.layout.simple_spinner_item, list);
                            City.setAdapter(arrayAdapter);
                        }
                        if (city.equals("Thành Phố Hà Nội")) {
                            ArrayList<String> list = new ArrayList<>();
                            for (String text : TP_HàNội) {
                                list.add(text);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CustomerEditProfile.this, android.R.layout.simple_spinner_item, list);

                            City.setAdapter(arrayAdapter);
                        }
                        City.setSelection(getIndexByString(City, userModel.getCity()));
                        if (city.equals("Tỉnh Tiền Giang")) {
                            ArrayList<String> list = new ArrayList<>();
                            for (String text : TiềnGiang) {
                                list.add(text);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CustomerEditProfile.this, android.R.layout.simple_spinner_item, list);

                            City.setAdapter(arrayAdapter);
                        }
                        City.setSelection(getIndexByString(City, userModel.getCity()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object value = parent.getItemAtPosition(position);
                        district = value.toString().trim();
                        if (district.equals("Q1")) {
                            ArrayList<String> listt = new ArrayList<>();
                            for (String text : Q1) {
                                listt.add(text);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CustomerEditProfile.this, android.R.layout.simple_spinner_item, listt);
                            Suburban.setAdapter(arrayAdapter);
                        }

                        if (district.equals("Q2")) {
                            ArrayList<String> listt = new ArrayList<>();
                            for (String text : Q2) {
                                listt.add(text);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CustomerEditProfile.this, android.R.layout.simple_spinner_item, listt);
                            Suburban.setAdapter(arrayAdapter);
                        }

                        if (district.equals("Q3")) {
                            ArrayList<String> listt = new ArrayList<>();
                            for (String text : Q3) {
                                listt.add(text);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CustomerEditProfile.this, android.R.layout.simple_spinner_item, listt);
                            Suburban.setAdapter(arrayAdapter);
                        }
                        Suburban.setSelection(getIndexByString(Suburban, userModel.getWard()));
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
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeViews() {
        firstname = findViewById(R.id.fnamee);
        lastname = findViewById(R.id.lnamee);
        address = findViewById(R.id.address);
        Email = findViewById(R.id.emailID);
        State = findViewById(R.id.statee);
        City = findViewById(R.id.cityy);
        Suburban = findViewById(R.id.sub);
        mobileno = findViewById(R.id.mobilenumber);
        Update = findViewById(R.id.update);
        password = findViewById(R.id.passwordlayout);
    }


    public void changePhoneNumber()
    {
        Intent intent = new Intent(CustomerEditProfile.this, CustomerPhonenumber.class);
        startActivity(intent);
    }
    public void changePassword()
    {
        Intent intent = new Intent(CustomerEditProfile.this, ChangePassword.class);
        startActivity(intent);
    }
    private int getIndexByString(Spinner st, String spist) {
        int index = 0;
        for (int i = 0; i < st.getCount(); i++) {
            if (st.getItemAtPosition(i).toString().equalsIgnoreCase(spist)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void updateInformation() {
        String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
        data = FirebaseDatabase.getInstance().getReference("Customer").child(useridd);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                confirmpass = userModel.getConfirmPassword();
                email = userModel.getEmailID();
                passwordd = userModel.getPassword();
                long mobilenoo = Long.parseLong(userModel.getPhoneNumber());
                String Fname = firstname.getText().toString().trim();
                String Lname = lastname.getText().toString().trim();
                String Address = address.getText().toString().trim();
                HashMap<String, String> hashMappp = new HashMap<>();
                hashMappp.put("District", district);
                hashMappp.put("ConfirmPassword", confirmpass);
                hashMappp.put("EmailID", email);
                hashMappp.put("FirstName", Fname);
                hashMappp.put("LastName",Lname);
                hashMappp.put("PhoneNumber", String.valueOf(mobilenoo));
                hashMappp.put("Password", passwordd);
                hashMappp.put("Address", Address);
                hashMappp.put("City", city);
                hashMappp.put("Ward", ward);
                firebaseDatabase.getInstance().getReference("Customer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMappp);
                ReusableCodeForAll.ShowAlert(CustomerEditProfile.this,"Thành Công","Cập nhật thành công");


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}