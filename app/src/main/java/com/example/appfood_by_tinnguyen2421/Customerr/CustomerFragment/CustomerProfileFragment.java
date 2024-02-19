package com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.CustomerEditProfile;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.CustomerOrdersHistory;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.Customer;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerFinalOrders1;
import com.example.appfood_by_tinnguyen2421.MainMenu;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerProfileFragment extends Fragment {

    RelativeLayout Account,History;
    LinearLayout LogOut;
    private double totalAmount = 0;
    TextView CusNamee,CusRankk,CusTotall;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_profile, container, false);
        LogOut = v.findViewById(R.id.logout_layout);
        Account=v.findViewById(R.id.Account_layout);
        History=v.findViewById(R.id.OrdersHistory);
        CusNamee=v.findViewById(R.id.CusName);
        CusRankk=v.findViewById(R.id.CusRank);
        CusTotall=v.findViewById(R.id.CusTotal);
        //CustomerHistoryOrders();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final Customer customer = snapshot.getValue(Customer.class);
                CusNamee.setText(customer.getFirstName()+customer.getLastName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), CustomerEditProfile.class);
                startActivity(intent);
            }
        });
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( getActivity(), CustomerOrdersHistory.class);
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
    private void CustomerHistoryOrders() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final double[] totalAmount = {0};
                List<Double> orderAmounts = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            CustomerFinalOrders1 customerFinalOrders1 = dataSnapshot.getValue(CustomerFinalOrders1.class);
                            double orderAmount = Double.parseDouble(customerFinalOrders1.getGrandTotalPrice());
                            orderAmounts.add(orderAmount);
                            if (orderAmounts.size() == dataSnapshot.getChildrenCount()) {
                                for (double amount : orderAmounts) {
                                    totalAmount[0] += amount;
                                }
                                CusTotall.setText("Total Amount: " + totalAmount[0]);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





}
