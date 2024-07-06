package com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryAdapter.DeliveryPendingOrderViewAdapter;
import com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryModel.DeliveryShipOrders;
import com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryModel.DeliveryShipOrders1;
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
public class DeliveryPendingOrderView extends AppCompatActivity {

    RecyclerView recyclerViewdish;
    private List<DeliveryShipOrders> deliveryShipOrdersList;
    private DeliveryPendingOrderViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID;
    TextView grandtotal, address, customerName, phoneNumber, chefName;
    LinearLayout l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_pending_order_view);
        initializeViews();
        setUpRecyclerView();
        deliveryShipOrdersList = new ArrayList<>();
        showPendingOrders();
    }

    private void setUpRecyclerView() {
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(DeliveryPendingOrderView.this));
    }

    private void initializeViews() {
        recyclerViewdish = findViewById(R.id.delivieworder);
        l1 = (LinearLayout) findViewById(R.id.linear1);
        grandtotal = (TextView) findViewById(R.id.Dtotal);
        address = (TextView) findViewById(R.id.DAddress);
        chefName =(TextView)findViewById(R.id.chefname);
        customerName = (TextView) findViewById(R.id.DName);
        phoneNumber = (TextView) findViewById(R.id.DNumber);
    }

    private void showPendingOrders() {
        RandomUID = getIntent().getStringExtra("Random");

        reference = FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveryShipOrdersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DeliveryShipOrders deliveryShipOrders = snapshot.getValue(DeliveryShipOrders.class);
                    deliveryShipOrdersList.add(deliveryShipOrders);
                }
                if (deliveryShipOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);

                } else {
                    l1.setVisibility(View.VISIBLE);
                }
                adapter = new DeliveryPendingOrderViewAdapter(DeliveryPendingOrderView.this, deliveryShipOrdersList);
                recyclerViewdish.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DeliveryShipOrders1 deliveryShipOrders1 = dataSnapshot.getValue(DeliveryShipOrders1.class);
                setUpData(deliveryShipOrders1);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpData(DeliveryShipOrders1 deliveryShipOrders1) {
        grandtotal.setText( deliveryShipOrders1.getGrandTotalPrice()+"đ");
        address.setText("Địa chỉ:"+deliveryShipOrders1.getAddress());
        customerName.setText("Tên khách hàng:"+deliveryShipOrders1.getName());
        phoneNumber.setText("Số điện thoại:" + deliveryShipOrders1.getMobileNumber());
        chefName.setText("Tên cửa hàng"+ deliveryShipOrders1.getChefName());
    }
}
