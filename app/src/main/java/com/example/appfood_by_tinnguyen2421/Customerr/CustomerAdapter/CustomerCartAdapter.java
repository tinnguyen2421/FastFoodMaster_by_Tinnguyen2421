package com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.OrderDish;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment.CustomerOrdersFragment.CustomerCartFragment;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.Cart;

import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerCartAdapter extends RecyclerView.Adapter<CustomerCartAdapter.ViewHolder> {

    private Context mcontext;
    private List<Cart> cartModellist;
    static int total = 0;

    public CustomerCartAdapter(Context context, List<Cart> cartModellist) {
        this.cartModellist = cartModellist;
        this.mcontext = context;
        total = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cart_placeorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Cart cart = cartModellist.get(position);
        holder.dishname.setText(position+1+"."+cart.getDishName());
        if (cart != null && cart.getDishPrice() != null) {
            String priceString = cart.getDishPrice();
            // Loại bỏ dấu phẩy và khoảng trắng từ chuỗi
            String priceWithoutComma = priceString.replace(",", "").trim();
            try {
                // Chuyển đổi chuỗi thành số và định dạng lại
                double parsedNumber = Double.parseDouble(priceWithoutComma);
                // Sử dụng parsedNumber trong giao diện người dùng với định dạng số
                DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
                String formattedPrice = decimalFormat.format(parsedNumber);
                holder.PriceRs.setText("Giá: " + formattedPrice + "đ");

            } catch (NumberFormatException e) {
                // Xử lý trường hợp không thể chuyển đổi thành số
                e.printStackTrace();
            }
        }
        holder.Qty.setText("× " + cart.getDishQuantity());
        if (cart != null && cart.getTotalPrice() != null) {
            String totalPriceString = cart.getTotalPrice();
            // Loại bỏ dấu phẩy và khoảng trắng từ chuỗi
            String totalPriceWithoutComma = totalPriceString.replace(",", "").trim();
            try {
                // Chuyển đổi chuỗi thành số và định dạng lại
                double parsedTotalPrice = Double.parseDouble(totalPriceWithoutComma);
                // Sử dụng parsedTotalPrice trong giao diện người dùng với định dạng số
                DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
                String formattedTotalPrice = decimalFormat.format(parsedTotalPrice);
                holder.Totalrs.setText("Tổng tiền: " + formattedTotalPrice + "đ");

            } catch (NumberFormatException e) {
                // Xử lý trường hợp không thể chuyển đổi thành số
                e.printStackTrace();
            }
        }
        Picasso.get().load(cart.getImageURL()).into(holder.imageView);
        total += Integer.parseInt(cart.getTotalPrice());
        holder.elegantNumberButton.setNumber(cart.getDishQuantity());
        final int dishprice = Integer.parseInt(cart.getDishPrice());

        holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                int num = newValue;
                int totalprice = num * dishprice;
                if (num != 0) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("DishID", cart.getDishID());
                    hashMap.put("DishName", cart.getDishName());
                    hashMap.put("DishQuantity", String.valueOf(num));
                    hashMap.put("DishPrice", String.valueOf(dishprice));
                    hashMap.put("TotalPrice", String.valueOf(totalprice));
                    hashMap.put("ChefID",cart.getChefID());
                    hashMap.put("ImageURL",cart.getImageURL());
                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getDishID()).setValue(hashMap);
                } else {

                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getDishID()).removeValue();
                }
            }
        });
        String totalString = String.valueOf(total);
        String totalWithoutComma = totalString.replace(",", "").trim();
        try {
            // Chuyển đổi chuỗi thành số và định dạng lại
            double parsedTotal = Double.parseDouble(totalWithoutComma);

            // Sử dụng parsedTotal trong giao diện người dùng với định dạng số
            DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
            String formattedTotal = decimalFormat.format(parsedTotal);

            // Hiển thị giá trị tổng cộng trong giao diện người dùng
            CustomerCartFragment.grandt.setText("Tổng cộng: " + formattedTotal + "đ");
        } catch (NumberFormatException e) {
            // Xử lý trường hợp không thể chuyển đổi thành số
            e.printStackTrace();
        }
        FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal").setValue(String.valueOf(total));

    }

    @Override
    public int getItemCount() {
        return cartModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishname, PriceRs, Qty, Totalrs;
        ElegantNumberButton elegantNumberButton;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishname = itemView.findViewById(R.id.Dishname);
            PriceRs = itemView.findViewById(R.id.pricers);
            Qty = itemView.findViewById(R.id.qty);
            imageView=itemView.findViewById(R.id.ImageView);
            Totalrs = itemView.findViewById(R.id.totalrs);
            elegantNumberButton = itemView.findViewById(R.id.elegantbtn);
        }
    }
}
