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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.Categories;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.Chef;
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

import java.util.UUID;

public class ChefPostCate extends AppCompatActivity {

    ImageButton imageButton;
    Button post_cate;
    EditText DishesID;
    TextInputLayout nameCatee, motaa;
    String cateID, CateName, Mota;
    Uri imageuri;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_post_cate);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        DishesID = findViewById(R.id.dishes_ID);
        nameCatee = findViewById(R.id.nameCate);
        motaa = findViewById(R.id.Mo_ta);

        post_cate = findViewById(R.id.postCate);
        FAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference("FoodSupplyDetails");
        try {
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dataaa = firebaseDatabase.getInstance().getReference("Chef").child(userid);
            dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Chef chefc = dataSnapshot.getValue(Chef.class);
                    District = chefc.getDistrict();
                    City = chefc.getCity();
                    Ward = chefc.getWard();
                    imageButton = findViewById(R.id.imageupload);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onSelectImageClick(v);
                        }
                    });


                    post_cate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            cateID = DishesID.getText().toString().trim();
                            CateName = nameCatee.getEditText().getText().toString().trim();
                            Mota = motaa.getEditText().getText().toString().trim();
                            if (isValid()) {
                                uploadImage();
                            }
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {

            Log.e("Errrrrr: ", e.getMessage());
        }
    }

    private boolean isValid() {
        nameCatee.setErrorEnabled(false);
        nameCatee.setError("");
        motaa.setErrorEnabled(false);
        motaa.setError("");


        boolean isValidCateName = false, isValidMota = false, isValid = false;
        if (TextUtils.isEmpty(CateName)) {
            nameCatee.setErrorEnabled(true);
            nameCatee.setError("Tên thể loại không được để trống");
        } else {
            nameCatee.setError(null);
            isValidCateName = true;
        }
        if (TextUtils.isEmpty(Mota)) {
            motaa.setErrorEnabled(true);
            motaa.setError("Mô tả không được để trống");
        } else {

            if (Mota.length() > 30) {
                motaa.setErrorEnabled(true);
                motaa.setError("Mô tả thể loại không được vượt quá 30 kí tự ");
            } else if (Mota.length() < 3) {
                motaa.setErrorEnabled(true);
                motaa.setError("Mô tả thể loại không được nhỏ hơn 3 kí tự ");
            } else {
                isValidMota = true;
            }
        }


        isValid = (isValidCateName && isValidMota) ? true : false;

        return isValid;
    }

    private void uploadImage() {

        if (imageuri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(ChefPostCate.this);
            progressDialog.setTitle("Đang tải ảnh...");
            progressDialog.show();
            RandomUId = UUID.randomUUID().toString();
            ref = storageReference.child(RandomUId);
            ChefId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Upload hình ảnh lên Firebase Storage thông qua đối tượng ref
            ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            RandomUId = UUID.randomUUID().toString();
                            Categories info = new Categories(cateID, CateName, Mota, String.valueOf(uri), RandomUId);
                            firebaseDatabase.getInstance().getReference("Categories").child(City).child(District).child(Ward).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId)
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();

                                            if(isValid())
                                            {finish();
                                                Toast.makeText(ChefPostCate.this, "Đăng thể loại thành công", Toast.LENGTH_SHORT).show();}
                                        }
                                    });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(ChefPostCate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Tải lên " + (int) progress + "%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }

    }


    private void onSelectImageClick(View v) {
        // Mở activity để chọn ảnh
        CropImage.startPickImageActivity(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageuri = CropImage.getPickImageResultUri(this, data);
            ((ImageButton) findViewById(R.id.imageupload)).setImageURI(imageuri);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            } else {
                uploadImage();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
