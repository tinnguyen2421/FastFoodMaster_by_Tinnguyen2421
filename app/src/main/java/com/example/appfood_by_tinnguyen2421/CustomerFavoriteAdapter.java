package com.example.appfood_by_tinnguyen2421;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.OrderDish;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class CustomerFavoriteAdapter extends RecyclerView.Adapter<CustomerFavoriteAdapter.MyViewHolder> {

    ArrayList<UpdateDishModel> updateDishModelArrayList;
    Context context;
    String Tag;

    public CustomerFavoriteAdapter(ArrayList<UpdateDishModel> foodSupplyDetails, Context context) {
        this.updateDishModelArrayList = foodSupplyDetails;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.categoryrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        UpdateDishModel updateDishModel = updateDishModelArrayList.get(position);
        if (updateDishModel != null) {
            holder.title.setText(updateDishModel.getDishes());
            Picasso.get()
                    .load(updateDishModel.getImageURL())
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Error : ", e.getMessage());
                        }
                    });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDish.class);
                intent.putExtra("FoodMenu"  ,updateDishModel.getRandomUID());
                intent.putExtra("ChefId",updateDishModel.getChefId());
                intent.putExtra("CateID",updateDishModel.getCateID());
                intent.putExtra("TenMon",updateDishModel.getDishes());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return updateDishModelArrayList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        ProgressBar progressBar;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.category_image);
            title = itemView.findViewById(R.id.category_title);
            progressBar = itemView.findViewById(R.id.progressbar);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
