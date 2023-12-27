package com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment.CustomerOrdersFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.CustomerPayment;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.PayableOrderAdapter;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerPaymentOrders;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerPaymentOrders1;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomerPayableOrdersFragment extends Fragment {
    RecyclerView recyclerView;
    private List<CustomerPaymentOrders> customerPaymentOrdersList;
    private PayableOrderAdapter adapter;
    DatabaseReference databaseReference;
    private LinearLayout pay;
    Button payment;
    TextView grandtotal;
    private SwipeRefreshLayout swipeRefreshLayout;
    public CustomerPayableOrdersFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.activity_payable_orders, container, false);
        recyclerView = v.findViewById(R.id.recyclepayableorder);
        pay = v.findViewById(R.id.btn);
        grandtotal = v.findViewById(R.id.rs);
        payment = (Button) v.findViewById(R.id.paymentmethod);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customerPaymentOrdersList = new ArrayList<>();
        swipeRefreshLayout = v.findViewById(R.id.Swipe2);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
        adapter = new PayableOrderAdapter(getContext(), customerPaymentOrdersList);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView = v.findViewById(R.id.recyclepayableorder);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                customerPaymentOrdersList = new ArrayList<>();
                CustomerpayableOrders();
            }
        });
        CustomerpayableOrders();
        return v;
    }
    private void CustomerpayableOrders() {

        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    customerPaymentOrdersList.clear();
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final String randomuid = snapshot.getKey();
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Dishes");
                        data.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                    CustomerPaymentOrders customerPaymentOrders = snapshot1.getValue(CustomerPaymentOrders.class);
                                    customerPaymentOrdersList.add(customerPaymentOrders);
                                }
                                if (customerPaymentOrdersList.size() == 0) {
                                    pay.setBackgroundResource(R.drawable.empty_product);
                                    pay.setVisibility(View.INVISIBLE);
                                } else {
                                    pay.setVisibility(View.VISIBLE);
                                    payment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getContext(), CustomerPayment.class);
                                            intent.putExtra("RandomUID", randomuid);
                                            startActivity(intent);
                                            //finish();
                                        }
                                    });
                                }
                                adapter = new PayableOrderAdapter(getContext(), customerPaymentOrdersList);
                                recyclerView.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomuid).child("OtherInformation");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    CustomerPaymentOrders1 customerPaymentOrders1 = dataSnapshot.getValue(CustomerPaymentOrders1.class);

                                    //grandtotal.setText(customerPaymentOrders1.getGrandTotalPrice()+"đ");
                                    if (customerPaymentOrders1 != null && customerPaymentOrders1.getGrandTotalPrice() != null) {
                                        String totalPriceString = customerPaymentOrders1.getGrandTotalPrice();

                                        // Loại bỏ dấu phẩy và khoảng trắng từ chuỗi
                                        String totalPriceWithoutComma = totalPriceString.replace(",", "").trim();

                                        try {
                                            // Chuyển đổi chuỗi thành số và định dạng lại
                                            double parsedTotalPrice = Double.parseDouble(totalPriceWithoutComma);

                                            // Sử dụng parsedTotalPrice trong giao diện người dùng với định dạng số
                                            DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
                                            String formattedTotalPrice = decimalFormat.format(parsedTotalPrice);

                                            grandtotal.setText( formattedTotalPrice + "đ");
                                        } catch (NumberFormatException e) {
                                            // Xử lý trường hợp không thể chuyển đổi thành số
                                            e.printStackTrace();
                                        }
                                    }
                                    swipeRefreshLayout.setRefreshing(false);

                                } else {
                                    swipeRefreshLayout.setRefreshing(false);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}