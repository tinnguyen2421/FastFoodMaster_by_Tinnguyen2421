package com.example.appfood_by_tinnguyen2421.Chef.ChefActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.FoodSupplyDetails;
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

public class ChefPostDish extends AppCompatActivity {

    ImageButton imageButton;
    Button post_dish;
    TextInputLayout dish, desc, pri, disc;
    TextView dishCountPercent;
    String cateID, description, price, dishes, discount, title;
    Uri imageuri;
    private ArrayList<String> categoryList;
    private SwitchCompat discountSwitch, availableSwitch;
    private boolean Discounting = false;
    private boolean AvailableDish = false;
    Spinner spinner;
    private Uri mCropimageuri;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dataaa;
    FirebaseAuth FAuth;
    StorageReference ref;
    String ChefId;
    String RandomUId;
    String District, City, Ward;
    private double giaGocDouble, giaGiamDouble, tiLeDouble;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_them_moi);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        spinner = findViewById(R.id.spinnerID);
        imageButton = findViewById(R.id.imageupload);
        dish = findViewById(R.id.DishName);
        desc = findViewById(R.id.DishDetail);
        pri = findViewById(R.id.DishPrice);
        disc = findViewById(R.id.DishDiscount);
        dishCountPercent = findViewById(R.id.DishCountPercent);
        post_dish = findViewById(R.id.PostDish);
        discountSwitch = findViewById(R.id.DiscountSwitch);
        availableSwitch = findViewById(R.id.DishAvailable);
        FAuth = FirebaseAuth.getInstance();
        categoryList = new ArrayList<>();
        disc.setVisibility(View.GONE);
        dishCountPercent.setVisibility(View.GONE);
        discountSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                disc.setVisibility(View.VISIBLE);
                dishCountPercent.setVisibility(View.VISIBLE);
            } else {
                disc.setVisibility(View.GONE);
                dishCountPercent.setVisibility(View.GONE);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ChefPostDish.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openFileChooser();
                } else {
                    ActivityCompat.requestPermissions(ChefPostDish.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }
            }
        });

        post_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateID = spinner.getSelectedItem().toString().trim();
                dishes = dish.getEditText().getText().toString().trim();
                description = desc.getEditText().getText().toString().trim();
                price = pri.getEditText().getText().toString().trim();
                discount = disc.getEditText().getText().toString().trim();
                Discounting = discountSwitch.isChecked();
                AvailableDish = availableSwitch.isChecked();
                if (isValid()) {
                    uploadImage();
                }
            }
        });

        try {
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dataaa = FirebaseDatabase.getInstance().getReference("Chef").child(userid);
            dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel chefc = dataSnapshot.getValue(UserModel.class);
                    District = chefc.getDistrict();
                    City = chefc.getCity();
                    Ward = chefc.getWard();
                    showdataspinner();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            Log.e("Errrrrr: ", e.getMessage());
        }
    }

    // Lấy dữ liệu từ RealtimeDb xuống để vào spinner
    public void showdataspinner() {
        RandomUId = getIntent().getStringExtra("RandomUID");
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Chef").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel chefc = dataSnapshot.getValue(UserModel.class);
                District = chefc.getDistrict();
                City = chefc.getCity();
                Ward = chefc.getWard();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Categories").child(City).child(District).child(Ward);
                databaseReference1.child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        categoryList.clear();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            categoryList.add(item.child("CateID").getValue(String.class));
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChefPostDish.this, com.hbb20.R.layout.support_simple_spinner_dropdown_item, categoryList);
                        spinner.setAdapter(arrayAdapter);
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

    private void uploadImage() {
        if (imageuri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(ChefPostDish.this);
            progressDialog.setTitle("Đang tải ảnh...");
            progressDialog.show();
            RandomUId = UUID.randomUUID().toString();
            ref = storageReference.child(RandomUId);
            ChefId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FoodSupplyDetails info = new FoodSupplyDetails(cateID, dishes, price, description, String.valueOf(uri), RandomUId, ChefId, discount, title, Discounting, AvailableDish);
                            FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(City).child(District).child(Ward).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId)
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            Toast.makeText(ChefPostDish.this, "Đăng món thành công", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ChefPostDish.this, ChefDishes.class);
                                            startActivity(intent);
                                        }
                                    });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(ChefPostDish.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Tải lên " + (int) progress + "%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChefPostDish.this);
            alertDialogBuilder.setTitle("Lỗi");
            alertDialogBuilder.setMessage("Hình ảnh không được để trống...");
        }

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
        boolean isValidDis = false, isValidDishname = false, isValiDescription = false, isValidPrice = false, isvalid = false;
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
            isValidDishname = true;
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
            isValiDescription = true;
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
                    giaGocDouble = Double.parseDouble(price);
                } catch (NumberFormatException e) {
                    pri.setErrorEnabled(true);
                    pri.setError("Giá không hợp lệ");
                }
            }
        }
        if (Discounting) {
            if (TextUtils.isEmpty(price)) {
                disc.setErrorEnabled(true);
                disc.setError("Bắt buộc phải nhập giá món ăn trước khi nhập giảm giá");
            } else if (!TextUtils.isEmpty(price) && TextUtils.isEmpty(discount)) {
                disc.setErrorEnabled(true);
                disc.setError("Giảm giá không được để trống");
            } else if (!TextUtils.isEmpty(price) && !TextUtils.isEmpty(discount) && giaGiamDouble >= giaGocDouble) {

                new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                        .setTitle("Lỗi")
                        .setMessage("Giảm giá phải nhỏ hơn giá gốc của món ăn")
                        .setPositiveButton("OK", null).show();
            } else {
                giaGiamDouble = Double.parseDouble(discount);
                tiLeDouble = (giaGocDouble - giaGiamDouble) / giaGocDouble * 100;
                int lamTron = (int) Math.round(tiLeDouble);
                title = String.valueOf(lamTron);
                dishCountPercent.setText("Giảm " + lamTron + "%");
                isValidDis = true;
            }
        } else {
            isValidDis = true;
            discount = "0";
            title = "";
        }
        isvalid = (isValidDis && isValiDescription && isValidDishname && isValidPrice) ? true : false;
        return isvalid;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            Log.d("Checkkkkk", "onActivityResult: "+imageuri);
            imageButton.setImageURI(imageuri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
