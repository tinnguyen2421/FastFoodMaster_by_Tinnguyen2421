package com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerFinalOrders;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerPaymentOrders;
import com.example.appfood_by_tinnguyen2421.R;

import java.util.List;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerTrackAdapter extends RecyclerView.Adapter<CustomerTrackAdapter.ViewHolder> {

    private Context context;
    private List<CustomerPaymentOrders> customerPaymentOrdersList;

    public CustomerTrackAdapter(Context context, List<CustomerPaymentOrders> customerPaymentOrdersList) {
        this.customerPaymentOrdersList = customerPaymentOrdersList;
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

        final CustomerPaymentOrders customerFinalOrders = customerPaymentOrdersList.get(position);
        holder.dishName.setText(customerFinalOrders.getDishName());
        holder.dishQuantity.setText(customerFinalOrders.getDishQuantity() + "× ");
        holder.totalPrice.setText( customerFinalOrders.getTotalPrice()+"đ");

    }

    @Override
    public int getItemCount() {
        return customerPaymentOrdersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishName, dishQuantity, dishPrice, totalPrice;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishName = itemView.findViewById(R.id.DishName);
            dishPrice =itemView.findViewById(R.id.DishPrice);
            dishQuantity = itemView.findViewById(R.id.DishQuantity);
            totalPrice = itemView.findViewById(R.id.TotalPrice);
        }
    }
}
