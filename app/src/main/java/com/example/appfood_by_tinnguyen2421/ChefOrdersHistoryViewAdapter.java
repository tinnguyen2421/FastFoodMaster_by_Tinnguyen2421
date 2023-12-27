package com.example.appfood_by_tinnguyen2421;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders;

import java.util.List;

public class ChefOrdersHistoryViewAdapter extends RecyclerView.Adapter<ChefOrdersHistoryViewAdapter.ViewHolder>
{
    private Context mcontext;
    private List<ChefFinalOrders> chefFinalOrderslist;

    public ChefOrdersHistoryViewAdapter(Context mcontext, List<ChefFinalOrders> chefFinalOrderslist) {
        this.mcontext = mcontext;
        this.chefFinalOrderslist = chefFinalOrderslist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.chef_orders_history_view_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChefFinalOrders chefFinalOrders=chefFinalOrderslist.get(position);
        holder.dishname.setText(position+1+"."+chefFinalOrders.getDishName());
        holder.price.setText("Giá tiền: " + chefFinalOrders.getDishPrice()+"đ");
        holder.quantity.setText("× " + chefFinalOrders.getDishQuantity());
        holder.totalprice.setText("Tổng cộng:" + chefFinalOrders.getTotalPrice()+"đ");
    }

    @Override
    public int getItemCount() {
        return chefFinalOrderslist.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishname = itemView.findViewById(R.id.Cdishname);
            price = itemView.findViewById(R.id.Cdishprice);
            totalprice = itemView.findViewById(R.id.Ctotalprice);
            quantity = itemView.findViewById(R.id.Cdishqty);
        }
    }
}
