package com.example.appfood_by_tinnguyen2421.Chef.ChefActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter.ChefDishAdapter;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChefDishes extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private List<UpdateDishModel> updateDishModelList;
    private ChefDishAdapter adapter;
    private DatabaseReference databaseReference;
    private String chefId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String district, city, ward;
    private FloatingActionButton addDishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_category_after_choose);

        recyclerView = findViewById(R.id.rcvHomee);
        updateDishModelList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        chefId = intent.getStringExtra("matl");

        swipeRefreshLayout = findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);

        addDishes = findViewById(R.id.themMoi);
        addDishes.setOnClickListener(view -> startActivity(new Intent(ChefDishes.this, ChefPostDish.class)));

        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference chefRef = FirebaseDatabase.getInstance().getReference("Chef").child(userId);
            chefRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel chef = dataSnapshot.getValue(UserModel.class);
                    if (chef != null) {
                        district = chef.getDistrict();
                        city = chef.getCity();
                        ward = chef.getWard();
                        ChefMenu();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }

    private void ChefMenu() {
        swipeRefreshLayout.setRefreshing(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(city).child(district).child(ward);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateDishModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        UpdateDishModel updateDishModel = snapshot1.getValue(UpdateDishModel.class);
                        if (chefId != null && chefId.equals(updateDishModel.getCateID())) {
                            updateDishModelList.add(updateDishModel);
                        }
                    }
                }
                adapter = new ChefDishAdapter(ChefDishes.this, updateDishModelList);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        ChefMenu();
    }
}
