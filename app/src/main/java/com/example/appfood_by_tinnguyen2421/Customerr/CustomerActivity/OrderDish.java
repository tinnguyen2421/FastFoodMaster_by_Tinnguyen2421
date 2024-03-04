package com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.BottomNavigation.CustomerBottomNavigation;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.CustomerDishesAdapter;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.Cart;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OrderDish extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    String RandomId, ChefID;
    ImageView imageView;
    TextView titlePercent;
    ElegantNumberButton additem;
    RecyclerView rv_reviews;
    TextView Foodname, ChefName, ChefLoaction, FoodQuantity, FoodPrice, FoodDescription;
    DatabaseReference databaseReference, dataaa, chefdata, reference, data, dataref;
    String District, City, Ward, dishname;
    //ex
    SwipeRefreshLayout swipeRefreshLayout;
    private CustomerDishesAdapter adapter;
    private List<UpdateDishModel> updateDishModelList;
    //ex
    int dishprice;
    String custID,Matl,TenMon;

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chitietfood);
        //exra
        rv_reviews = findViewById(R.id.rv_reviews);
        rv_reviews.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv_reviews.setLayoutManager(layoutManager);
        updateDishModelList = new ArrayList<>();
        Intent dataget=getIntent();
        Matl = dataget.getStringExtra("CateID");
        TenMon = dataget.getStringExtra("TenMon");
        //extra
        Foodname = (TextView) findViewById(R.id.food_name);
        ChefName = (TextView) findViewById(R.id.chef_name);
        ChefLoaction = (TextView) findViewById(R.id.chef_location);
        FoodQuantity = (TextView) findViewById(R.id.food_quantity);
        FoodPrice = (TextView) findViewById(R.id.food_price);
        FoodDescription = (TextView) findViewById(R.id.food_description);
        imageView = (ImageView) findViewById(R.id.image);
        titlePercent=findViewById(R.id.Title);
        additem = (ElegantNumberButton) findViewById(R.id.number_btn);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                dataaa = FirebaseDatabase.getInstance().getReference("UserModel").child(userid);
                dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserModel cust = dataSnapshot.getValue(UserModel.class);
                        District = cust.getDistrict();
                        City = cust.getCity();
                        Ward = cust.getWard();
                        customermenu();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
        loadDishInfoAndHandleItem();
    }
    public void loadDishInfoAndHandleItem()

    {
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("UserModel").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel cust = dataSnapshot.getValue(UserModel.class);
                District = cust.getDistrict();
                City = cust.getCity();
                Ward = cust.getWard();
                RandomId = getIntent().getStringExtra("FoodMenu");
                ChefID = getIntent().getStringExtra("ChefId");
                databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(City).child(District).child(Ward).child(ChefID).child(RandomId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                        Foodname.setText( "Tên món: " + updateDishModel.getDishName());
                        String ss = "<b>" + "Chi tiết: " + "</b>" + updateDishModel.getDescription();
                        FoodDescription.setText(Html.fromHtml(ss));
                        titlePercent.setText("Giảm "+updateDishModel.getDecreasePercent()+"%");
                        //
                        double price=Double.parseDouble(updateDishModel.getDishPrice());
                        DecimalFormat decimalFormat=new DecimalFormat("#,###,###,###");
                        String FormatPrice=decimalFormat.format(price);
                        //
                        double priceRededuce=Double.parseDouble(updateDishModel.getReducePrice());
                        DecimalFormat decimalFormatPriceReduce=new DecimalFormat("#,###,###,###");
                        String FormatPriceReduce=decimalFormatPriceReduce.format(priceRededuce);
                        if(updateDishModel.getOnSale().equals("true"))
                        {
                            String pri = "<b>" + "Giá:" + "</b>" + FormatPriceReduce + "đ";
                            FoodPrice.setText(Html.fromHtml(pri));
                        }
                        else
                        {
                            String pri = "<b>" + "Giá:" + "</b>" + FormatPrice + "đ";
                            FoodPrice.setText(Html.fromHtml(pri));
                        }
                        Glide.with(OrderDish.this).load(updateDishModel.getImageURL()).into(imageView);
                        chefdata = FirebaseDatabase.getInstance().getReference("Chef").child(ChefID);
                        chefdata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserModel chef = dataSnapshot.getValue(UserModel.class);
                                String name = "<b>" + "Tên cửa hàng: " + "</b>" + chef.getFirstName() + " " + chef.getLastName();
                                ChefName.setText(Html.fromHtml(name));
                                String loc = "<b>" + "Địa chỉ: " + "</b>" + chef.getWard();
                                ChefLoaction.setText(Html.fromHtml(loc));
                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Cart cart = dataSnapshot.getValue(Cart.class);
                                        if (dataSnapshot.exists()) {
                                            additem.setNumber(cart.getDishQuantity());
                                        }
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
                additem.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dataref = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Cart cart1 = null;
                                if (dataSnapshot.exists()) {
                                    int totalcount = 0;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        totalcount++;
                                    }
                                    int i = 0;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        i++;
                                        if (i == totalcount) {
                                            cart1 = snapshot.getValue(Cart.class);
                                        }
                                    }

                                    if (ChefID.equals(cart1.getChefID())) {
                                        data = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(District).child(City).child(Ward).child(ChefID).child(RandomId);
                                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                                                if(updateDishModel.getOnSale().equals("true"))
                                                {
                                                    dishprice = Integer.parseInt(updateDishModel.getReducePrice());
                                                }
                                                else
                                                {
                                                    dishprice = Integer.parseInt(updateDishModel.getDishPrice());
                                                }
                                                dishname = updateDishModel.getDishName();
                                                int num = Integer.parseInt(additem.getNumber());
                                                int totalprice = num * dishprice;
                                                if (num != 0) {
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("DishName", dishname);
                                                    hashMap.put("DishID", RandomId);
                                                    hashMap.put("DishQuantity", String.valueOf(num));
                                                    hashMap.put("DishPrice", String.valueOf(dishprice));
                                                    hashMap.put("TotalPrice", String.valueOf(totalprice));
                                                    hashMap.put("ChefID", ChefID);
                                                    hashMap.put("ImageURL",updateDishModel.getImageURL());
                                                    custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                                    reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            Toast.makeText(OrderDish.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                } else {

                                                    firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDish.this);
                                        builder.setMessage("Bạn không thể thêm các món ăn của nhiều quán ăn cùng một lúc, hãy thêm các món ăn của cùng 1 quán");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                                Intent intent = new Intent(OrderDish.this, CustomerBottomNavigation.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                } else {
                                    data = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(District).child(City).child(Ward).child(ChefID).child(RandomId);
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                                            if(updateDishModel.getOnSale().equals("true"))
                                            {
                                                dishprice = Integer.parseInt(updateDishModel.getReducePrice());
                                            }
                                            else
                                            {
                                                dishprice = Integer.parseInt(updateDishModel.getDishPrice());
                                            }
                                            dishname = updateDishModel.getDishName();
                                            int num = Integer.parseInt(additem.getNumber());
                                            int totalprice = num * dishprice;
                                            if (num != 0) {
                                                HashMap<String, String> hashMap = new HashMap<>();
                                                hashMap.put("DishName", dishname);
                                                hashMap.put("DishID", RandomId);
                                                hashMap.put("DishQuantity", String.valueOf(num));
                                                hashMap.put("DishPrice", String.valueOf(dishprice));
                                                hashMap.put("TotalPrice", String.valueOf(totalprice));
                                                hashMap.put("ChefID", ChefID);
                                                hashMap.put("ImageURL",updateDishModel.getImageURL());
                                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                                reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(OrderDish.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            } else {

                                                firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void customermenu() {

        swipeRefreshLayout.setRefreshing(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(District).child(City).child(Ward);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateDishModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        UpdateDishModel updateDishModel = snapshot1.getValue(UpdateDishModel.class);
                        if(Matl.equals(updateDishModel.getCateID())&&!TenMon.equals(updateDishModel.getDishName())) {
                            updateDishModelList.add(updateDishModel);
                        }
                    }
                }
                adapter = new CustomerDishesAdapter(OrderDish.this, updateDishModelList);
                rv_reviews.setAdapter(adapter);
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
        customermenu();
    }
}



