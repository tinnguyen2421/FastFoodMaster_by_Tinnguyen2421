package com.example.appfood_by_tinnguyen2421.Chef.ChefFragment.ChefOrdersFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter.ChefPendingOrdersAdapter;
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
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefPendingOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ChefFinalOrders1> chefPendingOrders1List;
    private ChefPendingOrdersAdapter adapter;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_chef_pendingorders, null);
        chefPendingOrders1List = new ArrayList<>();
        initializeViews(v);
        setUpRecyclerView();
        setUpSwipeRefresh();
        setUpListeners();
        showPendingOrders();
        return v;
    }

    private void setUpListeners() {
        swipeRefreshLayout.setOnRefreshListener(() -> refresh());
    }

    private void refresh() {
        chefPendingOrders1List.clear();
        showPendingOrders();
    }

    private void setUpSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChefPendingOrdersAdapter(getContext(), chefPendingOrders1List);
        recyclerView.setAdapter(adapter);
    }

    private void initializeViews(View v) {
        recyclerView = v.findViewById(R.id.Recycle_orders);
        swipeRefreshLayout = v.findViewById(R.id.Swipe_layoutt);

    }

    private void showPendingOrders() {
        databaseReference = FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    chefPendingOrders1List.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ChefFinalOrders1 chefPendingOrders1 = dataSnapshot.getValue(ChefFinalOrders1.class);
                                chefPendingOrders1List.add(chefPendingOrders1);
                                adapter.notifyDataSetChanged(); // Cập nhật adapter sau khi thêm dữ liệu vào danh sách
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    recyclerView.setBackgroundResource(R.drawable.empty); // Nếu danh sách rỗng, hiển thị layout trống
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}