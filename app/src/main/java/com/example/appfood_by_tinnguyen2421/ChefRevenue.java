package com.example.appfood_by_tinnguyen2421;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChefRevenue extends AppCompatActivity {
    private double total, parseDouble;
    private int month, year, count;
    TextView TimePickerr,Revenuee,TotalBillss;
    Button Refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_revenue);
        Refresh=findViewById(R.id.btnRefresh);
        TimePickerr=findViewById(R.id.timePicker);
        Revenuee=findViewById(R.id.Revenue);
        TotalBillss=findViewById(R.id.TotalBills);
        TimePickerr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               TimePicker();
            }
        });
        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerr.setText("");
                Revenuee.setText("");
                TotalBillss.setText("");
            }
        });
    }
    private void TimePicker() {
        count = 0;
        total = 0;
        parseDouble = 0;

        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ChefRevenue.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String thoiGian = (monthOfYear + 1) + "/" + year;
                    TimePickerr.setText(thoiGian);
                    QueryTime(thoiGian);
                },
                year, month, 1 // 1 là ngày mặc định khi hiển thị DatePickerDialog
        );

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }
    private void QueryTime(String thoiGian) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChefOrdersHistory");
        Query query = reference.orderByChild(FirebaseAuth.getInstance().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot chefSnapshot : dataSnapshot.getChildren()) {
                    // Duyệt qua các đơn hàng của đầu bếp có ChefId tương ứng
                    String formattedNumber = null;
                    for (DataSnapshot orderSnapshot : chefSnapshot.getChildren()) {
                        // Lấy thông tin từ OtherInformation
                        DataSnapshot otherInformationSnapshot = orderSnapshot.child("OtherInformation");
                        String aceptDate = otherInformationSnapshot.child("AceptDate").getValue(String.class);
                        String grandTotalPrice = otherInformationSnapshot.child("GrandTotalPrice").getValue(String.class);
                        // Sử dụng thông tin đã lấy
                        String inputPattern = "HH:mm, dd/MM/yyyy";
                        String outputPattern = "MM/yyyy";
                        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                        String monthYear = null;
                        formattedNumber = null;
                        try {
                            Date date = inputFormat.parse(aceptDate);
                            monthYear = outputFormat.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (thoiGian.equals(monthYear)) {

                            parseDouble = Double.parseDouble(grandTotalPrice);
                            total = total + parseDouble;
                            DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
                            formattedNumber = decimalFormat.format(total);
                            count++;
                        } else {
                            total = 0;
                            count = 0;
                            parseDouble = 0;
                            Revenuee.setText(total + "đ");
                            TotalBillss.setText(count + " đơn");
                        }

                    }
                    Revenuee.setText(formattedNumber + "đ");
                    TotalBillss.setText(count + " đơn");
                }
                }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
}