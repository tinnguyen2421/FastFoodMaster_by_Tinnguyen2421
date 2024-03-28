package com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefPreparedOrderView;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders1;
import com.example.appfood_by_tinnguyen2421.R;

import java.util.List;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefPreparedOrderAdapter extends RecyclerView.Adapter<ChefPreparedOrderAdapter.ViewHolder> {

    private Context context;
    private List<ChefFinalOrders1> chefFinalOrders1list;

    public ChefPreparedOrderAdapter(Context context, List<ChefFinalOrders1> chefFinalOrders1list) {
        this.chefFinalOrders1list = chefFinalOrders1list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chef_preparedorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChefFinalOrders1 chefFinalOrders1 = chefFinalOrders1list.get(position);
        setUpData(holder,chefFinalOrders1,position);
        setUpListeners(holder,chefFinalOrders1);


    }

    private void setUpListeners(ViewHolder holder, ChefFinalOrders1 chefFinalOrders1) {
        holder.vieworder.setOnClickListener(v -> showPreparedOrders(chefFinalOrders1.getRandomUID()));
    }

    private void showPreparedOrders(String randomUID) {
        Intent intent = new Intent(context, ChefPreparedOrderView.class);
        intent.putExtra("RandomUID", randomUID);
        context.startActivity(intent);
    }

    private void setUpData(ViewHolder holder, ChefFinalOrders1 chefFinalOrders1, int position) {
        holder.position.setText("Số thứ tự:"+position+1);
        holder.address.setText("Địa chỉ:"+chefFinalOrders1.getAddress());
        holder.grandtotalprice.setText("Tổng số tiền: " + chefFinalOrders1.getGrandTotalPrice()+"đ");
    }

    @Override
    public int getItemCount() {
        return chefFinalOrders1list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView address, grandtotalprice, position;
        Button vieworder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            position =itemView.findViewById(R.id.STT);
            address = itemView.findViewById(R.id.customer_address);
            grandtotalprice = itemView.findViewById(R.id.customer_totalprice);
            vieworder = itemView.findViewById(R.id.View);
        }
    }
}
