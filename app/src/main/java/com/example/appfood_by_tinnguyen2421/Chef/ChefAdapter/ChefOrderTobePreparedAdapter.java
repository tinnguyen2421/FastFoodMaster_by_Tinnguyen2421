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

    private Context context;
    private List<ChefFinalOrders1> chefFinalOrders1List;

    public ChefOrderTobePreparedAdapter(Context context, List<ChefFinalOrders1> chefFinalOrders1List) {
        this.chefFinalOrders1List = chefFinalOrders1List;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chef_ordertobeprepared, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ChefFinalOrders1 chefFinalOrders1 = chefFinalOrders1List.get(position);
        holder.STT.setText("Đơn hàng số:"+position+1);
        holder.Address.setText("Địa chỉ:"+ chefFinalOrders1.getAddress());
        holder.grandtotalprice.setText("Tổng đơn hàng:" + chefFinalOrders1.getGrandTotalPrice()+"đ");
        final String random = chefFinalOrders1.getRandomUID();


        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChefOrdertobePrepareView.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return chefFinalOrders1List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice,STT;
        Button Vieworder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            STT=itemView.findViewById(R.id.order_stt);
            Address = itemView.findViewById(R.id.cust_address);
            grandtotalprice = itemView.findViewById(R.id.Grandtotalprice);
            Vieworder = itemView.findViewById(R.id.View_order);
        }
    }
}
