package com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefVoucher;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.CustomerVoucherAdapter;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerVoucherFragment extends Fragment {

    RecyclerView rcvNotifyy;
    CustomerVoucherAdapter adapter;
    List<ChefVoucher> chefVoucherList;
    DatabaseReference databaseReference;

    public CustomerVoucherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_notification, container, false);
        rcvNotifyy = v.findViewById(R.id.rcvNotify);
        chefVoucherList=new ArrayList<>();
        rcvNotifyy.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvNotifyy.setLayoutManager(layoutManager);
        customerVoucher();
        return v;
    }

    private void customerVoucher() {

        databaseReference = FirebaseDatabase.getInstance().getReference("ChefVoucher");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    chefVoucherList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        ChefVoucher chefVoucher = snapshot1.getValue(ChefVoucher.class);
                        chefVoucherList.add(chefVoucher);
                    }
                    if(chefVoucherList.size()==0)
                    {
                        rcvNotifyy.setBackgroundResource(R.drawable.empty_wallet);
                    }
                }
                adapter = new CustomerVoucherAdapter(getContext(), chefVoucherList);
                rcvNotifyy.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}