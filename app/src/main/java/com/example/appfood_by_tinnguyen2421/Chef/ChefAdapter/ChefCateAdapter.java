package com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.Categories;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefDishes;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefUpdateCategories;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefCateAdapter extends RecyclerView.Adapter<ChefCateAdapter.ViewHolder> {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataaa;
    private Context mContext;
    private List<Categories> mCategoryList;

    public ChefCateAdapter(Context context, List<Categories> categoryList) {
        this.mContext = context;
        this.mCategoryList = categoryList;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chef_cate_update_delete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Categories category = mCategoryList.get(position);
        setUpData( holder,category);
        setUpListeners(holder,category);
    }

    private void showDeleteConfirmationDialog(Categories category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure you want to delete this category?");
        builder.setPositiveButton("Có", (dialog, which) -> deleteCategory(category));
        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void setUpData(ViewHolder holder,Categories category)
    {
        holder.dishCategory.setText(category.getTentheloai());
        Glide.with(mContext).load(category.getImage()).into(holder.imgCategory);
    }
    private void setUpListeners(ViewHolder holder,Categories category) {
        holder.categoryLayout.setOnClickListener(view -> showDishes(category));
        holder.editButton.setOnClickListener(view -> updateCategory(category));
        holder.deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(category));
    }
    private void deleteCategory(Categories category) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference chefRef = firebaseDatabase.getReference("Chef").child(userId);
        chefRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel chef = dataSnapshot.getValue(UserModel.class);
                if (chef != null) {
                    String district = chef.getDistrict();
                    String city = chef.getCity();
                    String ward = chef.getWard();
                    firebaseDatabase.getReference("Categories")
                            .child(city)
                            .child(district)
                            .child(ward)
                            .child(userId)
                            .child(category.getRandomUID())
                            .removeValue().addOnSuccessListener(unused -> {
                                showDeletionSuccessDialog();
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void showDeletionSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Thể loại đã được xóa!");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showDishes(Categories category)
    {
        Intent intent = new Intent(mContext, ChefDishes.class);
        intent.putExtra("matl", category.getMatheloai());
        mContext.startActivity(intent);
    }
    private void updateCategory(Categories category)
    {
        Intent intent1 = new Intent(mContext, ChefUpdateCategories.class);
        intent1.putExtra("mtl", category.getRandomUID());
        mContext.startActivity(intent1);
    }
    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout categoryLayout;
        TextView dishCategory;
        TextView editButton, deleteButton;
        ImageView imgCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryLayout = itemView.findViewById(R.id.CateLayout);
            dishCategory = itemView.findViewById(R.id.dish_cate);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            imgCategory = itemView.findViewById(R.id.ImageCategory);
        }
    }
}
