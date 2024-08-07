package com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryAdapter;

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

import com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryActivity.DeliveryPendingOrderView;
import com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryModel.DeliveryShipOrders;
import com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryModel.DeliveryShipOrders1;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class DeliveryPendingOrderFragmentAdapter extends RecyclerView.Adapter<DeliveryPendingOrderFragmentAdapter.ViewHolder> {

    private Context context;
    private List<DeliveryShipOrders1> deliveryShipOrders1list;
    private APIService apiService;
    String chefid;



    public DeliveryPendingOrderFragmentAdapter(Context context, List<DeliveryShipOrders1> deliveryShipOrders1list) {
        this.deliveryShipOrders1list = deliveryShipOrders1list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_pendingorders, parent, false);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DeliveryShipOrders1 deliveryShipOrders1 = deliveryShipOrders1list.get(position);
        setUpData(holder,deliveryShipOrders1,position);
        setUpListeners(holder,deliveryShipOrders1);


    }

    private void setUpData(ViewHolder holder, DeliveryShipOrders1 deliveryShipOrders1, int position) {
        holder.Stt.setText("Đơn hàng số:"+position+1);
        holder.Address.setText("Địa chỉ :"+deliveryShipOrders1.getAddress());
        holder.mobilenumber.setText("+84" + deliveryShipOrders1.getMobileNumber());
        holder.grandtotalprice.setText("Tổng tiền:" + deliveryShipOrders1.getGrandTotalPrice()+"đ");
    }

    private void setUpListeners(ViewHolder holder, DeliveryShipOrders1 deliveryShipOrders1) {
        holder.viewOrder.setOnClickListener(v -> showOrders(deliveryShipOrders1.getRandomUID()));
        holder.Accept.setOnClickListener(v -> acceptOrder(deliveryShipOrders1,deliveryShipOrders1.getRandomUID()));
        holder.Reject.setOnClickListener(v -> rejectOrder(deliveryShipOrders1,deliveryShipOrders1.getRandomUID()));
    }

    private void rejectOrder(DeliveryShipOrders1 deliveryShipOrders1, String randomUID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("Dishes");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    DeliveryShipOrders deliveryShipOrders = dataSnapshot1.getValue(DeliveryShipOrders.class);
                    chefid = deliveryShipOrders.getChefID();
                }

                FirebaseDatabase.getInstance().getReference().child("Tokens").child(chefid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String usertoken = dataSnapshot.getValue(String.class);
                        sendNotifications(usertoken, "Đơn hàng bị từ chối", "Đơn hàng của bạn đã bị người giao hàng từ chối", "RejectOrder");
                        FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("OtherInformation").removeValue();
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

    private void acceptOrder(DeliveryShipOrders1 deliveryShipOrders1, String randomUID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("Dishes");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DeliveryShipOrders deliveryShipOrderss = snapshot.getValue(DeliveryShipOrders.class);
                    HashMap<String, String> hashMap = new HashMap<>();
                    String dishid = deliveryShipOrderss.getDishID();
                    chefid = deliveryShipOrderss.getChefID();
                    hashMap.put("ChefID", deliveryShipOrderss.getChefID());
                    hashMap.put("DishID", deliveryShipOrderss.getDishID());
                    hashMap.put("DishName", deliveryShipOrderss.getDishName());
                    hashMap.put("DishPrice", deliveryShipOrderss.getDishPrice());
                    hashMap.put("DishQuantity", deliveryShipOrderss.getDishQuantity());
                    hashMap.put("RandomUID", deliveryShipOrderss.getRandomUID());
                    hashMap.put("TotalPrice", deliveryShipOrderss.getTotalPrice());
                    hashMap.put("UserID", deliveryShipOrderss.getUserID());
                    hashMap.put("ImageURL", deliveryShipOrderss.getImageURL());
                    FirebaseDatabase.getInstance().getReference("DeliveryShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("Dishes").child(dishid).setValue(hashMap);

                }

                DatabaseReference data = FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("OtherInformation");
                data.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DeliveryShipOrders1 deliveryShipOrders11 = dataSnapshot.getValue(DeliveryShipOrders1.class);
                        HashMap<String, String> hashMap1 = new HashMap<>();
                        LocalDateTime currentDateTime = LocalDateTime.now();

                        // Định dạng ngày giờ thành chuỗi nếu cần
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        String formattedDateTime = currentDateTime.format(formatter);
                        hashMap1.put("Address", deliveryShipOrders11.getAddress());
                        hashMap1.put("ChefID", deliveryShipOrders11.getChefID());
                        hashMap1.put("ChefName", deliveryShipOrders11.getChefName());
                        hashMap1.put("GrandTotalPrice", deliveryShipOrders11.getGrandTotalPrice());
                        hashMap1.put("MobileNumber", deliveryShipOrders11.getMobileNumber());
                        hashMap1.put("Name", deliveryShipOrders11.getName());
                        hashMap1.put("RandomUID", randomUID);
                        hashMap1.put("UserID", deliveryShipOrders11.getUserID());
                        hashMap1.put("AceptDate",formattedDateTime);
                        FirebaseDatabase.getInstance().getReference("DeliveryShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(deliveryShipOrders1.getUserID()).child(randomUID).child("OtherInformation").child("OrderStatus").setValue("Đang trên đường giao...").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory").child(deliveryShipOrders1.getUserID()).child(randomUID).child("OtherInformation").child("OrderStatus").setValue("Đang trên đường giao...").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(chefid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                        sendNotifications(usertoken, "Đơn hàng được chấp nhận", "Đơn hàng của bạn đã được người giao hàng chấp nhận", "AcceptOrder");
                                                                        ReusableCodeForAll.ShowAlert(context, "", "\n" +
                                                                                "Nhận đơn thành công ! Bây giờ bạn có thể kiểm tra các đơn hàng cần được giao");

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
                                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(chefid).child(randomUID).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(chefid).child(randomUID).child("OtherInformation").removeValue();
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

    private void showOrders(String randomUID) {
        Intent intent = new Intent(context, DeliveryPendingOrderView.class);
        intent.putExtra("Random", randomUID);
        context.startActivity(intent);
    }

    private void sendNotifications(String usertoken, String title, String message, String order) {

        Data data = new Data(title, message, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
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
        return deliveryShipOrders1list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice, mobilenumber,Stt;
        Button viewOrder, Accept, Reject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Stt = itemView.findViewById(R.id.STT);
            Address = itemView.findViewById(R.id.ad1);
            mobilenumber = itemView.findViewById(R.id.MB1);
            grandtotalprice = itemView.findViewById(R.id.TP1);
            viewOrder = itemView.findViewById(R.id.view1);
            Accept = itemView.findViewById(R.id.accept1);
            Reject = itemView.findViewById(R.id.reject1);
        }
    }
}
