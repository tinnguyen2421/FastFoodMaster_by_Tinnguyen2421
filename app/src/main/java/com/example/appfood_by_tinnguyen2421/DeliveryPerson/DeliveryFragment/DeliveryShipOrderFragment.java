package com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryAdapter.DeliveryShipOrderFragmentAdapter;

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

public class DeliveryShipOrderFragment extends Fragment {


    private RecyclerView recyclerView;
    private List<DeliveryShipOrders1> deliveryShipOrders1List;
    private DeliveryShipOrderFragmentAdapter adapter;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_deliveryshiporder, null);
        recyclerView = view.findViewById(R.id.delishiporder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        deliveryShipOrders1List = new ArrayList<>();
        DeliveryShipfinalOrder();
        return view;

    }

    private void DeliveryShipfinalOrder() {
       // if (getActivity() != null) {
          //  ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
          //  if (actionBar != null) {
          //      actionBar.hide();
          //  }
       // }
        databaseReference = FirebaseDatabase.getInstance().getReference("DeliveryShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveryShipOrders1List.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("DeliveryShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            DeliveryShipOrders1 deliveryShipOrders1 = dataSnapshot.getValue(DeliveryShipOrders1.class);
                            deliveryShipOrders1List.add(deliveryShipOrders1);
                            adapter = new DeliveryShipOrderFragmentAdapter(getContext(),deliveryShipOrders1List);
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
