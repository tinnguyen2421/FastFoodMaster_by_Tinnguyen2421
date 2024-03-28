package com.example.appfood_by_tinnguyen2421.Chef.ChefFragment.ChefOrdersFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter.ChefOrderTobePreparedAdapter;
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

public class ChefOrderTobePreparedFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<ChefFinalOrders1> chefFinalOrders1List;
    private ChefOrderTobePreparedAdapter adapter;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chef_order_tobe_prepared, container, false);
        chefFinalOrders1List = new ArrayList<>();
        initializeViews(v);
        setUpRecyclerView();
        setUpSwipeRefresh(v);
        setUpListeners();
        showOrdersToBePrepared();
        return v;
    }

    private void setUpListeners() {
        swipeRefreshLayout.setOnRefreshListener(() -> refreshLayout());
    }
    private void refreshLayout()
    {
        chefFinalOrders1List.clear();
        showOrdersToBePrepared();
    }
    private void setUpSwipeRefresh(View v) {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
    }

    private void initializeViews(View v) {
        recyclerView = v.findViewById(R.id.Recycle_orderstobeprepared);
        swipeRefreshLayout = v.findViewById(R.id.Swipe1);

    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChefOrderTobePreparedAdapter(getContext(), chefFinalOrders1List);
        recyclerView.setAdapter(adapter);
    }

    private void showOrdersToBePrepared() {
        databaseReference = FirebaseDatabase.getInstance().getReference("ChefWaitingOrders")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    chefFinalOrders1List.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("ChefWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ChefFinalOrders1 chefFinalOrders1 = dataSnapshot.getValue(ChefFinalOrders1.class);
                                chefFinalOrders1List.add(chefFinalOrders1);
                                adapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    recyclerView.setBackgroundResource(chefFinalOrders1List.isEmpty() ? R.drawable.empty_cart : 0); // Hiển thị layout trống nếu danh sách rỗng
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}