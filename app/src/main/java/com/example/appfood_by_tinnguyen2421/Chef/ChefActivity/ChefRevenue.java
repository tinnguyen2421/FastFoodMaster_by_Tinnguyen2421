package com.example.appfood_by_tinnguyen2421.Chef.ChefActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.appfood_by_tinnguyen2421.DesignPattern.FactoryMethod.ChefRevenueFactory;
import com.example.appfood_by_tinnguyen2421.DesignPattern.FactoryMethod.ChefRevenueFactoryImpl;
import com.example.appfood_by_tinnguyen2421.R;
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
import java.util.HashMap;
import java.util.Map;

public class ChefRevenue extends AppCompatActivity {
    private double total, parseDouble;
    private int month, year, count;
    TextView TimePickerr, Revenuee, TotalBillss, BestSellerDish, amountSold;
    private double totalAmount = 0;
    Button Refresh;
    private ChefRevenueFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_revenue);
        factory = new ChefRevenueFactoryImpl(); // Khởi tạo Factory
        ChefRevenue chefRevenue = factory.createChefRevenue(); // Tạo đối tượng ChefRevenue bằng Factory
        initializeViews();
        setUpListeners();
    }

    private void setUpListeners() {
        TimePickerr.setOnClickListener(view -> TimePicker());
        Refresh.setOnClickListener(view -> refresh());
    }

    private void initializeViews() {
        Refresh = findViewById(R.id.btnRefresh);
        TimePickerr = findViewById(R.id.timePicker);
        Revenuee = findViewById(R.id.Revenue);
        TotalBillss = findViewById(R.id.TotalBills);
        BestSellerDish = findViewById(R.id.BestSeller);
        amountSold = findViewById(R.id.AmountSold);
    }

    private void refresh() {
        TimePickerr.setText("");
        Revenuee.setText("");
        TotalBillss.setText("");
        BestSellerDish.setText("");
        amountSold.setText("");
    }

    private void TimePicker() {
        count = 0;
        total = 0;
        parseDouble = 0;

        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ChefRevenue.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String monthString = String.valueOf(monthOfYear + 1);
                    if (monthOfYear + 1 < 10) {
                        monthString = "0" + monthString;
                    }
                    String thoiGian = monthString + "/" + year;
                    TimePickerr.setText(thoiGian);
                    calculateRevenue(thoiGian);
                },
                year, month, 1 // 1 là ngày mặc định khi hiển thị DatePickerDialog
        );
        datePickerDialog.show();
    }

    private void calculateRevenue(String time) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChefOrdersHistory");
        Query query = reference.orderByChild(FirebaseAuth.getInstance().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot chefSnapshot : dataSnapshot.getChildren()) {
                    String formattedNumber = null;
                    for (DataSnapshot orderSnapshot : chefSnapshot.getChildren()) {
                        DataSnapshot otherInformationSnapshot = orderSnapshot.child("OtherInformation");
                        String aceptDate = otherInformationSnapshot.child("AceptDate").getValue(String.class);
                        String grandTotalPrice = otherInformationSnapshot.child("GrandTotalPrice").getValue(String.class);
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
                        if (time.equals(monthYear)) {
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
                calculateSalesQuantity(time);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void calculateSalesQuantity(String time) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChefOrdersHistory");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Integer> productQuantities = new HashMap<>();

                for (DataSnapshot chefSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : chefSnapshot.getChildren()) {
                        DataSnapshot otherInformationSnapshot = orderSnapshot.child("OtherInformation");
                        String chefID = otherInformationSnapshot.child("ChefID").getValue(String.class);
                        if (chefID != null && chefID.equals(FirebaseAuth.getInstance().getUid())) {
                            for (DataSnapshot dishSnapshot : orderSnapshot.child("Dishes").getChildren()) {
                                String dishName = dishSnapshot.child("DishName").getValue(String.class);
                                int quantity = Integer.parseInt(dishSnapshot.child("DishQuantity").getValue(String.class));
                                productQuantities.put(dishName, productQuantities.getOrDefault(dishName, 0) + quantity);
                            }
                        }
                    }
                }
                String mostSoldProduct = null;
                int maxQuantity = 0;
                for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                    if (entry.getValue() > maxQuantity) {
                        mostSoldProduct = entry.getKey();
                        maxQuantity = entry.getValue();
                    }
                }
                if (mostSoldProduct != null) {
                    BestSellerDish.setText(mostSoldProduct);
                    amountSold.setText(String.valueOf(maxQuantity));
                } else {
                    BestSellerDish.setText("Không có dữ liệu");
                    amountSold.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

}