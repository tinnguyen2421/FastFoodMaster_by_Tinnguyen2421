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

import java.util.List;

public class ChefOrdersHistoryViewAdapter extends RecyclerView.Adapter<ChefOrdersHistoryViewAdapter.ViewHolder>
{
    private Context mContext;
    private List<ChefFinalOrders> mChefFinalOrdersList;

    public ChefOrdersHistoryViewAdapter(Context context, List<ChefFinalOrders> chefFinalOrdersList) {
        this.mContext = context;
        this.mChefFinalOrdersList = chefFinalOrdersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chef_orders_history_view_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChefFinalOrders chefFinalOrders = mChefFinalOrdersList.get(position);
        setUpData(holder,chefFinalOrders,position);
    }

    private void setUpData(ViewHolder holder, ChefFinalOrders chefFinalOrders, int position) {
        holder.dishName.setText((position + 1) + ". " + chefFinalOrders.getDishName());
        holder.price.setText("Giá tiền: " + chefFinalOrders.getDishPrice() + "đ");
        holder.quantity.setText("× " + chefFinalOrders.getDishQuantity());
        holder.totalPrice.setText("Tổng cộng: " + chefFinalOrders.getTotalPrice() + "đ");
    }

    @Override
    public int getItemCount() {
        return mChefFinalOrdersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishName, price, totalPrice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.Cdishname);
            price = itemView.findViewById(R.id.Cdishprice);
            totalPrice = itemView.findViewById(R.id.Ctotalprice);
            quantity = itemView.findViewById(R.id.Cdishqty);
        }
    }
}
