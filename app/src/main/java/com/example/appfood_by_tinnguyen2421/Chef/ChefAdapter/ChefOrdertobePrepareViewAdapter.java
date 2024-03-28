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
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefOrdertobePrepareViewAdapter extends RecyclerView.Adapter<ChefOrdertobePrepareViewAdapter.ViewHolder> {

    private Context mContext;
    private List<ChefFinalOrders> mChefWaitingOrdersList;

    public ChefOrdertobePrepareViewAdapter(Context context, List<ChefFinalOrders> chefWaitingOrdersList) {
        this.mContext = context;
        this.mChefWaitingOrdersList = chefWaitingOrdersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chef_ordertobeprepared_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChefFinalOrders chefWaitingOrders = mChefWaitingOrdersList.get(position);
        setUpData(holder,chefWaitingOrders,position);
    }

    private void setUpData(ViewHolder holder, ChefFinalOrders chefWaitingOrders, int position) {
        holder.dishname.setText((position + 1) + ". " + chefWaitingOrders.getDishName());
        holder.price.setText("Giá: " + chefWaitingOrders.getDishPrice() + "đ");
        holder.quantity.setText("× " + chefWaitingOrders.getDishQuantity());
        holder.totalprice.setText("Tổng tiền: " + chefWaitingOrders.getTotalPrice() + "đ");
    }

    @Override
    public int getItemCount() {
        return mChefWaitingOrdersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishname = itemView.findViewById(R.id.Dname);
            price = itemView.findViewById(R.id.Dprice);
            totalprice = itemView.findViewById(R.id.Tprice);
            quantity = itemView.findViewById(R.id.Dqty);
        }
    }
}
