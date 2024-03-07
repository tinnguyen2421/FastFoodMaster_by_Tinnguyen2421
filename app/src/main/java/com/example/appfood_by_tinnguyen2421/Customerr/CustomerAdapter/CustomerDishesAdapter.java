package com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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
    private DatabaseReference mFvrtListRef;
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
        Glide.with(mContext).load(updateDishModel.getImageURL()).into(holder.imageView);
        holder.dishName.setText(updateDishModel.getDishName());
        double price = Double.parseDouble(updateDishModel.getDishPrice());
        double priceReduce = Double.parseDouble(updateDishModel.getReducePrice());
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
        String formatPrice = decimalFormat.format(price);
        String formatPriceReduce = decimalFormat.format(priceReduce);
        holder.price.setText(formatPrice + "đ");
        holder.priceReduce.setText(formatPriceReduce + "đ");
        holder.priceReduce.setTextColor(Color.RED);
        holder.title.setText("Giảm " + updateDishModel.getDecreasePercent() + "%");

        if (updateDishModel.getAvailableDish().equals("true")) {
            if (updateDishModel.getOnSale().equals("true")) {
                holder.title.setVisibility(View.VISIBLE);
                holder.priceReduce.setVisibility(View.VISIBLE);
                holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.title.setVisibility(View.GONE);
                holder.priceReduce.setVisibility(View.GONE);
                holder.price.setPaintFlags(0);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderDish.class);
                    intent.putExtra("FoodMenu", updateDishModel.getRandomUID());
                    intent.putExtra("ChefId", updateDishModel.getChefID());
                    intent.putExtra("CateID", updateDishModel.getCateID());
                    intent.putExtra("TenMon", updateDishModel.getDishName());
                    mContext.startActivity(intent);
                }
            });

            holder.imageViewShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
            });

            String key = updateDishModel.getDishName();
            favouriteChecker(key, holder);

            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProcessLike = true;
                    mFvrtRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (mProcessLike && snapshot.child(FirebaseAuth.getInstance().getUid()).hasChild(key)) {
                                mFvrtRef.child(FirebaseAuth.getInstance().getUid()).child(key).removeValue();
                                mFvrtListRef.child(key).removeValue();
                                Toast.makeText(mContext, "Xóa khỏi yêu thích!", Toast.LENGTH_SHORT).show();
                                mProcessLike = false;
                                DatabaseReference likedDishesRef = FirebaseDatabase.getInstance().getReference("likedDishes");
                                likedDishesRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).removeValue();
                            } else {
                                mFvrtRef.child(FirebaseAuth.getInstance().getUid()).child(key).setValue(true);
                                Favorite favorite = new Favorite(mFvrtListRef.push().getKey(), FirebaseAuth.getInstance().getUid(), updateDishModel, true);
                                mFvrtListRef.child(key).setValue(favorite);
                                mProcessLike = false;
                                Toast.makeText(mContext, "Thêm vào yêu thích!", Toast.LENGTH_SHORT).show();
                                DatabaseReference likedDishesRef = FirebaseDatabase.getInstance().getReference("likedDishes");
                                likedDishesRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).setValue(updateDishModel);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            });
        } else {
            holder.availableDish.setVisibility(View.VISIBLE);
            holder.linearDishes.setAlpha(0.3F);
        }
    }

    public void favouriteChecker(final String postKey, final ViewHolder holder) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String uid = user.getUid();
            mFvrtRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(uid).hasChild(postKey)) {
                        holder.likeButton.setImageResource(R.drawable.baseline_favorited);
                    } else {
                        holder.likeButton.setImageResource(R.drawable.baseline_favorite_border_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
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
        TextView dishName, price, title, priceReduce, availableDish;
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
            availableDish = itemView.findViewById(R.id.AvaiableDish);
            linearDishes = itemView.findViewById(R.id.LinearDishes);
        }
    }
}
