package com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.CustomerOrdersHistoryView;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders1;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CustomerOrdersHistoryAdapter extends RecyclerView.Adapter<CustomerOrdersHistoryAdapter.ViewHolder> {
    private Context context;
    private List<CustomerOrders1> customerOrders1List;
    public CustomerOrdersHistoryAdapter(Context context, List<CustomerOrders1> customerOrders1List ) {
        this.customerOrders1List = customerOrders1List;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.customer_history_orders_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomerOrders1 customerOrders1=customerOrders1List.get(position);
        setUpData(holder,customerOrders1,position);
        setUpListeners(holder,customerOrders1);
    }

    private void setUpListeners(ViewHolder holder, CustomerOrders1 customerOrders1) {
        holder.itemView.setOnClickListener(view ->showOrdersHistoryView(customerOrders1));
        holder.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage("Xóa đơn hàng này khỏi lịch sử ?");
            builder.setPositiveButton("Có", (dialogInterface, i) -> deleteOrders(customerOrders1));
            builder.setNegativeButton("Không", (dialog, which) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    private void deleteOrders(CustomerOrders1 customerOrders1) {
        FirebaseDatabase.getInstance().getReference("CustomerOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(customerOrders1.getRandomUID()).removeValue();
        AlertDialog.Builder food = new AlertDialog.Builder(context);
        food.setMessage("Đơn hàng đã được xóa");
        food.setPositiveButton("OK", (dialog, which) -> {});
        AlertDialog alertt = food.create();
        alertt.show();
    }

    private void showOrdersHistoryView(CustomerOrders1 customerOrders1) {
        Intent intent=new Intent(context, CustomerOrdersHistoryView.class);
        intent.putExtra("RandomUIDD",customerOrders1.getRandomUID());
        context.startActivity(intent);
    }

    private void setUpData(ViewHolder holder, CustomerOrders1 customerOrders1, int position) {
        holder.Numb.setText(String.valueOf(position+1));
        holder.NameCus.setText(customerOrders1.getName());
        holder.Address.setText(customerOrders1.getAddress());
        holder.PhoneNumb.setText(customerOrders1.getMobileNumber());
        holder.GrandTotal.setText(customerOrders1.getGrandTotalPrice());
        holder.SendDate.setText(customerOrders1.getOrderDate());
    }

    @Override
    public int getItemCount() {
        return customerOrders1List.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView GrandTotal,Status,Address,PhoneNumb,NameCus,Numb,SendDate,AceptDate,btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            GrandTotal=itemView.findViewById(R.id.txtGrandTotal);
            Status=itemView.findViewById(R.id.txtStatus);
            Address=itemView.findViewById(R.id.txtAddress);
            PhoneNumb=itemView.findViewById(R.id.txtPhonenumb);
            NameCus=itemView.findViewById(R.id.txtNameCus);
            Numb=itemView.findViewById(R.id.txtNumb);
            SendDate=itemView.findViewById(R.id.txttime);
            AceptDate=itemView.findViewById(R.id.txttime1);
            btnDelete=itemView.findViewById(R.id.BtnDelete);
        }
    }
}
