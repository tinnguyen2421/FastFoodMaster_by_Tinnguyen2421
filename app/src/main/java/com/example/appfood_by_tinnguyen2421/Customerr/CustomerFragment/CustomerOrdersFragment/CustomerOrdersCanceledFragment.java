package com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment.CustomerOrdersFragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.PendingOrdersAdapter;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders1;
import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerOrdersCanceledFragment extends Fragment {
    RecyclerView recyclerView;
    private List<CustomerOrders> customerOrdersCanceledList;
    private PendingOrdersAdapter adapter;
    DatabaseReference databaseReference,databaseReference1,dataaa;
    ScrollView scrollVieww;
    private List<CustomerOrders> customerOrdersList;
    Button reOrder;
    TextView grandtotal, orderStatus, orderID, orderDate;
    LinearLayout orderInfo, otherInfo1;
    String chefID, City,District,Ward,Price;
    ProgressDialog progressDialog;
    public CustomerOrdersCanceledFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customerorderscanceled, null);
        recyclerView = v.findViewById(R.id.recyclefinalorders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        grandtotal = v.findViewById(R.id.GrandTotal);
        orderInfo = v.findViewById(R.id.OrdersInfo);
        otherInfo1 = v.findViewById(R.id.OtherInfo1);
        orderStatus = v.findViewById(R.id.OrderStatus);
        orderID = v.findViewById(R.id.OrdersID);
        orderDate = v.findViewById(R.id.OrderDate);
        scrollVieww = v.findViewById(R.id.scrollView);
        reOrder=v.findViewById(R.id.ReOrder);
        customerOrdersList = new ArrayList<>();
        customerOrdersCanceledList = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        CustomerOrdersCanceled();
        ReOrder();
        return v;
    }
    private void getDishPrice(String chefID, String randomUID) {

    }


    private void ReOrder() {
        reOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference("CustomerOrdersCanceled").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        customerOrdersList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerOrdersCanceled").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Dishes");
                            data.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        final CustomerOrders customerOrders = snapshot1.getValue(CustomerOrders.class);
                                        final HashMap<String, String> hashMap = new HashMap<>();
                                        chefID=customerOrders.getChefID();

                                        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
                                        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                UserModel cust = dataSnapshot.getValue(UserModel.class);
                                                District = cust.getDistrict();
                                                City = cust.getCity();
                                                Ward = cust.getWard();
                                                databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(City).child(District).child(Ward).child(chefID).child(customerOrders.getDishID());
                                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                                                        databaseReference1 = FirebaseDatabase.getInstance().getReference("ChefStatus").child(updateDishModel.getChefID() + "/Status");
                                                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                String status = dataSnapshot.getValue(String.class);
                                                                if (status.equals("Mở cửa")) {
                                                                    if (updateDishModel.getOnSale().equals("true")) {
                                                                        Price = updateDishModel.getReducePrice();
                                                                        Log.d("Check Price", "getDishPrice: " + Price);

                                                                        if (updateDishModel.getAvailableDish().equals("true")) {
                                                                            // Do something
                                                                        } else {
                                                                            // Do something
                                                                        }
                                                                    } else {
                                                                        Price = updateDishModel.getDishPrice();
                                                                        Log.d("Check Price", "getDishPrice: " + Price);
                                                                    }
                                                                    hashMap.put("ChefID", customerOrders.getChefID());
                                                                    hashMap.put("DishID", customerOrders.getDishID());
                                                                    hashMap.put("DishName", customerOrders.getDishName());
                                                                    hashMap.put("DishQuantity", "1");
                                                                    hashMap.put("DishPrice", String.valueOf(Price));
                                                                    hashMap.put("RandomUID",customerOrders.getRandomUID());
                                                                    hashMap.put("TotalPrice", String.valueOf(Price));
                                                                    hashMap.put("UserID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                    hashMap.put("ImageURL", customerOrders.getImageURL());
                                                                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(customerOrders.getDishID()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            FirebaseDatabase.getInstance().getReference("CustomerOrdersCanceled").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(customerOrders.getRandomUID()).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    FirebaseDatabase.getInstance().getReference("CustomerOrdersCanceled").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(customerOrders.getRandomUID()).child("OtherInformation").removeValue()   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                } else {
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                // Handle onCancelled event
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        // Handle onCancelled event
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Handle onCancelled event
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void CustomerOrdersCanceled() {
        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerOrdersCanceled").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerOrdersCanceledList.clear();
                final boolean[] hasData = {false};
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerOrdersCanceled").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Dishes");
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                CustomerOrders customerOrders = snapshot1.getValue(CustomerOrders.class);
                                customerOrdersCanceledList.add(customerOrders);
                            }
                            adapter = new PendingOrdersAdapter(getContext(), customerOrdersCanceledList);
                            recyclerView.setAdapter(adapter);
                            if (customerOrdersCanceledList.size() != 0) {
                                hasData[0] = true;
                                orderInfo.setVisibility(View.VISIBLE);
                                otherInfo1.setVisibility(View.VISIBLE);
                                reOrder.setVisibility(View.VISIBLE);
                                scrollVieww.setBackground(null);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            CustomerOrders1 customerOrders1 = dataSnapshot.getValue(CustomerOrders1.class);
                            try {
                                int a = customerOrders1.getRandomUID().length() - 10;
                                orderID.setText(customerOrders1.getRandomUID().substring(a));
                                orderDate.setText(customerOrders1.getOrderDate());
                                orderStatus.setText(customerOrders1.getOrderStatus());
                                grandtotal.setText(customerOrders1.getGrandTotalPrice() + "đ");
                            } catch (Exception e) {
                                Log.d("CustomerTrackFragment", "onDataChange: " + e);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                if (!hasData[0]) {
                    scrollVieww.setBackgroundResource(R.drawable.empty_product);
                    orderInfo.setVisibility(View.GONE);
                    otherInfo1.setVisibility(View.GONE);
                    reOrder.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}