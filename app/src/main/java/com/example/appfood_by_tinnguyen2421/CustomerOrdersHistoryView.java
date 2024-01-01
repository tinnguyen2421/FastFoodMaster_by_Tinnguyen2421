package com.example.appfood_by_tinnguyen2421;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders1;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerFinalOrders;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerFinalOrders1;
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
    private List<CustomerFinalOrders> customerFinalOrdersList;
    private CustomerOrdersHistoryViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID;

    TextView grantotal,idOrders,cusName,ShippingTime;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders_history_view);
        recyclerView=findViewById(R.id.rcvItem);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerOrdersHistoryView.this));
        customerFinalOrdersList = new ArrayList<>();
        ChefordersHistoryView();
    }
    private void ChefordersHistoryView() {
        RandomUID = getIntent().getStringExtra("RandomUID");
        reference = FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerFinalOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CustomerFinalOrders customerFinalOrders = snapshot.getValue(CustomerFinalOrders.class);
                    customerFinalOrdersList.add(customerFinalOrders);
                }

                adapter = new CustomerOrdersHistoryViewAdapter(CustomerOrdersHistoryView.this, customerFinalOrdersList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CustomerFinalOrders1 customerFinalOrders1 = dataSnapshot.getValue(CustomerFinalOrders1.class);
                grantotal=findViewById(R.id.txtGrandTotal);
                idOrders=findViewById(R.id.txtOrdersID);
                cusName=findViewById(R.id.txtCusName);
                ShippingTime=findViewById(R.id.txtDatetime);
                grantotal.setText(customerFinalOrders1.getGrandTotalPrice());
                int a=customerFinalOrders1.getRandomUID().length()-10;
                idOrders.setText(customerFinalOrders1.getRandomUID().substring(a));
                cusName.setText(customerFinalOrders1.getName());
                ShippingTime.setText(customerFinalOrders1.getShippingDate());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}