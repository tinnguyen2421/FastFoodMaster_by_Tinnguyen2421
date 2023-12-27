package com.example.appfood_by_tinnguyen2421;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefChangePassword;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefPhonenumberAuth;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.Chef;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.CustomerPassword;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.CustomerPhonenumber;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.Customer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChefEditProfile extends AppCompatActivity {

    String[] TP_HCM = {"Q1", "Q2", "Q3", "Q4", "Q5", "Q6", "Q7", "Q8", "Q9", "Q10", "Q11", "Q12", "Quận Tân Bình", "Quận Bình Tân", "Quận Phú Nhuận", "Quận Bình Thạnh"};
    String[] TP_HàNội = {"Quận Hoàn Kiếm", "Quận Ba Đình", "Quận Đống Đa"};
    String[] TiềnGiang = {"Huyện Chợ Gạo", "Huyện Tân Phú Đông", "Huyện Gò Công"};


    String[] Q1 = {"Phường Bến Nghé", "Phường Bến Thành", "Phường Cô Giang", "Phường Cầu Kho", "Phường Cầu Ông Lãnh", "Phường Nguyễn Cư Trinh", "Phường Nguyễn Thái Bình", "Phường Phạm Ngũ Lão",
            "Phường Tân Định", "Phường ĐaKao"};


    String[] Q2 = {"Phường An Khánh", "Phường An Lợi Đông", " Phường An Phú", "PBình An", "Phường Bình Khánh", "Phường Bình Trưng Đông", "Phường Bình Trưng Tây", "Phường Cát Lái", "Phường Thạnh Mỹ Lợi", "Phường Thảo Điền", "Phường Thủ Thiêm"};
    String[] Q3 = {"P1", "P2", "P3", "P4", "P5", "P9", "P10", "P11", "P12", "P13", "P14"};

    EditText firstname, lastname, address;
    Spinner State, City, Suburban;
    TextView mobileno, Email;
    Button Update;
    LinearLayout password;
    DatabaseReference databaseReference, data;
    FirebaseDatabase firebaseDatabase;
    String statee, cityy, suburban, email, passwordd, confirmpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_edit_profile);
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
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chef").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Chef chef = dataSnapshot.getValue(Chef.class);

                firstname.setText(chef.getFname());
                lastname.setText(chef.getLname());
                address.setText(chef.getArea());
                mobileno.setText(chef.getMobile());
                Email.setText(chef.getEmailID());
                State.setSelection(getIndexByString(State, chef.getState()));
                State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object value = parent.getItemAtPosition(position);
                        statee = value.toString().trim();
                        if (statee.equals("Thành Phố Hồ Chí Minh")) {
                            ArrayList<String> list = new ArrayList<>();
                            for (String text : TP_HCM) {
                                list.add(text);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefEditProfile.this, android.R.layout.simple_spinner_item, list);
                            City.setAdapter(arrayAdapter);
                        }
                        if (statee.equals("Thành Phố Hà Nội")) {
                            ArrayList<String> list = new ArrayList<>();
                            for (String text : TP_HàNội) {
                                list.add(text);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefEditProfile.this, android.R.layout.simple_spinner_item, list);

                            City.setAdapter(arrayAdapter);
                        }
                        City.setSelection(getIndexByString(City, chef.getCity()));
                        if (statee.equals("Tỉnh Tiền Giang")) {
                            ArrayList<String> list = new ArrayList<>();
                            for (String text : TiềnGiang) {
                                list.add(text);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefEditProfile.this, android.R.layout.simple_spinner_item, list);

                            City.setAdapter(arrayAdapter);
                        }
                        City.setSelection(getIndexByString(City, chef.getCity()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object value = parent.getItemAtPosition(position);
                        cityy = value.toString().trim();
                        if (cityy.equals("Q1")) {
                            ArrayList<String> listt = new ArrayList<>();
                            for (String text : Q1) {
                                listt.add(text);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefEditProfile.this, android.R.layout.simple_spinner_item, listt);
                            Suburban.setAdapter(arrayAdapter);
                        }

                        if (cityy.equals("Q2")) {
                            ArrayList<String> listt = new ArrayList<>();
                            for (String text : Q2) {
                                listt.add(text);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefEditProfile.this, android.R.layout.simple_spinner_item, listt);
                            Suburban.setAdapter(arrayAdapter);
                        }

                        if (cityy.equals("Q3")) {
                            ArrayList<String> listt = new ArrayList<>();
                            for (String text : Q3) {
                                listt.add(text);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChefEditProfile.this, android.R.layout.simple_spinner_item, listt);
                            Suburban.setAdapter(arrayAdapter);
                        }
                        Suburban.setSelection(getIndexByString(Suburban, chef.getSuburban()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Suburban.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object value = parent.getItemAtPosition(position);
                        suburban = value.toString().trim();
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
        updateinformation();

    }

    private void updateinformation() {


        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                data = FirebaseDatabase.getInstance().getReference("Chef").child(useridd);
                data.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Customer customer = dataSnapshot.getValue(Customer.class);


                        confirmpass = customer.getConfirmPassword();
                        email = customer.getEmailID();
                        passwordd = customer.getPassword();
                        long mobilenoo = Long.parseLong(customer.getMobileno());

                        String Fname = firstname.getText().toString().trim();
                        String Lname = lastname.getText().toString().trim();
                        String Address = address.getText().toString().trim();

                        HashMap<String, String> hashMappp = new HashMap<>();
                        hashMappp.put("Area", Address);
                        hashMappp.put("City", cityy);
                        hashMappp.put("ConfirmPassword", confirmpass);
                        hashMappp.put("FirstName", Fname);
                        hashMappp.put("EmailID", email);
                        hashMappp.put("LastName", Lname);
                        hashMappp.put("Mobileno", String.valueOf(mobilenoo));
                        hashMappp.put("Password", passwordd);
                        hashMappp.put("State", statee);
                        hashMappp.put("Suburban", suburban);
                        firebaseDatabase.getInstance().getReference("Chef").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMappp);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChefEditProfile.this);
                        Toast.makeText(ChefEditProfile.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });


            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChefEditProfile.this, ChefChangePassword.class);
                startActivity(intent);
            }
        });

        mobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ChefEditProfile.this, ChefPhonenumberAuth.class);
                startActivity(i);
            }
        });
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
}