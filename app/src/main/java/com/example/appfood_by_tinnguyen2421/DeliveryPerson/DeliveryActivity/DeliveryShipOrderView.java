package com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryAdapter.DeliveryShipOrderViewAdapter;
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
public class DeliveryShipOrderView extends AppCompatActivity {

    RecyclerView recyclerViewdish;
    private List<DeliveryShipOrders> deliveryShipOrdersList;
    private DeliveryShipOrderViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID;
    TextView grandtotal, address, name, number, ChefName;
    LinearLayout l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_ship_order_view);
        recyclerViewdish = findViewById(R.id.delishipvieworder);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(DeliveryShipOrderView.this));
        l1 = (LinearLayout) findViewById(R.id.linear2);
        grandtotal = (TextView) findViewById(R.id.Shiptotal);
        ChefName = (TextView) findViewById(R.id.chefname1);
        address = (TextView) findViewById(R.id.ShipAddress);
        name = (TextView) findViewById(R.id.ShipName);
        number = (TextView) findViewById(R.id.ShipNumber);
        deliveryShipOrdersList = new ArrayList<>();
        deliveryfinalorders();
    }

    private void deliveryfinalorders() {

        RandomUID = getIntent().getStringExtra("RandomUID");

        reference = FirebaseDatabase.getInstance().getReference("DeliveryShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
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
                adapter = new DeliveryShipOrderViewAdapter(DeliveryShipOrderView.this, deliveryShipOrdersList);
                recyclerViewdish.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DeliveryShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DeliveryShipOrders1 deliveryShipOrders1 = dataSnapshot.getValue(DeliveryShipOrders1.class);
                grandtotal.setText( deliveryShipOrders1.getGrandTotalPrice()+"đ");
                address.setText(deliveryShipOrders1.getAddress());
                name.setText(deliveryShipOrders1.getName());
                number.setText("Số điện thoại :" + deliveryShipOrders1.getMobileNumber());
                ChefName.setText("Tên Cửa hàng :" + deliveryShipOrders1.getChefName());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
