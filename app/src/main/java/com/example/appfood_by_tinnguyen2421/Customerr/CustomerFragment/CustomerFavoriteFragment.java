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


    RecyclerView rcvcart;
    ArrayList<UpdateDishModel> updateDishModelArrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragmentfavorite,container,false);
        rcvcart= view.findViewById(R.id.rcvcart);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rcvcart.setLayoutManager(linearLayoutManager);
        updateDishModelArrayList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("likedDishes").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                   UpdateDishModel food = dataSnapshot.getValue(UpdateDishModel.class);
                    updateDishModelArrayList.add(food);
                    CustomerFavoriteAdapter foodAdapter = new CustomerFavoriteAdapter(updateDishModelArrayList,getActivity());
                    rcvcart.setAdapter(foodAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }
}
