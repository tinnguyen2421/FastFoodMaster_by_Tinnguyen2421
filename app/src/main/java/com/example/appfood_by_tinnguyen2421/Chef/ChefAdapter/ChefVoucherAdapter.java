package com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefVoucher;
import com.example.appfood_by_tinnguyen2421.R;

import java.util.List;


public class ChefVoucherAdapter extends RecyclerView.Adapter<ChefVoucherAdapter.ViewHolder> {
    //May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
    private Context mcont;
    private List<ChefVoucher> chefVoucherList;

    public ChefVoucherAdapter(Context context, List<ChefVoucher> chefVoucherList) {
        this.chefVoucherList = chefVoucherList;
        this.mcont = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcont).inflate(R.layout.chef_voucher_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChefVoucher chefVoucher = chefVoucherList.get(position);
        holder.voucherName.setText(chefVoucher.getVoucherName());
        holder.voucherValue.setText("Giảm"+chefVoucher.getVoucherValue());
        holder.voucherDate.setText(chefVoucher.getStartDate()+"-"+chefVoucher.getEndDate());
        holder.dishApply.setText(chefVoucher.getDishUseVoucher());
    }


    @Override
    public int getItemCount() {
        return chefVoucherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCate;
        TextView voucherName,voucherValue,voucherDate,dishApply;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishApply=itemView.findViewById(R.id.DishApply);
            voucherName=itemView.findViewById(R.id.VoucherName);
            voucherValue=itemView.findViewById(R.id.VoucherValue);
            voucherDate=itemView.findViewById(R.id.VoucherDate);
            imgCate=itemView.findViewById(R.id.ImageCategory);
        }
    }
}