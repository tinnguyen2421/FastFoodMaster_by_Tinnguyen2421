package com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefVoucher;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class CustomerVoucherAdapter extends RecyclerView.Adapter<CustomerVoucherAdapter.ViewHolder> {
    //May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
    private Context mcont;
    private List<ChefVoucher> chefVoucherList;

    public CustomerVoucherAdapter(Context context, List<ChefVoucher> chefVoucherList) {
        this.chefVoucherList = chefVoucherList;
        this.mcont = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcont).inflate(R.layout.customer_voucher_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChefVoucher chefVoucher = chefVoucherList.get(position);
        setUpData(holder,chefVoucher);
        setUpListeners(holder,chefVoucher);


    }

    private void getVoucher(ChefVoucher chefVoucher)
    {
        String RandomUID = UUID.randomUUID().toString();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("DishUseVoucher", chefVoucher.getDishUseVoucher());
        hashMap.put("EndDate", chefVoucher.getEndDate());
        hashMap.put("StartDate", chefVoucher.getStartDate());
        hashMap.put("VoucherApply", chefVoucher.getVoucherApply());
        hashMap.put("VoucherID", chefVoucher.getVoucherID());
        hashMap.put("VoucherName", chefVoucher.getVoucherName());
        hashMap.put("VoucherValue", chefVoucher.getVoucherValue());
        FirebaseDatabase.getInstance().getReference("CustomerVoucher")
                .child(FirebaseAuth.getInstance().getUid())
                .child(RandomUID)
                .setValue(hashMap).addOnCompleteListener(task -> {

                });
    }
    private void setUpListeners(ViewHolder holder, ChefVoucher chefVoucher) {
        holder.collectVoucher.setOnClickListener(view -> getVoucher(chefVoucher));
    }

    private void setUpData(ViewHolder holder, ChefVoucher chefVoucher) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date date1 = sdf.parse(chefVoucher.getEndDate());
            Date currentDate = new Date();

            if (currentDate.compareTo(date1) < 0) {
                holder.voucherName.setText(chefVoucher.getVoucherName());
                holder.voucherValue.setText("Giảm:"+chefVoucher.getVoucherValue()+"đ");
                holder.voucherDate.setText("HSD:"+chefVoucher.getEndDate());
                holder.dishApply.setText("Món áp dụng:"+chefVoucher.getDishUseVoucher());
            } else {
                holder.VoucherLayoutt.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return chefVoucherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button collectVoucher;
        ImageView imgCate;
        LinearLayout VoucherLayoutt;
        TextView voucherName,voucherValue,voucherDate,dishApply;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            collectVoucher=itemView.findViewById(R.id.CollectVoucher);
            VoucherLayoutt=itemView.findViewById(R.id.VoucherLayout);
            dishApply=itemView.findViewById(R.id.DishApply);
            voucherName=itemView.findViewById(R.id.VoucherName);
            voucherValue=itemView.findViewById(R.id.VoucherValue);
            voucherDate=itemView.findViewById(R.id.VoucherDate);
            imgCate=itemView.findViewById(R.id.ImageCategory);
        }
    }
}