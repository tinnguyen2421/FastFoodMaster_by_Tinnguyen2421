package com.example.appfood_by_tinnguyen2421.Chef.ChefActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.bumptech.glide.Glide;
import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.BottomNavigation.ChefBottomNavigation;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.FoodSupplyDetails;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class ChefUpdateDish extends AppCompatActivity {
    private ArrayList<String> categoryList;

    private Spinner spinnerCate;
    private TextInputLayout desc, pri, dish, disc;
    private ImageButton imageButton;
    private Uri imageUri;
    private String dbUri;
    private boolean isDiscounting = false;
    private boolean isAvailable = false;
    private Button btnUpdate, btnDelete;
    private String cateID, description, price, dishes, chefId, discount, discountPercent, title;
    private TextView dishCountPercent;
    private String randomUid;
    private StorageReference ref;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String ID;
    private ProgressDialog progressDialog;
    private DatabaseReference dataaa;
    private SwitchCompat discountSwitch, availableSwitch;
    private String district, city, ward;
    private double originalPrice, discountedPrice, discountRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__delete__dish);
        initializeViews();
        setUpListeners();
        initializeData();
    }

    private void initializeViews() {
        spinnerCate = findViewById(R.id.spinnerID);
        dish = findViewById(R.id.DishName);
        pri = findViewById(R.id.DishPrice);
        desc = findViewById(R.id.DishDetail);
        imageButton = findViewById(R.id.imageupload);
        disc = findViewById(R.id.DishDiscount);
        dishCountPercent = findViewById(R.id.DishCountPercent);
        discountSwitch = findViewById(R.id.DiscountSwitch);
        availableSwitch = findViewById(R.id.DishAvailable);
        btnUpdate = findViewById(R.id.btnUpdateDish);
        btnDelete = findViewById(R.id.btnDeleteDish);
    }

    private void setUpListeners() {
        btnUpdate.setOnClickListener(v -> updateDishClicked());
        btnDelete.setOnClickListener(v -> deleteDishClicked());
        imageButton.setOnClickListener(this::onSelectImageClick);
    }

    private void initializeData() {
        ID = getIntent().getStringExtra("updatedeletedish");
        categoryList = new ArrayList<>();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Chef").child(userId);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel chef = dataSnapshot.getValue(UserModel.class);
                district = chef.getDistrict();
                city = chef.getCity();
                ward = chef.getWard();
                showDataSpinner();
                loadDishInformation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDataSpinner() {
        randomUid = getIntent().getStringExtra("RandomUID");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Chef").child(userId);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel chef = dataSnapshot.getValue(UserModel.class);
                district = chef.getDistrict();
                city = chef.getCity();
                ward = chef.getWard();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                        .getReference("Categories").child(city).child(district).child(ward);
                databaseReference1.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        categoryList.clear();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            categoryList.add(item.child("CateID").getValue(String.class));
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChefUpdateDish.this,
                                com.hbb20.R.layout.support_simple_spinner_dropdown_item, categoryList);
                        spinnerCate.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadDishInformation() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        progressDialog = new ProgressDialog(ChefUpdateDish.this);
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails")
                .child(city).child(district).child(ward).child(userId).child(ID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                dish.getEditText().setText(updateDishModel.getDishName());
                pri.getEditText().setText(updateDishModel.getDishPrice());
                Glide.with(ChefUpdateDish.this).load(updateDishModel.getImageURL()).into(imageButton);
                dbUri = updateDishModel.getImageURL();
                desc.getEditText().setText(updateDishModel.getDescription());
                discountSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b) {
                        disc.setVisibility(View.VISIBLE);
                        dishCountPercent.setVisibility(View.VISIBLE);
                    } else {
                        disc.setVisibility(View.GONE);
                        dishCountPercent.setVisibility(View.GONE);
                    }
                });
                if (updateDishModel.getAvailableDish().equals("true")) {
                    availableSwitch.setChecked(true);
                }
                if (updateDishModel.getOnSale().equals("true")) {
                    discountSwitch.setChecked(true);
                    disc.getEditText().setText(updateDishModel.getReducePrice());
                    dishCountPercent.setText("Giảm " + updateDishModel.getDecreasePercent() + "%");
                } else {
                    disc.setVisibility(View.GONE);
                    dishCountPercent.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateDishClicked() {
        cateID = spinnerCate.getSelectedItem().toString().trim();
        dishes = dish.getEditText().getText().toString().trim();
        description = desc.getEditText().getText().toString().trim();
        price = pri.getEditText().getText().toString().trim();
        discount = disc.getEditText().getText().toString().trim();
        isDiscounting = discountSwitch.isChecked();
        isAvailable = availableSwitch.isChecked();
        if (isValid()) {
            if (imageUri != null) {
                uploadImage();
            } else {
                updateDesc(dbUri);
            }
        }
    }

    private void deleteDishClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChefUpdateDish.this);
        builder.setMessage("Bạn có chắc chắn muốn xóa món ăn này");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference("FoodSupplyDetails")
                        .child(district).child(city).child(ward)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(ID).removeValue();

                AlertDialog.Builder food = new AlertDialog.Builder(ChefUpdateDish.this);
                food.setMessage("Món ăn của bạn đã được xóa");
                food.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ChefUpdateDish.this, ChefBottomNavigation.class));
                    }
                });
                AlertDialog alert = food.create();
                alert.show();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isValid() {
        dish.setError("");
        dish.setErrorEnabled(false);
        desc.setError("");
        desc.setErrorEnabled(false);
        pri.setErrorEnabled(false);
        pri.setError("");
        disc.setErrorEnabled(false);
        disc.setError("");
        boolean isValidDis = false, isValidDishName = false, isValidDescription = false, isValidPrice = false, isValidInput = false;
        if (TextUtils.isEmpty(dishes)) {
            dish.setErrorEnabled(true);
            dish.setError("Tên món ăn không được để trống");
        } else if (dishes.length() > 20) {
            dish.setErrorEnabled(true);
            dish.setError("Tên món ăn không quá 20 kí tự");
        } else if (dishes.length() < 3) {
            dish.setErrorEnabled(true);
            dish.setError("Tên món ăn không ít hơn 3 kí tự");
        } else {
            dish.setError(null);
            isValidDishName = true;
        }

        if (TextUtils.isEmpty(description)) {
            desc.setErrorEnabled(true);
            desc.setError("Chi tiết món ăn không được để trống");
        } else if (description.length() > 20) {
            desc.setErrorEnabled(true);
            desc.setError("Chi tiết món ăn không được quá 20 kí tự");
        } else if (description.length() < 3) {
            desc.setErrorEnabled(true);
            desc.setError("Chi tiết món ăn không được ít hơn 3 kí tự");
        } else {
            desc.setError(null);
            isValidDescription = true;
        }

        if (TextUtils.isEmpty(price)) {
            pri.setErrorEnabled(true);
            pri.setError("Giá không được để trống");
        } else {
            double priceValue = Double.parseDouble(price);
            if (priceValue > 1000000) {
                pri.setErrorEnabled(true);
                pri.setError("Giá món ăn không được lớn hơn 1.000.000 vnđ");
            } else if (priceValue < 1000) {
                pri.setErrorEnabled(true);
                pri.setError("Giá món ăn không được nhỏ hơn 1.000 vnđ");
            } else {
                isValidPrice = true;
                try {
                    originalPrice = Double.parseDouble(price);
                } catch (NumberFormatException e) {
                    pri.setErrorEnabled(true);
                    pri.setError("Giá không hợp lệ");
                }
            }
        }
        if (isDiscounting) {
            if (TextUtils.isEmpty(price)) {
                disc.setErrorEnabled(true);
                disc.setError("Bắt buộc phải nhập giá món ăn trước khi nhập giá giảm");
            } else if (!TextUtils.isEmpty(price) && TextUtils.isEmpty(discount)) {
                disc.setErrorEnabled(true);
                disc.setError("Giá giảm không được để trống");
            } else if (!TextUtils.isEmpty(price) && !TextUtils.isEmpty(discount) && discountedPrice >= originalPrice) {
                new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                        .setTitle("Lỗi")
                        .setMessage("Giá giảm phải nhỏ hơn giá gốc của món ăn")
                        .setPositiveButton("OK", null).show();
            } else {
                discountedPrice = Double.parseDouble(discount);
                discountRate = (originalPrice - discountedPrice) / originalPrice * 100;
                int roundedRate = (int) Math.round(discountRate);
                title = String.valueOf(roundedRate);
                dishCountPercent.setText("Giảm " + roundedRate + "%");
                isValidDis = true;
            }
        } else {
            isValidDis = true;
            discount = "0";
            title = "";
        }
        isValidInput = (isValidDis && isValidDescription && isValidDishName && isValidPrice);
        return isValidInput;
    }

    private void uploadImage() {
        if (imageUri != null) {
            progressDialog.setTitle("Đang tải...");
            progressDialog.show();
            randomUid = UUID.randomUUID().toString();
            ref = storageReference.child(randomUid);
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            updateDesc(String.valueOf(uri));
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ChefUpdateDish.this, "Thất bại : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("tải lên " + (int) progress + "%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }
    }

    private void updateDesc(String uri) {
        chefId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FoodSupplyDetails info = new FoodSupplyDetails(cateID, dishes, price, description, uri, ID, chefId, discount, title, isDiscounting, isAvailable);
        FirebaseDatabase.getInstance().getReference("FoodSupplyDetails")
                .child(city).child(district).child(ward)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID)
                .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(ChefUpdateDish.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onSelectImageClick(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle activity result for selecting image from gallery
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imageButton);
        }
    }

    private static final int PICK_IMAGE_REQUEST = 1;
}
