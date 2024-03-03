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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefUpdateDish;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;
import com.example.appfood_by_tinnguyen2421.R;

import java.util.List;


public class ChefDishAdapter extends RecyclerView.Adapter<ChefDishAdapter.ViewHolder> {
    //May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
    private Context mcont;
    private List<UpdateDishModel> updateDishModellist;

    public ChefDishAdapter(Context context, List<UpdateDishModel> updateDishModellist) {
        this.updateDishModellist = updateDishModellist;
        this.mcont = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcont).inflate(R.layout.chef_menu_update_delete, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final UpdateDishModel updateDishModel = updateDishModellist.get(position);
        holder.dishes.setText(updateDishModel.getDishName());
        holder.saleCost.setText(updateDishModel.getReducePrice());
        holder.cost.setText(updateDishModel.getDishPrice());
        holder.titlePercent.setText("Giảm "+updateDishModel.getDecreasePercent()+"%");
        Glide.with(mcont).load(updateDishModel.getImageURL()).into(holder.imgCate);
        updateDishModel.getRandomUID();
        if(updateDishModel.getOnSale().equals("true"))
        {
            holder.titlePercent.setVisibility(View.VISIBLE);
            holder.saleCost.setVisibility(View.VISIBLE);
            holder.cost.setPaintFlags(holder.cost.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
           holder.titlePercent.setVisibility(View.GONE);
           holder.saleCost.setVisibility(View.GONE);
           holder.cost.setPaintFlags(0);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcont, ChefUpdateDish.class);
                intent.putExtra("updatedeletedish", updateDishModel.getRandomUID());
                mcont.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCate;
        TextView dishes,titlePercent,cost,saleCost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishes = itemView.findViewById(R.id.tenSp );
            titlePercent=itemView.findViewById(R.id.tiLe);
            cost=itemView.findViewById(R.id.giaGoc);
            saleCost=itemView.findViewById(R.id.giamCon);
            imgCate=itemView.findViewById(R.id.ImageCategory);
        }
    }
}