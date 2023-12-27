package com.example.appfood_by_tinnguyen2421;

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

import com.example.appfood_by_tinnguyen2421.BottomNavigation.ChefFoodPanel_BottomNavigation;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.Chef_Update_Delete_Dish;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefFinalOrders1;
import com.example.appfood_by_tinnguyen2421.SendNotification.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ChefOrdersHistoryAdapter extends RecyclerView.Adapter<ChefOrdersHistoryAdapter.ViewHolder> {
    private Context context;
    private List<ChefFinalOrders1> chefFinalOrders1List;
    public ChefOrdersHistoryAdapter(Context context, List<ChefFinalOrders1> chefFinalOrders1List) {
        this.chefFinalOrders1List = chefFinalOrders1List;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.chef_history_orders_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChefFinalOrders1 chefFinalOrders1=chefFinalOrders1List.get(position);
        holder.Numb.setText(String.valueOf(position+1));
        holder.NameCus.setText(chefFinalOrders1.getName());
        holder.Address.setText(chefFinalOrders1.getAddress());
        holder.PhoneNumb.setText(chefFinalOrders1.getMobileNumber());
        holder.Status.setText(chefFinalOrders1.getStatus());
        holder.GrandTotal.setText(chefFinalOrders1.getGrandTotalPrice());
        holder.SendDate.setText(chefFinalOrders1.getDateTime());
        holder.AceptDate.setText(chefFinalOrders1.getAceptDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ChefOrdersHistoryView.class);
                intent.putExtra("RandomUID",chefFinalOrders1.getRandomUID());
                context.startActivity(intent);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("Xóa đơn hàng này khỏi lịch sử ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference("ChefOrdersHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(chefFinalOrders1.getRandomUID()).removeValue();
                        AlertDialog.Builder food = new AlertDialog.Builder(context);
                        food.setMessage("Đơn hàng đã được xóa");
                        food.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //context.startActivity(new Intent(context, ChefOrdersHistory.class));
                            }
                        });
                        AlertDialog alertt = food.create();
                        alertt.show();
                    }

                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chefFinalOrders1List.size();
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
