package com.example.appfood_by_tinnguyen2421.Chef.ChefFragment.ChefOrdersFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter.ChefPreparedOrderAdapter;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders1;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChefPreparedOrderFragment extends Fragment {

    public ChefPreparedOrderFragment() {
        // Required empty public constructor
    }
    private RecyclerView recyclerView;
    private List<ChefFinalOrders1> chefFinalOrders1List;
    private ChefPreparedOrderAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_chef_prepared_order, container, false);
        chefFinalOrders1List = new ArrayList<>();
        initializeViews(v);
        setUpRecyclerView();
        showPreparedOrders();
        return v;

    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initializeViews(View v) {
        recyclerView = v.findViewById(R.id.Recycle_preparedOrders);

    }

    private void showPreparedOrders() {

        databaseReference = FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chefFinalOrders1List.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ChefFinalOrders1 chefFinalOrders1 = dataSnapshot.getValue(ChefFinalOrders1.class);
                            chefFinalOrders1List.add(chefFinalOrders1);
                            adapter = new ChefPreparedOrderAdapter(getContext(), chefFinalOrders1List);
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