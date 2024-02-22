package com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.OrderDish;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.Favorite;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DecimalFormat;
import java.util.List;

//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerDishesAdapter extends RecyclerView.Adapter<CustomerDishesAdapter.ViewHolder> {


    private Context mcontext;
    private List<UpdateDishModel>updateDishModellist;
    DatabaseReference fvrtref,fvrt_listRef;
    Boolean mProcessLike =false;

    public CustomerDishesAdapter(Context context, List<UpdateDishModel> updateDishModellist)
    {
        this.updateDishModellist=updateDishModellist;
        this.mcontext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.monan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final UpdateDishModel updateDishModel=updateDishModellist.get(position);
        Glide.with(mcontext).load(updateDishModel.getImageURL()).into(holder.imageView);
        holder.Dishname.setText(updateDishModel.getDishName());
        updateDishModel.getRandomUID();
        updateDishModel.getChefID();
        double price=Double.parseDouble(updateDishModel.getDishPrice());
        double priceReduce=Double.parseDouble(updateDishModel.getReducePrice());
        DecimalFormat decimalFormat=new DecimalFormat("#,###,###,###");
        String FormatPrice=decimalFormat.format(price);
        String FormatPriceReduce=decimalFormat.format(priceReduce   );
        holder.price.setText(FormatPrice+"đ");
        holder.priceReduce.setText(FormatPriceReduce+"đ");
        holder.priceReduce.setTextColor(Color.RED);
        holder.tittle.setText("Giảm "+updateDishModel.getDecreasePercent()+"%");
        if(updateDishModel.getOnSale().equals("true"))
        {
            holder.tittle.setVisibility(View.VISIBLE);
            holder.priceReduce.setVisibility(View.VISIBLE);
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            holder.tittle.setVisibility(View.GONE);
            holder.priceReduce.setVisibility(View.GONE);
            holder.price.setPaintFlags(0);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mcontext, OrderDish.class);
                intent.putExtra("FoodMenu"  ,updateDishModel.getRandomUID());
                intent.putExtra("ChefId",updateDishModel.getChefID());

                intent.putExtra("CateID",updateDishModel.getCateID());
                intent.putExtra("TenMon",updateDishModel.getDishName());
                mcontext.startActivity(intent);
            }
        });
        holder.imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent intent=new Intent(Intent.ACTION_SEND);
             intent.setType("text/plain");
             String shareBody=updateDishModel.getDishName();
             String shareSub=updateDishModel.getDescription();
                String shareSub1=updateDishModel.getImageURL();
                String combinedText=shareSub+"\n"+shareSub1;
             intent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
             intent.putExtra(Intent.EXTRA_TEXT,combinedText);

             mcontext.startActivity(intent);
            }
        });
        fvrtref = FirebaseDatabase.getInstance().getReference("favourites");
        fvrt_listRef = FirebaseDatabase.getInstance().getReference("favoriteList").child(FirebaseAuth.getInstance().getUid());
        String  key = updateDishModel.getDishName();
        favouriteChecker(key,holder);
        holder.likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProcessLike = true;
                fvrtref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mProcessLike.equals(true)){
                            if (snapshot.child(FirebaseAuth.getInstance().getUid()).hasChild(key)){
                                fvrtref.child(FirebaseAuth.getInstance().getUid()).child(key).removeValue();
                                fvrt_listRef.child(key).removeValue();
                                Toast.makeText(v.getContext(), "Xóa khỏi yêu thích!", Toast.LENGTH_SHORT).show();
                                mProcessLike = false;
                                DatabaseReference likedDishesRef = FirebaseDatabase.getInstance().getReference("likedDishes");
                                likedDishesRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).removeValue();
                            }else {
                                fvrtref.child(FirebaseAuth.getInstance().getUid()).child(key).setValue(true);
                                Favorite favorite = new Favorite(fvrt_listRef.push().getKey(), FirebaseAuth.getInstance().getUid(), updateDishModel,true);
                                fvrt_listRef.child(key).setValue(favorite);
                                mProcessLike = false;
                                Toast.makeText(v.getContext(), "Thêm vào yêu thích!", Toast.LENGTH_SHORT).show();
                                DatabaseReference likedDishesRef = FirebaseDatabase.getInstance().getReference("likedDishes");
                                likedDishesRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).setValue(updateDishModel);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

    }

    public void favouriteChecker(final String postkey, ViewHolder holder) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        fvrtref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild(postkey)){
                    holder.likebtn.setImageResource(R.drawable.baseline_favorited);
                }else {
                    holder.likebtn.setImageResource(R.drawable.baseline_favorite_border_24);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView,imageViewShare;
        TextView Dishname,price,tittle,priceReduce;
        ElegantNumberButton additem;
        ImageView likebtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewShare=itemView.findViewById(R.id.share_btn);
            imageView=itemView.findViewById(R.id.menu_image);
            Dishname=itemView.findViewById(R.id.dishname);
            price=itemView.findViewById(R.id.dishprice);
            additem=itemView.findViewById(R.id.number_btn);
            tittle=itemView.findViewById(R.id.tiLe);
            priceReduce=itemView.findViewById(R.id.PriceReduce);
            likebtn=itemView.findViewById(R.id.likeBtn);


        }
    }
}
