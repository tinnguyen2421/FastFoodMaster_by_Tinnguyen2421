package com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.OrderDish;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.Favorite;
import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.text.DecimalFormat;
import java.util.List;


public class CustomerDishesAdapter extends RecyclerView.Adapter<CustomerDishesAdapter.ViewHolder> {

    private Context mContext;
    private List<UpdateDishModel> mUpdateDishModelList;
    private DatabaseReference mFvrtRef;
    private DatabaseReference mFvrtListRef, databaseReference;
    private boolean mProcessLike = false;

    public CustomerDishesAdapter(Context context, List<UpdateDishModel> updateDishModelList) {
        this.mUpdateDishModelList = updateDishModelList;
        this.mContext = context;
        this.mFvrtRef = FirebaseDatabase.getInstance().getReference("favourites");
        this.mFvrtListRef = FirebaseDatabase.getInstance().getReference("favoriteList").child(FirebaseAuth.getInstance().getUid());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.monan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UpdateDishModel updateDishModel = mUpdateDishModelList.get(position);
        setUpData(holder,updateDishModel);
        favouriteChecker(updateDishModel.getRandomUID(), holder);
        setUpVisible(holder,updateDishModel);
        setUpListeners(holder,updateDishModel);
    }

    private void setUpListeners(ViewHolder holder, UpdateDishModel updateDishModel) {
        holder.itemView.setOnClickListener(v -> orderDish(updateDishModel));

        holder.imageViewShare.setOnClickListener(view -> shareDish(updateDishModel));

        holder.likeButton.setOnClickListener(v ->likeProcess(updateDishModel,v,holder));
    }

    private void likeProcess(UpdateDishModel updateDishModel, View v, ViewHolder holder) {
        mProcessLike = true;
        String key1 = updateDishModel.getRandomUID();
        DatabaseReference likedDishesRef = FirebaseDatabase.getInstance().getReference("likedDishes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key1);
        mFvrtRef.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mProcessLike) {
                    if (snapshot.hasChild(key1)) {
                        mFvrtRef.child(FirebaseAuth.getInstance().getUid()).child(key1).removeValue();
                        mFvrtListRef.child(key1).removeValue();
                        Toast.makeText(v.getContext(), "Xóa khỏi yêu thích!", Toast.LENGTH_SHORT).show();
                        mProcessLike = false;
                        likedDishesRef.removeValue();
                        // Đặt hình ảnh trái tim không được yêu thích
                        holder.likeButton.setImageResource(R.drawable.baseline_favorite_border_24);
                    } else {
                        mFvrtRef.child(FirebaseAuth.getInstance().getUid()).child(key1).setValue(true);
                        Favorite favorite = new Favorite(mFvrtListRef.push().getKey(), FirebaseAuth.getInstance().getUid(), updateDishModel, true);
                        mFvrtListRef.child(key1).setValue(favorite);
                        mProcessLike = false;
                        Toast.makeText(v.getContext(), "Thêm vào yêu thích!", Toast.LENGTH_SHORT).show();
                        likedDishesRef.setValue(updateDishModel);
                        // Đặt hình ảnh trái tim đã được yêu thích
                        holder.likeButton.setImageResource(R.drawable.baseline_favorited);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void shareDish(UpdateDishModel updateDishModel) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = updateDishModel.getDishName();
        String shareSub = updateDishModel.getDescription();
        String shareSub1 = updateDishModel.getImageURL();
        String combinedText = shareSub + "\n" + shareSub1;
        intent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
        intent.putExtra(Intent.EXTRA_TEXT, combinedText);
        mContext.startActivity(intent);
    }

    private void orderDish(UpdateDishModel updateDishModel) {
        Intent intent = new Intent(mContext, OrderDish.class);
        intent.putExtra("FoodMenu", updateDishModel.getRandomUID());
        intent.putExtra("ChefId", updateDishModel.getChefID());
        intent.putExtra("CateID", updateDishModel.getCateID());
        intent.putExtra("TenMon", updateDishModel.getDishName());
        mContext.startActivity(intent);
    }

    private void setUpData(ViewHolder holder, UpdateDishModel updateDishModel) {
        Glide.with(mContext).load(updateDishModel.getImageURL()).into(holder.imageView);
        holder.dishName.setText(updateDishModel.getDishName());
        holder.price.setText(formatPrice(updateDishModel.getDishPrice()) + "đ");
        holder.priceReduce.setText(formatPrice(updateDishModel.getReducePrice()) + "đ");
        holder.priceReduce.setTextColor(Color.RED);
        holder.title.setText("Giảm " + updateDishModel.getDecreasePercent() + "%");
    }

    private String formatPrice(String priceString) {
        double price = Double.parseDouble(priceString);
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
        return decimalFormat.format(price) ;
    }
    private void setUpVisible(ViewHolder holder, UpdateDishModel updateDishModel) {
        databaseReference = FirebaseDatabase.getInstance().getReference("ChefStatus").child(updateDishModel.getChefID() + "/Status");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                if (status != null && status.equals("Mở cửa")) {
                    if (updateDishModel.getAvailableDish() != null && updateDishModel.getAvailableDish().equals("true")) {
                        holder.dishesStatus.setVisibility(View.GONE);
                        holder.linearDishes.setAlpha(1.0F);
                        if (updateDishModel.getOnSale() != null && updateDishModel.getOnSale().equals("true")) {
                            holder.title.setVisibility(View.VISIBLE);
                            holder.priceReduce.setVisibility(View.VISIBLE);
                            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        } else {
                            holder.title.setVisibility(View.GONE);
                            holder.priceReduce.setVisibility(View.GONE);
                            holder.price.setPaintFlags(0);
                        }
                    } else {
                        holder.dishesStatus.setVisibility(View.VISIBLE);
                        holder.linearDishes.setAlpha(0.3F);
                    }
                } else {
                    holder.shopStatus.setVisibility(View.VISIBLE);
                    holder.linearDishes.setAlpha(0.3F);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }

    public void favouriteChecker(final String postKey, final ViewHolder holder) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String uid = user.getUid();
            mFvrtRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(postKey)) {
                        holder.likeButton.setImageResource(R.drawable.baseline_favorited);
                    } else {
                        holder.likeButton.setImageResource(R.drawable.baseline_favorite_border_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu có
                }
            });
        }
    }




    @Override
    public int getItemCount() {
        return mUpdateDishModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imageViewShare, likeButton;
        TextView dishName, price, title, priceReduce, dishesStatus, shopStatus;
        ElegantNumberButton addItem;
        LinearLayout linearDishes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewShare = itemView.findViewById(R.id.share_btn);
            imageView = itemView.findViewById(R.id.menu_image);
            dishName = itemView.findViewById(R.id.dishname);
            price = itemView.findViewById(R.id.dishprice);
            addItem = itemView.findViewById(R.id.number_btn);
            title = itemView.findViewById(R.id.tiLe);
            priceReduce = itemView.findViewById(R.id.PriceReduce);
            likeButton = itemView.findViewById(R.id.likeBtn);
            dishesStatus = itemView.findViewById(R.id.AvaiableDish);
            linearDishes = itemView.findViewById(R.id.LinearDishes);
            shopStatus = itemView.findViewById(R.id.ShopClosed);
        }
    }
}

