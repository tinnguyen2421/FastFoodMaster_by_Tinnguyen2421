package com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.BottomNavigation.CustomerFoodPanel_BottomNavigation;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerFinalOrders;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerPaymentOrders1;
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

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//delete this class
public class CustomerPayment extends AppCompatActivity {

    TextView OnlinePayment, CashPayment;
    String RandomUID, ChefID;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment);

        OnlinePayment = (TextView) findViewById(R.id.online);
        CashPayment = (TextView) findViewById(R.id.cash);
        RandomUID = getIntent().getStringExtra("RandomUIDDD");
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        OnlinePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerPayment.this, CustomerOnlinePayment.class);
                intent.putExtra("randomUID", RandomUID);
                startActivity(intent);
            }
        });
        CashPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
                Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final CustomerFinalOrders customerFinalOrders = snapshot.getValue(CustomerFinalOrders.class);
                            HashMap<String, String> hashMap2 = new HashMap<>();
                            String dishid = customerFinalOrders.getDishID();
                            ChefID = customerFinalOrders.getChefId();
                            hashMap2.put("ChefId", customerFinalOrders.getChefId());
                            hashMap2.put("DishId", customerFinalOrders.getDishID());
                            hashMap2.put("DishName", customerFinalOrders.getDishName());
                            hashMap2.put("DishPrice", customerFinalOrders.getDishPrice());
                            hashMap2.put("DishQuantity", customerFinalOrders.getDishQuantity());
                            hashMap2.put("RandomUID", customerFinalOrders.getRandomUID());
                            hashMap2.put("TotalPrice", customerFinalOrders.getTotalPrice());
                            hashMap2.put("UserId", customerFinalOrders.getUserId());
                            FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes").setValue(hashMap2);
                            FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(ChefID).child(RandomUID).child("Dishes").setValue(hashMap2);
                        }
                        DatabaseReference dataa = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                        dataa.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                CustomerPaymentOrders1 customerPaymentOrders11 = dataSnapshot.getValue(CustomerPaymentOrders1.class);
                                HashMap<String, String> hashMap3 = new HashMap<>();
                                hashMap3.put("Address", customerPaymentOrders11.getAddress());
                                hashMap3.put("GrandTotalPrice", customerPaymentOrders11.getGrandTotalPrice());
                                hashMap3.put("MobileNumber", customerPaymentOrders11.getMobileNumber());
                                hashMap3.put("Name", customerPaymentOrders11.getName());
                                hashMap3.put("Note", customerPaymentOrders11.getNote());
                                hashMap3.put("RandomUID", customerPaymentOrders11.getRandomUID());
                                hashMap3.put("Status", "Đơn hàng của bạn đang chờ Cửa hàng xác nhận...");
                                hashMap3.put("OrderDate", customerPaymentOrders11.getOrderDate());
                                hashMap3.put("AceptDate",customerPaymentOrders11.getPaymentMethod());
                                FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(ChefID).child(RandomUID).child("OtherInformation").setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(ChefID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isOrdered").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

                                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(ChefID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                                        sendNotifications(usertoken, "Đơn hàng mới", "Bạn có đơn hàng mới", "Order");
                                                                                        //progressDialog.dismiss();
                                                                                        ReusableCodeForAll.ShowAlert(CustomerPayment.this, "", "Đơn hàng của bạn đã được chuyển sang trạng thái Đang chờ xử lý, vui lòng đợi cho đến khi cửa hàng chấp nhận đơn hàng của bạn");
                                                                                    }
                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                        sendNotifications(usertoken, "Xác nhận đặt hàng", "Chế độ thanh toán được xác nhận, Bây giờ bạn có thể bắt đầu đặt hàng", "Confirm");
                                                                    }
                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });

                                                            }
                                                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerPayment.this);
                                                                builder.setMessage("Đơn hàng đã được thanh toán, Bây giờ bạn có thể theo dõi đơn hàng của mình.");
                                                                builder.setCancelable(false);
                                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        dialog.dismiss();
                                                                        Intent b = new Intent(CustomerPayment.this, CustomerFoodPanel_BottomNavigation.class);
                                                                        b.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        startActivity(b);
                                                                        finish();

                                                                    }
                                                                });
                                                                AlertDialog alert = builder.create();
                                                                alert.show();
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
        });
    }

    private void sendNotifications(String usertoken, String title, String message, String confirm) {
        Data data = new Data(title, message, confirm);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(CustomerPayment.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
