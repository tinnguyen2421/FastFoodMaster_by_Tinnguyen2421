package com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders;
import com.example.appfood_by_tinnguyen2421.R;


import java.text.DecimalFormat;
import java.util.List;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefOrderDishesAdapter extends RecyclerView.Adapter<ChefOrderDishesAdapter.ViewHolder> {


    private Context mContext;
    private List<ChefFinalOrders> mChefPendingOrdersList;

    public ChefOrderDishesAdapter(Context context, List<ChefFinalOrders> chefPendingOrdersList) {
        this.mChefPendingOrdersList = chefPendingOrdersList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chef_order_dishes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChefFinalOrders chefPendingOrders = mChefPendingOrdersList.get(position);
        setUpData(holder, chefPendingOrders,position);

    }

    private void setUpData(ViewHolder holder, ChefFinalOrders chefPendingOrders,int position) {
        holder.dishName.setText((position + 1) + ". " + chefPendingOrders.getDishName());
        setUpTextView(holder.price, chefPendingOrders.getDishPrice());
        holder.quantity.setText("× " + chefPendingOrders.getDishQuantity());
        setUpTextView(holder.totalPrice, chefPendingOrders.getTotalPrice());
    }

    private void setUpTextView(TextView textView, String priceString) {
        if (priceString != null) {
            String priceWithoutComma = priceString.replace(",", "").trim();
            try {
                double parsedNumber = Double.parseDouble(priceWithoutComma);
                DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
                String formattedPrice = decimalFormat.format(parsedNumber);
                textView.setText(formattedPrice + "đ");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mChefPendingOrdersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishName, price, totalPrice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.DN);
            price = itemView.findViewById(R.id.PR);
            totalPrice = itemView.findViewById(R.id.TR);
            quantity = itemView.findViewById(R.id.QY);
        }
    }
}
