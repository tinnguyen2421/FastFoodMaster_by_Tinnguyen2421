package com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.CustomerOrdersHistoryAdapter;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders1;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrdersHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<CustomerOrders1> customerOrders1List;

    private CustomerOrdersHistoryAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders_history);
        customerOrders1List = new ArrayList<>();
        initializeViews();
        setUpRecyclerView();
        showOrdersHistory();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.RcvHistory);
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerOrdersHistory.this));
    }

    private void showOrdersHistory() {

        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerOrders1List.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            CustomerOrders1 customerOrders1 = dataSnapshot.getValue(CustomerOrders1.class);
                            customerOrders1List.add(customerOrders1);
                            adapter = new CustomerOrdersHistoryAdapter(CustomerOrdersHistory.this, customerOrders1List);
                            recyclerView.setAdapter(adapter);
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