package com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment.CustomerOrdersFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.PendingOrdersAdapter;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerPendingOrders;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerPendingOrdersFragment extends Fragment {
    RecyclerView recyclerView;
    private List<CustomerPendingOrders> customerPendingOrdersList;
    private PendingOrdersAdapter adapter;
    DatabaseReference databaseReference;
    public CustomerPendingOrdersFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.activity_pending_orders, container, false);
        recyclerView = v.findViewById(R.id.Recycleorders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customerPendingOrdersList = new ArrayList<>();
        CustomerpendingOrders();
        return v;
    }
    private void CustomerpendingOrders() {

        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerPendingOrdersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Dishes");
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                CustomerPendingOrders customerPendingOrders = snapshot1.getValue(CustomerPendingOrders.class);
                                customerPendingOrdersList.add(customerPendingOrders);
                            }
                            adapter = new PendingOrdersAdapter(getContext(), customerPendingOrdersList);
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