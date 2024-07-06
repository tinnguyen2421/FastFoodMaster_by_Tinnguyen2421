package com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.appbar.CollapsingToolbarLayout;
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

    private String RandomId, ChefID;
    private ImageView imageView,imageViewCard;
    private TextView titlePercent, Foodname, ChefName, ChefLocation, FoodQuantity, FoodPrice, FoodDescription,outOfDishes, shopStatus,availableDish,shopStatuss;
    private ElegantNumberButton additem;
    private RecyclerView rvReviews;
    CollapsingToolbarLayout collapsingg;
    private DatabaseReference databaseReference, dataaa, chefData, reference, data, dataRef;
    private String District, City, Ward, dishName;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomerDishesAdapter adapter;
    private List<UpdateDishModel> updateDishModelList;
    private int dishPrice;
    private ImageView Iv_backdrop;
    private String custID, cateID, dishes;
    private LinearLayout layoutNumberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietfood);

        // Initialize views
        initializeViews();

        // Set up SwipeRefreshLayout
        setUpSwipeRefreshLayout();

        // Load dish information and handle item
        loadDishInfoAndHandleItem();
    }

    private void initializeViews() {
        // Initialize views
        Foodname = findViewById(R.id.food_name);
        ChefName = findViewById(R.id.chef_name);
        ChefLocation = findViewById(R.id.chef_location);
        FoodQuantity = findViewById(R.id.food_quantity);
        FoodPrice = findViewById(R.id.food_price);
        FoodDescription = findViewById(R.id.food_description);
        imageView = findViewById(R.id.image);
        titlePercent = findViewById(R.id.DecreasePercent);
        additem = findViewById(R.id.number_btn);
        layoutNumberButton=findViewById(R.id.LayoutNumberButton);
        outOfDishes=findViewById(R.id.OutOfDishes);
        shopStatus =findViewById(R.id.ShopClosed);
        availableDish=findViewById(R.id.DishAvailable);
        shopStatuss=findViewById(R.id.ShopStatuss);
        imageViewCard=findViewById(R.id.image);
        Iv_backdrop=findViewById(R.id.iv_backdrop);
        rvReviews = findViewById(R.id.rv_reviews);
        collapsingg=findViewById(R.id.collapsing);
        rvReviews.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvReviews.setLayoutManager(layoutManager);
        updateDishModelList = new ArrayList<>();
        Intent dataget=getIntent();
        cateID = dataget.getStringExtra("CateID");
        dishes = dataget.getStringExtra("TenMon");
    }

    private void setUpSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
    }

    private void loadDishInfoAndHandleItem() {
        loadUserLocationInfo();
        loadDishInfoAndHandleItem1();
    }

    private void loadUserLocationInfo() {
        swipeRefreshLayout.setRefreshing(true);
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel cust = dataSnapshot.getValue(UserModel.class);
                District = cust.getDistrict();
                City = cust.getCity();
                Ward = cust.getWard();
                loadDishes();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadDishInfoAndHandleItem1() {
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
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
                        // Handle dish information
                        handleDishInfo(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                handleAddItemButtonClick();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void handleDishInfo(DataSnapshot dataSnapshot) {
        UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
        if (updateDishModel != null) {
            setDishPrice(updateDishModel);
            loadChefInfo(updateDishModel);
            loadCartItemQuantity(updateDishModel);
        }
    }

    private void setDishPrice(UpdateDishModel updateDishModel) {
        double price = Double.parseDouble(updateDishModel.getDishPrice());
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
        String formattedPrice = decimalFormat.format(price);

        double reducePrice = Double.parseDouble(updateDishModel.getReducePrice());
        DecimalFormat decimalFormatPriceReduce = new DecimalFormat("#,###,###,###");
        String formattedPriceReduce = decimalFormatPriceReduce.format(reducePrice);

        collapsingg.setTitle(updateDishModel.getDishName());
        // Set dish name
        Foodname.setText("Tên món: " + updateDishModel.getDishName());
        // Set dish description
        String descriptionHtml = "<b>" + "Chi tiết: " + "</b>" + updateDishModel.getDescription();
        FoodDescription.setText(Html.fromHtml(descriptionHtml));

        Glide.with(OrderDish.this).load(updateDishModel.getImageURL()).into(imageView);
        Glide.with(OrderDish.this).load(updateDishModel.getImageURL()).into(Iv_backdrop);
        databaseReference=FirebaseDatabase.getInstance().getReference("ChefStatus").child(updateDishModel.getChefID()+"/Status");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                if(status.equals("Mở cửa")) {
                    if (updateDishModel.getOnSale().equals("true")) {
                        String priceHtml = "<b>" + "Giá:" + "</b>" + formattedPriceReduce + "đ";
                        FoodPrice.setText(Html.fromHtml(priceHtml));
                        titlePercent.setText("Giảm "+updateDishModel.getDecreasePercent()+"%");
                        if (updateDishModel.getAvailableDish().equals("true")) {
                        } else {
                            additem.setVisibility(View.GONE);
                            imageViewCard.setAlpha((float) 0.3);
                            outOfDishes.setVisibility(View.VISIBLE);
                            availableDish.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        String priceHtml = "<b>" + "Giá:" + "</b>" + formattedPrice + "đ";
                        FoodPrice.setText(Html.fromHtml(priceHtml));
                        titlePercent.setVisibility(View.GONE);
                    }
                }
                else
                {
                    shopStatuss.setVisibility(View.VISIBLE);
                    shopStatus.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadChefInfo(UpdateDishModel updateDishModel) {
        chefData = FirebaseDatabase.getInstance().getReference("Chef").child(ChefID);
        chefData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel chef = dataSnapshot.getValue(UserModel.class);
                if (chef != null) {
                    String nameHtml = "<b>" + "Tên cửa hàng: " + "</b>" + chef.getFirstName() + " " + chef.getLastName();
                    ChefName.setText(Html.fromHtml(nameHtml));
                    String locHtml = "<b>" + "Địa chỉ: " + "</b>" + chef.getWard();
                    ChefLocation.setText(Html.fromHtml(locHtml));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void loadCartItemQuantity(UpdateDishModel updateDishModel) {
        custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Cart cart = dataSnapshot.getValue(Cart.class);
                if (cart != null && dataSnapshot.exists()) {
                    additem.setNumber(cart.getDishQuantity());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void handleAddItemButtonClick() {
        additem.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle add item button click
                handleAddItemButtonClick1();
            }
        });
    }

    private void handleAddItemButtonClick1() {
        dataRef = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if cart exists
                if (dataSnapshot.exists()) {
                    int totalCount = (int) dataSnapshot.getChildrenCount();
                    int i = 0;
                    Cart cart1 = null;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        i++;
                        if (i == totalCount) {
                            cart1 = snapshot.getValue(Cart.class);
                        }
                    }

                    // Check if chef ID matches
                    if (ChefID.equals(cart1.getChefID())) {
                        // Load dish details and calculate total price
                        loadDishDetailsAndCalculateTotalPrice();
                    } else {
                        showChefMismatchDialog();
                    }
                } else {
                    // Load dish details and calculate total price
                    loadDishDetailsAndCalculateTotalPrice();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void loadDishDetailsAndCalculateTotalPrice() {
        data = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(City).child(District).child(Ward).child(ChefID).child(RandomId);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                if (updateDishModel != null) {
                    if (updateDishModel.getOnSale().equals("true")) {
                        dishPrice = Integer.parseInt(updateDishModel.getReducePrice());
                    } else {
                        dishPrice = Integer.parseInt(updateDishModel.getDishPrice());
                    }
                    dishName = updateDishModel.getDishName();
                    int num = Integer.parseInt(additem.getNumber());
                    int totalPrice = num * dishPrice;
                    if (num != 0) {
                        addToCart(num, totalPrice, updateDishModel);
                    } else {
                        removeCartItem();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void addToCart(int num, int totalPrice, UpdateDishModel updateDishModel) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("DishName", dishName);
        hashMap.put("DishID", RandomId);
        hashMap.put("DishQuantity", String.valueOf(num));
        hashMap.put("DishPrice", String.valueOf(dishPrice));
        hashMap.put("TotalPrice", String.valueOf(totalPrice));
        hashMap.put("ChefID", ChefID);
        hashMap.put("ImageURL", updateDishModel.getImageURL());
        custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
        reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(OrderDish.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeCartItem() {
        FirebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
    }

    private void showChefMismatchDialog() {
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

    private void loadDishes() {
        swipeRefreshLayout.setRefreshing(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(City).child(District).child(Ward);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateDishModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        UpdateDishModel updateDishModel = snapshot1.getValue(UpdateDishModel.class);
                        if (updateDishModel != null && cateID.equals(updateDishModel.getCateID()) && !dishes.equals(updateDishModel.getDishName())) {
                            updateDishModelList.add(updateDishModel);
                        }
                    }
                }
                adapter = new CustomerDishesAdapter(OrderDish.this, updateDishModelList);
                rvReviews.setAdapter(adapter);
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
        loadDishes();
    }
}
