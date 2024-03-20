package com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.CustomerEditProfile;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.CustomerOrdersHistory;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders1;
import com.example.appfood_by_tinnguyen2421.MainMenu;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerProfileFragment extends Fragment {

    private double total, parseDouble;

    RelativeLayout Account, History;
    LinearLayout LogOut;
    private double totalAmount = 0;
    TextView CusNamee, CusRankk, CusTotall;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_profile, container, false);
        LogOut = v.findViewById(R.id.logout_layout);
        Account = v.findViewById(R.id.Account_layout);
        History = v.findViewById(R.id.OrdersHistory);
        CusNamee = v.findViewById(R.id.CusName);
        CusRankk = v.findViewById(R.id.CusRank);
        CusTotall = v.findViewById(R.id.CusTotal);
        QueryTime();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final UserModel userModel = snapshot.getValue(UserModel.class);
                CusNamee.setText(userModel.getFirstName() + userModel.getLastName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomerEditProfile.class);
                startActivity(intent);
            }
        });
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomerOrdersHistory.class);
                startActivity(intent);
            }
        });
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn có muốn đăng xuất ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), MainMenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        return v;
    }
    private void QueryTime() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory");
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
                        String grandTotalPrice = otherInformationSnapshot.child("GrandTotalPrice").getValue(String.class);
                            parseDouble = Double.parseDouble(grandTotalPrice);
                            total = total + parseDouble;
                            DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
                            formattedNumber = decimalFormat.format(total);

                    }
                    CusTotall.setText(formattedNumber + "đ");
                    if (0<total&&total<100000)
                    {
                        CusRankk.setText("Thành viên Sắt");
                    }
                    else if(100000<total&&total<1000000)
                    {
                        CusRankk.setText("Thành viên Đồng");
                    }
                    else if (1000000<total&&total<5000000) {
                        CusRankk.setText("Thành viên Bạc");
                    }
                    else if (5000000<total&&total<10000000) {
                        CusRankk.setText("Thành viên Vàng");
                    }
                    else if (10000000<total&&total<15000000) {
                        CusRankk.setText("Thành viên Bạch kim");
                    }

                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }





}
