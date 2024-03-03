package com.example.appfood_by_tinnguyen2421.Chef.ChefActivity;

import static java.lang.String.*;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.bumptech.glide.Glide;
import com.example.appfood_by_tinnguyen2421.BottomNavigation.ChefBottomNavigation;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.Chef;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.UUID;

//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefUpdateDish extends AppCompatActivity {
    private ArrayList<String> categoryList;

    Spinner spinnerCate;
    TextInputLayout desc, pri, dish,disc;
    private SwitchCompat discountSwitch,availableSwitch;
    ImageButton imageButton;
    Uri imageuri;
    String dburi;
    private Uri mCropimageuri;
    private boolean Discounting=false;
    private boolean Available=false;
    Button Update_dish, Delete_dish;
    String cateID,description, price, dishes, ChefId,discount,discountPercent,title;
    TextView dishCountPercent;
    String RandomUId;
    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth FAuth;
    String ID;
    private ProgressDialog progressDialog;
    DatabaseReference dataaa;
    String State, City, Sub;
    private double giaGocDouble, giaGiamDouble, tiLeDouble;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__delete__dish);
        spinnerCate = (Spinner) findViewById(R.id.spinnerID);
        dish = (TextInputLayout) findViewById(R.id.DishName);
        pri = (TextInputLayout) findViewById(R.id.DishPrice);
        desc = (TextInputLayout) findViewById(R.id.DishDetail);
        imageButton = (ImageButton) findViewById(R.id.imageupload);
        disc=(TextInputLayout)findViewById(R.id.DishDiscount);
        dishCountPercent= (TextView)findViewById(R.id.DishCountPercent);
        discountSwitch=(SwitchCompat)findViewById(R.id.DiscountSwitch);
        availableSwitch=(SwitchCompat)findViewById(R.id.DishAvailable);
        Update_dish = (Button) findViewById(R.id.btnUpdateDish);
        Delete_dish = (Button) findViewById(R.id.btnDeleteDish);
        ID = getIntent().getStringExtra("updatedeletedish");
        EditText dishNameEditText = dish.getEditText();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        categoryList=new ArrayList<>();
        dataaa = firebaseDatabase.getInstance().getReference("Chef").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Chef chefc = dataSnapshot.getValue(Chef.class);
                State = chefc.getDistrict();
                City = chefc.getCity();
                Sub = chefc.getWard();
                showdataspinner();
                loadDishInformation();
                Update_dish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        cateID= spinnerCate.getSelectedItem().toString().trim();
                        dishes= dish.getEditText().getText().toString().trim();
                        description = desc.getEditText().getText().toString().trim();
                        price = pri.getEditText().getText().toString().trim();
                        discount = disc.getEditText().getText().toString().trim();
                        Discounting=discountSwitch.isChecked();
                        Available=availableSwitch.isChecked();
                        if (isValid()) {
                            if (imageuri != null) {
                                uploadImage();
                            }
                            else {
                                updatedesc(dburi);
                            }
                        }
                    }
                });
                Delete_dish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChefUpdateDish.this);
                        builder.setMessage("Bạn có chắc chắn muốn xóa món ăn này");
                        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                firebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(State).child(City).child(Sub).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID).removeValue();

                                AlertDialog.Builder food = new AlertDialog.Builder(ChefUpdateDish.this);
                                food.setMessage("Món ăn của bạn đã được xóa");
                                food.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        startActivity(new Intent(ChefUpdateDish.this, ChefBottomNavigation.class));
                                    }
                                });
                                AlertDialog alertt = food.create();
                                alertt.show();
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
                });
                FAuth = FirebaseAuth.getInstance();
                databaseReference = firebaseDatabase.getInstance().getReference("FoodSupplyDetails");
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSelectImageClick(v);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void showdataspinner()
    {
        RandomUId = getIntent().getStringExtra("RandomUID");
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = firebaseDatabase.getInstance().getReference("Chef").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Chef chefc = dataSnapshot.getValue(Chef.class);
                State = chefc.getDistrict();
                City = chefc.getCity();
                Sub = chefc.getWard();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Categories").child(State).child(City).child(Sub);
                databaseReference1.child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        categoryList.clear();
                        for (DataSnapshot item: snapshot.getChildren())
                        {
                            categoryList.add(item.child("CateID").getValue(String.class));
                        }
                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(ChefUpdateDish.this, com.hbb20.R.layout.support_simple_spinner_dropdown_item,categoryList);
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
    public void loadDishInformation(){
        String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
        progressDialog = new ProgressDialog(ChefUpdateDish.this);
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(State).child(City).child(Sub).child(useridd).child(ID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                dish.getEditText().setText( updateDishModel.getDishName());
                pri.getEditText().setText(updateDishModel.getDishPrice());
                Glide.with(ChefUpdateDish.this).load(updateDishModel.getImageURL()).into(imageButton);
                dburi = updateDishModel.getImageURL();
                desc.getEditText().setText(updateDishModel.getDescription());
                if(updateDishModel.getAvailableDish().equals("true"))
                {
                    availableSwitch.setChecked(true);
                }
                if(updateDishModel.getOnSale().equals("true"))
                {
                    discountSwitch.setChecked(true);
                    disc.getEditText().setText(updateDishModel.getReducePrice());
                    dishCountPercent.setText("Giảm "+updateDishModel.getDecreasePercent()+"%");
                }
                else
                {
                    disc.setVisibility(View.GONE);
                    dishCountPercent.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        boolean isValidDis=false, isValidDishname=false, isValiDescription = false, isValidPrice = false, isvalid = false;
        if (TextUtils.isEmpty(dishes)) {
            dish.setErrorEnabled(true);
            dish.setError("Tên món ăn không được để trống");
        } else if (dishes.length()>20) {
            dish.setErrorEnabled(true);
            dish.setError("Tên món ăn không quá 20 kí tự");
        }
        else if (dishes.length()<3) {
            dish.setErrorEnabled(true);
            dish.setError("Tên món ăn không ít hơn 3 kí tự");
        }
        else {
            dish.setError(null);
            isValidDishname = true;
        }

        if (TextUtils.isEmpty(description)) {
            desc.setErrorEnabled(true);
            desc.setError("Chi tiết món ăn không được để trống");
        } else if (description.length()>20) {
            desc.setErrorEnabled(true);
            desc.setError("Chi tiết món ăn không được quá 20 kí tự");
        }
        else if (description.length()<3) {
            desc.setErrorEnabled(true);
            desc.setError("Chi tiết món ăn không được ít hơn 3 kí tự");
        }
        else {
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
            } else if (priceValue<1000) {
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
            if(TextUtils.isEmpty(price))
            {disc.setErrorEnabled(true);
                disc.setError("Bắt buộc phải nhập giá món ăn trước khi nhập giá giảm");}
            else if (!TextUtils.isEmpty(price)&&TextUtils.isEmpty(discount)) {
                disc.setErrorEnabled(true);
                disc.setError("Giá giảm không được để trống");
            } else if (!TextUtils.isEmpty(price)&&!TextUtils.isEmpty(discount)&&giaGiamDouble >= giaGocDouble) {

                new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                        .setTitle("Lỗi")
                        .setMessage("Giá giảm phải nhỏ hơn giá gốc của món ăn")
                        .setPositiveButton("OK", null).show();
            } else {
                giaGiamDouble = Double.parseDouble(discount);
                tiLeDouble = (giaGocDouble - giaGiamDouble) / giaGocDouble * 100;
                int lamTron = (int) Math.round(tiLeDouble);
                title = String.valueOf(lamTron);
                dishCountPercent.setText("Giảm " + lamTron+"%");
                isValidDis=true;
            }
        } else {
            isValidDis=true;
            discount = "0";
            title="";
        }
        isvalid = (isValidDis&&isValiDescription&& isValidDishname  && isValidPrice) ? true : false;
        return isvalid;
    }



    private void uploadImage() {

        if (imageuri != null) {

            progressDialog.setTitle("Đang tải...");
            progressDialog.show();
            RandomUId = UUID.randomUUID().toString();
            ref = storageReference.child(RandomUId);
            ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            updatedesc(valueOf(uri));
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

    private void updatedesc(String uri) {
        ChefId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FoodSupplyDetails info = new FoodSupplyDetails(cateID,dishes, price, description, uri, ID, ChefId,discount,title,Discounting,Available);
        firebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(State).child(City).child(Sub)
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

        CropImage.startPickImageActivity(this);
    }
    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageuri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                mCropimageuri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            } else {

                startCropImageActivity(imageuri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((ImageButton) findViewById(R.id.imageupload)).setImageURI(result.getUri());
                Toast.makeText(this, "Cắt ảnh thành công" + result.getSampleSize(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cắt ảnh thất bại" + result.getError(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mCropimageuri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCropImageActivity(mCropimageuri);
        } else {
            Toast.makeText(this, "Hủy bỏ , yêu cầu không được cấp phép", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCropImageActivity(Uri imageuri) {

        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }
}
