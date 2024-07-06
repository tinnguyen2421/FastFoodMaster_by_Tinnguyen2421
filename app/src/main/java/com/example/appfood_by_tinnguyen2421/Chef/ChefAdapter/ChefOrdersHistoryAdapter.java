package com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders1;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefOrdersHistoryView;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChefOrdersHistoryAdapter extends RecyclerView.Adapter<ChefOrdersHistoryAdapter.ViewHolder> {
    private Context mContext;
    private List<ChefFinalOrders1> mChefFinalOrders1List;

    public ChefOrdersHistoryAdapter(Context context, List<ChefFinalOrders1> chefFinalOrders1List) {
        this.mChefFinalOrders1List = chefFinalOrders1List;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chef_history_orders_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChefFinalOrders1 chefFinalOrders1 = mChefFinalOrders1List.get(position);
        setUpData(holder,chefFinalOrders1,position);
        setUpListeners(holder,chefFinalOrders1);
    }

    @Override
    public int getItemCount() {
        return mChefFinalOrders1List.size();
    }
    private void setUpData(ViewHolder holder,ChefFinalOrders1 chefFinalOrders1,int postion)
    {
        holder.Numb.setText(String.valueOf(postion) + 1);
        holder.NameCus.setText(chefFinalOrders1.getName());
        holder.Address.setText(chefFinalOrders1.getAddress());
        holder.PhoneNumb.setText(chefFinalOrders1.getMobileNumber());
        holder.Status.setText(chefFinalOrders1.getOrderStatus());
        holder.GrandTotal.setText(chefFinalOrders1.getGrandTotalPrice());
        holder.SendDate.setText(chefFinalOrders1.getDateTime());
        holder.AceptDate.setText(chefFinalOrders1.getAceptDate());
    }
    private void setUpListeners(ViewHolder holder,ChefFinalOrders1 chefFinalOrders1)
    {
        holder.itemView.setOnClickListener(view -> showHistoryView(chefFinalOrders1));
        holder.btnDelete.setOnClickListener(view -> showDeleteConfirmationDialog(chefFinalOrders1));
    }
    private void showHistoryView(ChefFinalOrders1 chefFinalOrders1)
    {
        Intent intent = new Intent(mContext, ChefOrdersHistoryView.class);
        intent.putExtra("RandomUID", chefFinalOrders1.getRandomUID());
        mContext.startActivity(intent);
    }
    private void showDeleteConfirmationDialog(ChefFinalOrders1 chefFinalOrders1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Xóa đơn hàng này khỏi lịch sử ?");
        builder.setPositiveButton("Có", (dialogInterface, i) -> {
            FirebaseDatabase.getInstance().getReference("ChefOrdersHistory")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(chefFinalOrders1.getRandomUID())
                    .removeValue();
            showDeletedOrderMessage();
        });

        builder.setNegativeButton("Không", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeletedOrderMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Đơn hàng đã được xóa");
        builder.setPositiveButton("OK", (dialog, which) -> {});

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView GrandTotal, Status, Address, PhoneNumb, NameCus, Numb, SendDate, AceptDate, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            GrandTotal = itemView.findViewById(R.id.txtGrandTotal);
            Status = itemView.findViewById(R.id.txtStatus);
            Address = itemView.findViewById(R.id.txtAddress);
            PhoneNumb = itemView.findViewById(R.id.txtPhonenumb);
            NameCus = itemView.findViewById(R.id.txtNameCus);
            Numb = itemView.findViewById(R.id.txtNumb);
            SendDate = itemView.findViewById(R.id.txttime);
            AceptDate = itemView.findViewById(R.id.txttime1);
            btnDelete = itemView.findViewById(R.id.BtnDelete);
        }
    }
}
