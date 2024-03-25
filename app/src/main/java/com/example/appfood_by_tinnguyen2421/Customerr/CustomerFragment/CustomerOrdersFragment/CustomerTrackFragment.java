package com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment.CustomerOrdersFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.CustomerTrackAdapter;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.Cart;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders1;
import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.ReusableCodeForAll;
import com.example.appfood_by_tinnguyen2421.SendNotification.APIService;
import com.example.appfood_by_tinnguyen2421.SendNotification.Client;
import com.example.appfood_by_tinnguyen2421.SendNotification.Data;
import com.example.appfood_by_tinnguyen2421.SendNotification.MyResponse;
import com.example.appfood_by_tinnguyen2421.SendNotification.NotificationSender;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerTrackFragment extends Fragment {

    RecyclerView recyclerView;
    private List<CustomerOrders> customerOrdersList;
    private CustomerTrackAdapter adapter;
    DatabaseReference databaseReference;
    ScrollView scrollVieww;
    Button orderCancel;
    TextView grandtotal, orderStatus, orderID, orderDate;
    LinearLayout orderInfo, otherInfo1;
    String chefID,userID;
    ProgressDialog progressDialog;
    private APIService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customertrack, null);
        recyclerView = v.findViewById(R.id.recyclefinalorders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        grandtotal = v.findViewById(R.id.GrandTotal);
        orderInfo = v.findViewById(R.id.OrdersInfo);
        otherInfo1 = v.findViewById(R.id.OtherInfo1);
        orderStatus = v.findViewById(R.id.OrderStatus);
        orderID = v.findViewById(R.id.OrdersID);
        orderDate = v.findViewById(R.id.OrderDate);
        orderCancel=v.findViewById(R.id.OrderCancel);
        scrollVieww = v.findViewById(R.id.scrollView);
        customerOrdersList = new ArrayList<>();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        CustomerTrackOrder();
        CancelOrders();
        return v;
    }
    private void CancelOrders()
    {
        orderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Vui lòng đợi ...");
                progressDialog.show();

                databaseReference = FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        customerOrdersList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Dishes");
                            data.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        final CustomerOrders customerOrders = snapshot1.getValue(CustomerOrders.class);
                                        final HashMap<String, String> hashMap = new HashMap<>();
                                        chefID=customerOrders.getChefID();
                                        userID=customerOrders.getUserID();
                                        hashMap.put("ChefID", customerOrders.getChefID());
                                        hashMap.put("DishID", customerOrders.getDishID());
                                        hashMap.put("DishName", customerOrders.getDishName());
                                        hashMap.put("DishQuantity", customerOrders.getDishQuantity());
                                        hashMap.put("DishPrice", customerOrders.getDishPrice());
                                        hashMap.put("RandomUID",customerOrders.getRandomUID());
                                        hashMap.put("TotalPrice", customerOrders.getTotalPrice());
                                        hashMap.put("UserID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        hashMap.put("ImageURL", customerOrders.getImageURL());
                                        FirebaseDatabase.getInstance().getReference("CustomerOrdersCanceled").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(customerOrders.getRandomUID()).child("Dishes").child(customerOrders.getDishID()).setValue(hashMap);

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
                                    HashMap<String, String> hashMap1 = new HashMap<>();
                                    hashMap1.put("Address", customerOrders1.getAddress());
                                    hashMap1.put("GrandTotalPrice", customerOrders1.getGrandTotalPrice());
                                    hashMap1.put("MobileNumber", customerOrders1.getMobileNumber());
                                    hashMap1.put("RandomUID",customerOrders1.getRandomUID());
                                    hashMap1.put("Name", customerOrders1.getName());
                                    hashMap1.put("Note", customerOrders1.getNote());
                                    hashMap1.put("OrderDate", customerOrders1.getOrderDate());
                                    hashMap1.put("PaymentMethod", customerOrders1.getPaymentMethod());
                                    hashMap1.put("OrderStatus","Chờ xác nhận...");
                                    FirebaseDatabase.getInstance().getReference("CustomerOrdersCanceled").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(customerOrders1.getRandomUID()).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(customerOrders1.getRandomUID()).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(customerOrders1.getRandomUID()).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(chefID).child(customerOrders1.getRandomUID()).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(chefID).child(customerOrders1.getRandomUID()).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(userID).child("isOrdered").setValue("false").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(chefID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                    String usertoken = dataSnapshot.getValue(String.class);
                                                                                    sendNotifications(usertoken, "Đơn bị hủy", "Rất tiếc, đơn hàng vừa bị hủy", "Order");
                                                                                    progressDialog.dismiss();
                                                                                    ReusableCodeForAll.ShowAlert(getContext(),"Thành Công","Đơn hàng đã được hủy !");
                                                                                }
                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                                }
                                                                            });
                                                                                }
                                                                            });

                                                                        }
                                                                    });


                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });

                                            }
                                    });
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
    private void CustomerTrackOrder() {
        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerOrdersList.clear();
                final boolean[] hasData = {false};
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Dishes");
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                CustomerOrders customerOrders = snapshot1.getValue(CustomerOrders.class);
                                customerOrdersList.add(customerOrders);
                            }
                            adapter = new CustomerTrackAdapter(getContext(), customerOrdersList);
                            recyclerView.setAdapter(adapter);
                            if (customerOrdersList.size() != 0) {
                                hasData[0] = true;
                                orderInfo.setVisibility(View.VISIBLE);
                                otherInfo1.setVisibility(View.VISIBLE);
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
                                if (orderStatus.getText().equals("Chờ xác nhận..."))
                                {
                                    orderCancel.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    orderCancel.setVisibility(View.GONE);
                                }
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
                    orderCancel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendNotifications(String usertoken, String title, String message, String order) {

        Data data = new Data(title, message, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(getContext(), "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
