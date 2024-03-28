package com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.CustomerFavoriteAdapter;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerFavoriteFragment extends Fragment {

    private static CustomerFavoriteFragment instance = null;

    RecyclerView rcvcart;
    ArrayList<UpdateDishModel> updateDishModelArrayList;
    CustomerFavoriteAdapter customerFavoriteAdapter;

    //singleton
    public static CustomerFavoriteFragment getInstance() {
        if (instance == null) {
            instance = new CustomerFavoriteFragment();
        }
        return instance;
    }

    public CustomerFavoriteFragment() {
        // Private constructor to prevent instantiation
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentfavorite, container, false);
        rcvcart = view.findViewById(R.id.rcvcart);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcvcart.setLayoutManager(linearLayoutManager);
        updateDishModelArrayList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("likedDishes").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateDishModelArrayList.clear(); // Clear previous data before adding new ones
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UpdateDishModel food = dataSnapshot.getValue(UpdateDishModel.class);
                    updateDishModelArrayList.add(food);
                }
                if (updateDishModelArrayList.isEmpty()) {
                    rcvcart.setBackgroundResource(R.drawable.empty_bill);
                } else {
                    if (customerFavoriteAdapter == null) {
                        customerFavoriteAdapter = new CustomerFavoriteAdapter(updateDishModelArrayList, getActivity());
                        rcvcart.setAdapter(customerFavoriteAdapter);
                    } else {
                        customerFavoriteAdapter.notifyDataSetChanged(); // Notify adapter for data changes
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
        return view;
    }
}
