package com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders;
import com.example.appfood_by_tinnguyen2421.R;

import java.util.List;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerTrackAdapter extends RecyclerView.Adapter<CustomerTrackAdapter.ViewHolder> {

    private Context context;
    private List<CustomerOrders> customerOrdersList;

    public CustomerTrackAdapter(Context context, List<CustomerOrders> customerOrdersList) {
        this.customerOrdersList = customerOrdersList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_trackorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CustomerOrders customerFinalOrders = customerOrdersList.get(position);
        setUpData(holder,customerFinalOrders,position);

    }

    private void setUpData(ViewHolder holder, CustomerOrders customerFinalOrders, int position) {
        Glide.with(context).load(customerFinalOrders.getImageURL()).into(holder.dishImage);
        holder.dishName.setText(customerFinalOrders.getDishName());
        holder.dishPrice.setText(customerFinalOrders.getDishPrice());
        holder.dishQuantity.setText( "× "+customerFinalOrders.getDishQuantity() );
        holder.totalPrice.setText( customerFinalOrders.getTotalPrice()+"đ");
    }

    @Override
    public int getItemCount() {
        return customerOrdersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishName, dishQuantity, dishPrice, totalPrice;
        ImageView dishImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishName = itemView.findViewById(R.id.DishName);
            dishPrice =itemView.findViewById(R.id.DishPrice);
            dishQuantity = itemView.findViewById(R.id.DishQuantity);
            dishImage =itemView.findViewById(R.id.imageView);
            totalPrice = itemView.findViewById(R.id.TotalPrice);
        }
    }
}
