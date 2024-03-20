package com.example.appfood_by_tinnguyen2421.Chef.ChefActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter.ChefOrdertobePrepareViewAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefOrdertobePrepareView extends AppCompatActivity {

    RecyclerView recyclerViewdish;
    private List<ChefFinalOrders> chefWaitingOrdersList;
    private ChefOrdertobePrepareViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID, userid;
    TextView grandtotal, note, address, name, number;
    LinearLayout l1;
    Button Preparing;
    private ProgressDialog progressDialog;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_ordertobe_prepare_view);
        recyclerViewdish = findViewById(R.id.Recycle_viewOrder);
        grandtotal = findViewById(R.id.rupees);
        note = findViewById(R.id.NOTE);
        address = findViewById(R.id.ad);
        name = findViewById(R.id.nm);
        number = findViewById(R.id.num);
        l1 = findViewById(R.id.button1);
        Preparing = findViewById(R.id.preparing);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        progressDialog = new ProgressDialog(ChefOrdertobePrepareView.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(ChefOrdertobePrepareView.this));
        chefWaitingOrdersList = new ArrayList<>();
        CheforderdishesView();
    }
    private void CheforderdishesView() {
        RandomUID = getIntent().getStringExtra("RandomUID");
        reference = FirebaseDatabase.getInstance().getReference("ChefWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chefWaitingOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChefFinalOrders chefWaitingOrders = snapshot.getValue(ChefFinalOrders.class);
                    chefWaitingOrdersList.add(chefWaitingOrders);
                }
                if (chefWaitingOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);

                } else {
                    l1.setVisibility(View.VISIBLE);
                    Preparing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressDialog.setMessage("Vui lòng đợi...");
                            progressDialog.show();

                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("ChefWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        final ChefFinalOrders chefWaitingOrders = dataSnapshot1.getValue(ChefFinalOrders.class);
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        String dishid = chefWaitingOrders.getDishID();
                                        userid = chefWaitingOrders.getUserID();
                                        String chefID=chefWaitingOrders.getChefID();
                                        hashMap.put("ChefID", chefWaitingOrders.getChefID());
                                        hashMap.put("DishID", chefWaitingOrders.getDishID());
                                        hashMap.put("DishName", chefWaitingOrders.getDishName());
                                        hashMap.put("DishPrice", chefWaitingOrders.getDishPrice());
                                        hashMap.put("DishQuantity", chefWaitingOrders.getDishQuantity());
                                        hashMap.put("RandomUID", chefWaitingOrders.getRandomUID());
                                        hashMap.put("TotalPrice", chefWaitingOrders.getTotalPrice());
                                        hashMap.put("UserID", chefWaitingOrders.getUserID());
                                        hashMap.put("ImageURL",chefWaitingOrders.getImageURL());
                                        FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes").child(dishid).setValue(hashMap);
                                    }
                                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("ChefWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            final ChefFinalOrders1 chefFinalOrders1 = dataSnapshot.getValue(ChefFinalOrders1.class);
                                            HashMap<String, String> hashMap1 = new HashMap<>();
                                            hashMap1.put("Address", chefFinalOrders1.getAddress());
                                            hashMap1.put("GrandTotalPrice", chefFinalOrders1.getGrandTotalPrice());
                                            hashMap1.put("MobileNumber", chefFinalOrders1.getMobileNumber());
                                            hashMap1.put("Name", chefFinalOrders1.getName());
                                            hashMap1.put("RandomUID", RandomUID);
                                            hashMap1.put("AceptDate", chefFinalOrders1.getAceptDate());
                                            FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(userid).child(RandomUID).child("OtherInformation").child("OrderStatus").setValue("Đã chuẩn bị...").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    FirebaseDatabase.getInstance().getReference("ChefWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            FirebaseDatabase.getInstance().getReference("ChefWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {

                                                                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                            String usertoken = dataSnapshot.getValue(String.class);
                                                                                            sendNotifications(usertoken, "Estimated Time", "Cửa hàng đang chuẩn bị đơn hàng của bạn", "Preparing");
                                                                                            progressDialog.dismiss();
                                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(ChefOrdertobePrepareView.this);
                                                                                            builder.setMessage("Xong ! Xem tiếp những đơn hàng cần chuẩn bị");
                                                                                            builder.setCancelable(false);
                                                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                                    dialog.dismiss();
                                                                                                    //Intent b = new Intent(ChefOrdertobePrepareView.this, ChefOrderTobePreparedFragment.class);
                                                                                                    //startActivity(b);
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
                adapter = new ChefOrdertobePrepareViewAdapter(ChefOrdertobePrepareView.this, chefWaitingOrdersList);
                recyclerViewdish.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChefWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChefFinalOrders1 chefFinalOrders1 = dataSnapshot.getValue(ChefFinalOrders1.class);
                grandtotal.setText(  chefFinalOrders1.getGrandTotalPrice()+"đ");
                note.setText(chefFinalOrders1.getNote());
                address.setText(chefFinalOrders1.getAddress());
                name.setText(chefFinalOrders1.getName());
                number.setText("Số điện thoại" + chefFinalOrders1.getMobileNumber());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifications(String usertoken, String title, String message, String preparing) {
        Data data = new Data(title, message, preparing);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(ChefOrdertobePrepareView.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
