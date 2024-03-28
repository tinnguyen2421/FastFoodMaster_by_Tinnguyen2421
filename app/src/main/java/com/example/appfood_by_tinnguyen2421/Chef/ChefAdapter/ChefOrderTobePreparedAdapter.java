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

import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefOrdertobePrepareView;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders1;
import com.example.appfood_by_tinnguyen2421.R;

import java.util.List;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefOrderTobePreparedAdapter extends RecyclerView.Adapter<ChefOrderTobePreparedAdapter.ViewHolder> {

    private Context mContext;
    private List<ChefFinalOrders1> mChefFinalOrders1List;

    public ChefOrderTobePreparedAdapter(Context context, List<ChefFinalOrders1> chefFinalOrders1List) {
        this.mContext = context;
        this.mChefFinalOrders1List = chefFinalOrders1List;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chef_ordertobeprepared, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChefFinalOrders1 chefFinalOrders1 = mChefFinalOrders1List.get(position);
        setUpData(holder,chefFinalOrders1,position);
        setUpListener(holder,chefFinalOrders1);
    }
    private void setUpData(ViewHolder holder,ChefFinalOrders1 chefFinalOrders1,int position)
    {
        holder.position.setText("Đơn hàng số: " + (position + 1));
        holder.address.setText("Địa chỉ: " + chefFinalOrders1.getAddress());
        holder.grandtotalprice.setText("Tổng đơn hàng: " + chefFinalOrders1.getGrandTotalPrice() + "đ");
    }
    private void setUpListener(ViewHolder holder,ChefFinalOrders1 chefFinalOrders1)
    {
        holder.vieworder.setOnClickListener(view -> showOrders(chefFinalOrders1.getRandomUID()));
    }
    private void showOrders(String randomUID)
    {
        Intent intent = new Intent(mContext, ChefOrdertobePrepareView.class);
        intent.putExtra("RandomUID", randomUID);
        mContext.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return mChefFinalOrders1List.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView address, grandtotalprice, position;
        Button vieworder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.order_stt);
            address = itemView.findViewById(R.id.cust_address);
            grandtotalprice = itemView.findViewById(R.id.Grandtotalprice);
            vieworder = itemView.findViewById(R.id.View_order);
        }
    }
}
