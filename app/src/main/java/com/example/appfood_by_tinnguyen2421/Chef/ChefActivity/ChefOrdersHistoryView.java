package com.example.appfood_by_tinnguyen2421.Chef.ChefActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders1;
import com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter.ChefOrdersHistoryViewAdapter;
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

public class ChefOrdersHistoryView extends AppCompatActivity {
    RecyclerView recyclerView;
    private List<ChefFinalOrders> chefFinalOrdersList;
    private ChefOrdersHistoryViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID;

    TextView grantotal,idOrders,cusName,ShippingTime;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_orders_history_view);
        recyclerView=findViewById(R.id.rcvItem);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChefOrdersHistoryView.this));
        chefFinalOrdersList = new ArrayList<>();
        ChefordersHistoryView();
    }
    private void ChefordersHistoryView() {
        RandomUID = getIntent().getStringExtra("RandomUID");
        reference = FirebaseDatabase.getInstance().getReference("ChefOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chefFinalOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChefFinalOrders chefFinalOrders = snapshot.getValue(ChefFinalOrders.class);
                    chefFinalOrdersList.add(chefFinalOrders);
                }

                adapter = new ChefOrdersHistoryViewAdapter(ChefOrdersHistoryView.this, chefFinalOrdersList);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChefOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChefFinalOrders1 chefFinalOrders1 = dataSnapshot.getValue(ChefFinalOrders1.class);
                grantotal=findViewById(R.id.txtGrandTotal);
                idOrders=findViewById(R.id.txtOrdersID);
                cusName=findViewById(R.id.txtCusName);
                ShippingTime=findViewById(R.id.txtDatetime);
                grantotal.setText(chefFinalOrders1.getGrandTotalPrice());
                int a=chefFinalOrders1.getRandomUID().length()-10;
                idOrders.setText(chefFinalOrders1.getRandomUID().substring(a));
                cusName.setText(chefFinalOrders1.getName());
                ShippingTime.setText(chefFinalOrders1.getDeliveryDate());


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}