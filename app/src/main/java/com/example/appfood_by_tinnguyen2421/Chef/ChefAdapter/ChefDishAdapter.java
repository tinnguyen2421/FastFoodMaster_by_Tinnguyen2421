package com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefUpdateDish;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;
import com.example.appfood_by_tinnguyen2421.R;

import java.util.List;


public class ChefDishAdapter extends RecyclerView.Adapter<ChefDishAdapter.ViewHolder> {
    //May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
    private Context mContext;
    private List<UpdateDishModel> mUpdateDishModelList;

    public ChefDishAdapter(Context context, List<UpdateDishModel> updateDishModelList) {
        this.mContext = context;
        this.mUpdateDishModelList = updateDishModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chef_menu_update_delete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UpdateDishModel updateDishModel = mUpdateDishModelList.get(position);

        setUpData(holder,updateDishModel);
        setVisibility(holder,updateDishModel);
        setUpListeners(holder,updateDishModel);
    }

    private void setUpListeners(ViewHolder holder,UpdateDishModel updateDishModel) {
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ChefUpdateDish.class);
            intent.putExtra("updatedeletedish", updateDishModel.getRandomUID());
            mContext.startActivity(intent);
        });
    }

    private void setVisibility(ViewHolder holder, UpdateDishModel updateDishModel) {
        if (updateDishModel.getAvailableDish().equals("true")) {
            if (updateDishModel.getOnSale().equals("true")) {
                holder.titlePercent.setVisibility(View.VISIBLE);
                holder.saleCost.setVisibility(View.VISIBLE);
                holder.cost.setPaintFlags(holder.cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.titlePercent.setVisibility(View.GONE);
                holder.saleCost.setVisibility(View.GONE);
                holder.cost.setPaintFlags(0);
            }
            holder.availableDish.setVisibility(View.GONE);
            holder.cardViewDishes.setAlpha(1.0F);
        } else {
            holder.titlePercent.setVisibility(View.GONE);
            holder.saleCost.setVisibility(View.GONE);
            holder.cost.setPaintFlags(0);
            holder.availableDish.setVisibility(View.VISIBLE);
            holder.cardViewDishes.setAlpha(0.5F);
        }
    }

    @Override
    public int getItemCount() {
        return mUpdateDishModelList.size();
    }
    private void setUpData(ViewHolder holder,UpdateDishModel updateDishModel)
    {
        holder.dishes.setText(updateDishModel.getDishName());
        holder.saleCost.setText(updateDishModel.getReducePrice());
        holder.cost.setText(updateDishModel.getDishPrice());
        holder.titlePercent.setText("Giảm " + updateDishModel.getDecreasePercent() + "%");
        Glide.with(mContext).load(updateDishModel.getImageURL()).into(holder.imgCate);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCate;
        TextView dishes, titlePercent, cost, saleCost, availableDish;
        CardView cardViewDishes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishes = itemView.findViewById(R.id.tenSp);
            titlePercent = itemView.findViewById(R.id.tiLe);
            cost = itemView.findViewById(R.id.giaGoc);
            saleCost = itemView.findViewById(R.id.giamCon);
            imgCate = itemView.findViewById(R.id.ImageCategory);
            availableDish = itemView.findViewById(R.id.AvaiableDish);
            cardViewDishes = itemView.findViewById(R.id.CardViewDishes);
        }
    }
}