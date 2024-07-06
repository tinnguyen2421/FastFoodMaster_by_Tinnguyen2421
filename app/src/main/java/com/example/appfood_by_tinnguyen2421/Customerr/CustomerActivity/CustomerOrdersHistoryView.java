package com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.CustomerOrdersHistoryViewAdapter;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders1;
import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.SendNotification.APIService;
import com.example.appfood_by_tinnguyen2421.SendNotification.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrdersHistoryView extends AppCompatActivity {
    RecyclerView recyclerView;
    private List<CustomerOrders> customerOrdersList;
    private CustomerOrdersHistoryViewAdapter adapter;
    DatabaseReference reference;
    String randomUID;

    TextView grantotal,idOrders,cusName, shippingTime;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders_history_view);
        customerOrdersList = new ArrayList<>();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        initializeViews();
        setUpRecyclerView();
        showOrdersHistoryView();

    }

    private void initializeViews() {
        recyclerView=findViewById(R.id.rcvItem);
        grantotal=findViewById(R.id.txtGrandTotal);
        idOrders=findViewById(R.id.txtOrdersID);
        cusName=findViewById(R.id.txtCusName);
        shippingTime =findViewById(R.id.txtDatetime);
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerOrdersHistoryView.this));
    }

    private void showOrdersHistoryView() {
        randomUID = getIntent().getStringExtra("RandomUIDD");
        reference = FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("Dishes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerOrdersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CustomerOrders customerOrders = snapshot.getValue(CustomerOrders.class);
                    customerOrdersList.add(customerOrders);
                }
                adapter = new CustomerOrdersHistoryViewAdapter(CustomerOrdersHistoryView.this, customerOrdersList);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CustomerOrders1 customerOrders1 = dataSnapshot.getValue(CustomerOrders1.class);
                setUpData(customerOrders1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpData(CustomerOrders1 customerOrders1) {
        grantotal.setText(customerOrders1.getGrandTotalPrice());
        int a=customerOrders1.getRandomUID().length()-10;
        idOrders.setText(customerOrders1.getRandomUID().substring(a));
        cusName.setText(customerOrders1.getName());
        shippingTime.setText(customerOrders1.getShippingDate());
    }
}