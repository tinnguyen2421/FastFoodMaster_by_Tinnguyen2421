package com.example.appfood_by_tinnguyen2421.Chef.ChefActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.Categories;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateCateModel;
import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.ReusableCodeForAll;
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

public class ChefUpdateCategories extends AppCompatActivity {
    private ArrayList<String> categoryList;
    ImageButton imageButton;
    Uri imageuri;
    String dburi;
    TextInputLayout CateName, DesCate;
    private Uri mCropimageuri;

    String cateIDD,CateNamee, DesCatee,  ChefId;
    String RandomUId;

    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth FAuth;

    private ProgressDialog progressDialog;
    DatabaseReference dataaa;
    String District, City, Ward;
    EditText CateID;
    Button btnUpdateCate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_update_cate);
        CateID =findViewById(R.id.Cate_id);
        CateName=findViewById(R.id.cate_name);
        DesCate=findViewById(R.id.MotaCate);
        btnUpdateCate =findViewById(R.id.Updatedishh);
        imageButton=findViewById(R.id.imageupload);
        RandomUId = getIntent().getStringExtra("mtl");
        categoryList=new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = firebaseDatabase.getInstance().getReference("Chef").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel chefc = dataSnapshot.getValue(UserModel.class);
                District = chefc.getDistrict();
                City = chefc.getCity();
                Ward = chefc.getWard();
                btnUpdateCate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        cateIDD= CateID.getText().toString().trim();
                        CateNamee=CateName.getEditText().getText().toString().trim();
                        DesCatee = DesCate.getEditText().getText().toString().trim();
                        if (isValid()) {
                            if (imageuri != null) {
                                uploadImage();
                            } else {
                                updateDesc(dburi);
                            }
                        }
                    }
                });

                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                progressDialog = new ProgressDialog(ChefUpdateCategories.this);
                databaseReference = FirebaseDatabase.getInstance().getReference("Categories").child(City).child(District).child(Ward).child(useridd).child(RandomUId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        UpdateCateModel updateCateModel = dataSnapshot.getValue(UpdateCateModel.class);
                        CateID.setText( updateCateModel.getCateID());
                        DesCate.getEditText().setText(updateCateModel.getMota());
                        CateName.getEditText().setText(updateCateModel.getTentheloai());
                        Glide.with(ChefUpdateCategories.this).load(updateCateModel.getImage()).into(imageButton);
                        dburi = updateCateModel.getImage();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                FAuth = FirebaseAuth.getInstance();
                databaseReference = firebaseDatabase.getInstance().getReference("Categories");
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
    private boolean isValid() {

        CateName.setErrorEnabled(false);
        CateName.setError("");
        DesCate.setErrorEnabled(false);
        DesCate.setError("");

        boolean isValiDescription = false, isValidCateName = false, isvalid = false;
        if (TextUtils.isEmpty(DesCatee)) {
            DesCate.setErrorEnabled(true);
            DesCate.setError("Chi tiết về thể loại không được để trống");

        }
        else {
            if (DesCatee.length() > 40) {
                DesCate.setErrorEnabled(true);
                DesCate.setError("Chi tiết thể loại không vượt quá 40 ký tự");
            } else if (DesCatee.length() < 1) {
                DesCate.setErrorEnabled(true);
                DesCate.setError("Chi tiết thể loại không được ít hơn 1 ký tự");
            } else {
                DesCate.setError(null);
                isValiDescription = true;
            }
        }
        if (TextUtils.isEmpty(CateNamee)) {
            CateName.setErrorEnabled(true);
            CateName.setError("Tên thể loại không được để trống");

        }
        else {
            if (CateNamee.length() > 20) {
                CateName.setErrorEnabled(true);
                CateName.setError("Tên thể loại không vượt quá 40 ký tự");
            } else if (CateNamee.length() < 1) {
                CateName.setErrorEnabled(true);
                CateName.setError("Tên thể loại không được ít hơn 1 ký tự");
            } else {
                CateName.setError(null);
                isValidCateName = true;
            }
        }


        isvalid = (isValiDescription && isValidCateName ) ? true : false;

        return isvalid;
    }
    private void uploadImage() {

        if (imageuri != null) {

            progressDialog.setTitle("Đang tải...");
            progressDialog.show();
            ref = storageReference.child(RandomUId);
            ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(ChefUpdateCategories.this, "Thất bại : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
        ChefId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Categories info = new Categories( cateIDD,CateNamee, DesCatee, uri,RandomUId);
        firebaseDatabase.getInstance().getReference("Categories").child(City).child(District).child(Ward)
                .child(ChefId).child(RandomUId)
                .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        ReusableCodeForAll.ShowAlert(ChefUpdateCategories.this,"Thành Công","Cập nhât thành công");
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
