package com.example.appfood_by_tinnguyen2421.Chef.ChefActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders1;
import com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter.ChefOrdersHistoryAdapter;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChefOrdersHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<ChefFinalOrders1> chefFinalOrders1List;

    private ChefOrdersHistoryAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_orders_history);
        recyclerView = findViewById(R.id.RcvHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChefOrdersHistory.this));
        chefFinalOrders1List = new ArrayList<>();
        ChefOrdersHistoryList();
    }

    private void ChefOrdersHistoryList() {

        databaseReference = FirebaseDatabase.getInstance().getReference("ChefOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chefFinalOrders1List.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("ChefOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ChefFinalOrders1 chefFinalOrders1 = dataSnapshot.getValue(ChefFinalOrders1.class);
                            chefFinalOrders1List.add(chefFinalOrders1);
                            adapter = new ChefOrdersHistoryAdapter(ChefOrdersHistory.this, chefFinalOrders1List);
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