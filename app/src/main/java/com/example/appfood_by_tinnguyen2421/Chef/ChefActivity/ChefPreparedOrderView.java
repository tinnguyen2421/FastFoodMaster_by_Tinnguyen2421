package com.example.appfood_by_tinnguyen2421.Chef.ChefActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter.ChefPreparedOrderViewAdapter;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders1;
import com.example.appfood_by_tinnguyen2421.R;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefPreparedOrderView extends AppCompatActivity {


    RecyclerView recyclerViewdish;
    private List<ChefFinalOrders> chefFinalOrdersList;
    private ChefPreparedOrderViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID, userid;
    TextView grandtotal, address, name, number;
    LinearLayout l1;
    Button Prepared;
    private ProgressDialog progressDialog;
    private APIService apiService;
    Spinner Shipper;
    String deliveryId = "XwBpEfjTGRTIHqqEJQJrEcbKj512";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_prepared_order_view);
        recyclerViewdish = findViewById(R.id.Recycle_viewOrder);
        grandtotal = findViewById(R.id.Gtotal);
        address = findViewById(R.id.Cadress);
        name = findViewById(R.id.Cname);
        number = findViewById(R.id.Cnumber);
        l1 = findViewById(R.id.linear);
        Shipper = findViewById(R.id.shipper);
        Prepared = findViewById(R.id.prepared);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        progressDialog = new ProgressDialog(ChefPreparedOrderView.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(ChefPreparedOrderView.this));
        chefFinalOrdersList = new ArrayList<>();
        CheforderdishesView();
    }

    private void CheforderdishesView() {

        RandomUID = getIntent().getStringExtra("RandomUID");

        reference = FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chefFinalOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChefFinalOrders chefFinalOrders = snapshot.getValue(ChefFinalOrders.class);
                    chefFinalOrdersList.add(chefFinalOrders);
                }
                if (chefFinalOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);

                } else {
                    l1.setVisibility(View.VISIBLE);
                    Prepared.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressDialog.setMessage("Vui lòng đợi...");
                            progressDialog.show();
                            DatabaseReference data = FirebaseDatabase.getInstance().getReference("Chef").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            data.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final UserModel chef = dataSnapshot.getValue(UserModel.class);
                                    final String ChefName = chef.getFirstName() + " " + chef.getLastName();
                                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
                                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                final ChefFinalOrders chefFinalOrders = dataSnapshot1.getValue(ChefFinalOrders.class);
                                                HashMap<String, String> hashMap = new HashMap<>();
                                                String dishid = chefFinalOrders.getDishID();
                                                userid = chefFinalOrders.getUserID();
                                                hashMap.put("ChefID", chefFinalOrders.getChefID());
                                                hashMap.put("DishID", chefFinalOrders.getDishID());
                                                hashMap.put("DishName", chefFinalOrders.getDishName());
                                                hashMap.put("DishPrice", chefFinalOrders.getDishPrice());
                                                hashMap.put("DishQuantity", chefFinalOrders.getDishQuantity());
                                                hashMap.put("RandomUID", RandomUID);
                                                hashMap.put("TotalPrice", chefFinalOrders.getTotalPrice());
                                                hashMap.put("UserID", chefFinalOrders.getUserID());
                                                hashMap.put("ImageURL",chefFinalOrders.getImageURL());
                                                FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(deliveryId).child(RandomUID).child("Dishes").child(dishid).setValue(hashMap);
                                                FirebaseDatabase.getInstance().getReference("ChefOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes").child(dishid).setValue(hashMap);
                                            }
                                            DatabaseReference data = FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                                            data.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    final ChefFinalOrders1 chefFinalOrders1 = dataSnapshot.getValue(ChefFinalOrders1.class);
                                                    HashMap<String, String> hashMap1 = new HashMap<>();
                                                    LocalDateTime currentDateTime = LocalDateTime.now();

                                                    // Định dạng ngày giờ thành chuỗi nếu cần
                                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy");
                                                    String formattedDateTime = currentDateTime.format(formatter);
                                                    String chefid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    hashMap1.put("Address", chefFinalOrders1.getAddress());
                                                    hashMap1.put("ChefID", chefid);
                                                    hashMap1.put("ChefName", ChefName);
                                                    hashMap1.put("GrandTotalPrice", chefFinalOrders1.getGrandTotalPrice());
                                                    hashMap1.put("MobileNumber", chefFinalOrders1.getMobileNumber());
                                                    hashMap1.put("Name", chefFinalOrders1.getName());
                                                    hashMap1.put("RandomUID", RandomUID);
                                                    hashMap1.put("UserID", userid);
                                                    hashMap1.put("DateTime",formattedDateTime);
                                                    hashMap1.put("AceptDate",chefFinalOrders1.getAceptDate());
                                                    hashMap1.put("DeliveryDate",chefFinalOrders1.getDeliveryDate());
                                                    FirebaseDatabase.getInstance().getReference("ChefOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(deliveryId).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(userid).child(RandomUID).child("OtherInformation").child("OrderStatus").setValue("Đã được chuẩn bị...").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    String usertoken = dataSnapshot.getValue(String.class);
                                                                                    sendNotifications(usertoken, "Chuẩn bị đơn hàng", "Đơn hàng của bạn đã được chuẩn bị", "Prepared");
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                        }
                                                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            FirebaseDatabase.getInstance().getReference().child("Tokens").child(deliveryId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    String usertoken = dataSnapshot.getValue(String.class);
                                                                                    sendNotifications(usertoken, "Đơn mới", "Bạn có 1 đơn hàng mới", "DeliveryOrder");
                                                                                    progressDialog.dismiss();
                                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChefPreparedOrderView.this);
                                                                                    builder.setMessage("Đơn hàng đã được gửi tới Người giao hàng");
                                                                                    builder.setCancelable(false);
                                                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            dialog.dismiss();
                                                                                            finish();
                                                                                        }
                                                                                    });
                                                                                    AlertDialog alert = builder.create();
                                                                                    alert.show();
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

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    });

                }
                adapter = new ChefPreparedOrderViewAdapter(ChefPreparedOrderView.this, chefFinalOrdersList);
                recyclerViewdish.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChefFinalOrders1 chefFinalOrders1 = dataSnapshot.getValue(ChefFinalOrders1.class);
                grandtotal.setText(  chefFinalOrders1.getGrandTotalPrice()+"đ");
                address.setText(chefFinalOrders1.getAddress());
                name.setText(chefFinalOrders1.getName());
                number.setText( chefFinalOrders1.getMobileNumber());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifications(String usertoken, String title, String message, String prepared) {

        Data data = new Data(title, message, prepared);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(ChefPreparedOrderView.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
