package com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefOrderDishes;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders1;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders;
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

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefPendingOrdersAdapter extends RecyclerView.Adapter<ChefPendingOrdersAdapter.ViewHolder> {

    private Context context;
    private List<ChefFinalOrders1> chefPendingOrders1list;
    private APIService apiService;
    String userid, dishid;


    public ChefPendingOrdersAdapter(Context context, List<ChefFinalOrders1> chefPendingOrders1list) {
            this.chefPendingOrders1list = chefPendingOrders1list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chef_orders, parent, false);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ChefFinalOrders1 chefPendingOrders1 = chefPendingOrders1list.get(position);

        setUpData(holder,chefPendingOrders1,position);
        setUpListeners(holder,chefPendingOrders1);

    }

    private void setUpListeners(ViewHolder holder, ChefFinalOrders1 chefPendingOrders1) {
        holder.vieworder.setOnClickListener(v -> showOrderDishes(chefPendingOrders1.getRandomUID()));
        holder.accept.setOnClickListener(v -> handleAcceptAction(chefPendingOrders1.getRandomUID()));
        holder.reject.setOnClickListener(v -> handleRejectAction(chefPendingOrders1.getRandomUID()));
    }

    private void handleRejectAction(String randomUID) {
        {

            DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("Dishes");
            Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final ChefFinalOrders chefPendingOrders = snapshot.getValue(ChefFinalOrders.class);
                        userid = chefPendingOrders.getUserID();
                        dishid = chefPendingOrders.getDishID();
                    }
                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String usertoken = dataSnapshot.getValue(String.class);
                            sendNotifications(usertoken, "Đơn hàng bị từ chối", "Cửa hàng đã từ chối đơn hàng của bạn vì một số lí do", "Home");
                            FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(userid).child(randomUID).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(userid).child(randomUID).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(userid).child("isOrdered").setValue("false");
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
    }

    private void handleAcceptAction(String randomUID) {
        {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChefPendingOrders")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(randomUID)
                    .child("Dishes");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final CustomerOrders chefPendingOrders = snapshot.getValue(CustomerOrders.class);
                        HashMap<String, String> hashMap = new HashMap<>();
                        String chefid = chefPendingOrders.getChefID();
                        String dishid = chefPendingOrders.getDishID();
                        userid=chefPendingOrders.getUserID();
                        hashMap.put("ChefID", chefPendingOrders.getChefID());
                        hashMap.put("DishID", chefPendingOrders.getDishID());
                        hashMap.put("DishName", chefPendingOrders.getDishName());
                        hashMap.put("DishPrice", chefPendingOrders.getDishPrice());
                        hashMap.put("DishQuantity", chefPendingOrders.getDishQuantity());
                        hashMap.put("RandomUID", chefPendingOrders.getRandomUID());
                        hashMap.put("TotalPrice", chefPendingOrders.getTotalPrice());
                        hashMap.put("UserID", chefPendingOrders.getUserID());
                        hashMap.put("ImageURL",chefPendingOrders.getImageURL());
                        FirebaseDatabase.getInstance().getReference("ChefWaitingOrders").child(chefid).child(randomUID).child("Dishes").child(dishid).setValue(hashMap);
                        FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(userid).child(randomUID).child("Dishes").child(dishid).setValue(hashMap);
                        FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory").child(userid).child(randomUID).child("Dishes").child(dishid).setValue(hashMap);
                    }
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("OtherInformation");
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ChefFinalOrders1 chefPendingOrders11 = dataSnapshot.getValue(ChefFinalOrders1.class);
                            LocalDateTime currentDateTime = LocalDateTime.now();
                            // Định dạng ngày giờ thành chuỗi nếu cần
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy");
                            String formattedDateTime = currentDateTime.format(formatter);
                            HashMap<String, String> hashMap1 = new HashMap<>();
                            hashMap1.put("Address", chefPendingOrders11.getAddress());
                            hashMap1.put("GrandTotalPrice", chefPendingOrders11.getGrandTotalPrice());
                            hashMap1.put("MobileNumber", chefPendingOrders11.getMobileNumber());
                            hashMap1.put("Name", chefPendingOrders11.getName());
                            hashMap1.put("Note", chefPendingOrders11.getNote());
                            hashMap1.put("RandomUID", randomUID);
                            hashMap1.put("AceptDate", formattedDateTime);
                            hashMap1.put("PaymentMethod", chefPendingOrders11.getPaymentMethod());
                            hashMap1.put("OrderStatus","Đang chuẩn bị...");
                            FirebaseDatabase.getInstance().getReference("ChefWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory").child(userid).child(randomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(userid).child(randomUID).child("OtherInformation").child("OrderStatus").setValue("Đang chuẩn bị...").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(userid).child("isOrdered").setValue("false").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    String usertoken = dataSnapshot.getValue(String.class);
                                                                                    sendNotifications(usertoken, "Đơn hàng được chấp nhận", "Đầu bếp đang chuẩn bị cho bạn nè !!!", "Payment");
                                                                                    ReusableCodeForAll.ShowAlert(context,"","Hãy chuẩn bị cho khách bạn nhé");
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    private void showOrderDishes(String randomUID) {
        Intent intent = new Intent(context, ChefOrderDishes.class);
        intent.putExtra("RandomUID", randomUID);
        context.startActivity(intent);
    }

    private void setUpData(ViewHolder holder, ChefFinalOrders1 chefPendingOrders1, int position) {
        holder.position.setText("Đơn hàng số: "+position+1);
        holder.address.setText("Địa chỉ :"+chefPendingOrders1.getAddress());
        if (chefPendingOrders1 != null && chefPendingOrders1.getGrandTotalPrice() != null) {
            String priceString = chefPendingOrders1.getGrandTotalPrice();
            String priceWithoutComma = priceString.replace(",", "").trim();
            try {
                double parsedNumber = Double.parseDouble(priceWithoutComma);
                DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
                String formattedPrice = decimalFormat.format(parsedNumber);
                holder.grandtotalprice.setText("Tổng đơn hàng: " + formattedPrice + "đ");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotifications(String usertoken, String title, String message, String order) {

        Data data = new Data(title, message, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return chefPendingOrders1list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView position, address, grandtotalprice;
        Button vieworder, accept, reject;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            position =itemView.findViewById(R.id.IdOrders);
            address = itemView.findViewById(R.id.AD);
            grandtotalprice = itemView.findViewById(R.id.TP);
            vieworder = itemView.findViewById(R.id.vieww);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);


        }
    }
}