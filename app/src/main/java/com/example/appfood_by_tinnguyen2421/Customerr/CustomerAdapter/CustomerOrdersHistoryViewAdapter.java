package com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders;
import com.example.appfood_by_tinnguyen2421.R;

import java.util.List;

public class CustomerOrdersHistoryViewAdapter extends RecyclerView.Adapter<CustomerOrdersHistoryViewAdapter.ViewHolder>
{
    private Context mcontext;
    private List<CustomerOrders> customerOrdersList;

    public CustomerOrdersHistoryViewAdapter(Context mcontext, List<CustomerOrders> customerOrdersList) {
        this.mcontext = mcontext;
        this.customerOrdersList = customerOrdersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_orders_history_view_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CustomerOrders customerOrders=customerOrdersList.get(position);
        setUpData(holder,customerOrders,position);
    }

    private void setUpData(ViewHolder holder, CustomerOrders customerOrders, int position) {
        holder.dishname.setText(position+1+"."+customerOrders.getDishName());
        holder.price.setText("Giá tiền: " + customerOrders.getDishPrice()+"đ");
        holder.quantity.setText("× " + customerOrders.getDishQuantity());
        holder.totalprice.setText("Tổng cộng:" + customerOrders.getTotalPrice()+"đ");
    }

    @Override
    public int getItemCount() {
        return customerOrdersList.size();
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
