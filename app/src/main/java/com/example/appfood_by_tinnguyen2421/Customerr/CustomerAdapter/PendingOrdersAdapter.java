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

import java.text.DecimalFormat;
import java.util.List;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.ViewHolder> {

    private Context context;
    private List<CustomerOrders> customerOrdersList;

    public PendingOrdersAdapter(Context context, List<CustomerOrders> customerOrdersList) {
        this.customerOrdersList = customerOrdersList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_order_dishes, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CustomerOrders customerOrders = customerOrdersList.get(position);
        setUpData(holder,customerOrders,position);

    }

    private void setUpData(ViewHolder holder, CustomerOrders customerOrders, int position) {
        holder.Dishname.setText(position+1+"."+ customerOrders.getDishName());
        if (customerOrders != null && customerOrders.getDishPrice() != null) {
            holder.Price.setText("Giá:"+formatPrice(customerOrders.getDishPrice()));
        }
        holder.Quantity.setText("× " + customerOrders.getDishQuantity());
        if (customerOrders != null && customerOrders.getDishPrice() != null) {
            holder.Totalprice.setText("Tổng tiền:"+formatPrice(customerOrders.getTotalPrice()));
        }
    }

    private String formatPrice(String priceString) {
        if (priceString != null) {
            String priceWithoutComma = priceString.replace(",", "").trim();
            try {
                double parsedNumber = Double.parseDouble(priceWithoutComma);
                DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
                return  decimalFormat.format(parsedNumber) + "đ";
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    @Override
    public int getItemCount() {
        return customerOrdersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Dishname, Price, Quantity, Totalprice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Dishname = itemView.findViewById(R.id.Dishh);
            Price = itemView.findViewById(R.id.pricee);
            Quantity = itemView.findViewById(R.id.qtyy);
            Totalprice = itemView.findViewById(R.id.total);

        }
    }
}
