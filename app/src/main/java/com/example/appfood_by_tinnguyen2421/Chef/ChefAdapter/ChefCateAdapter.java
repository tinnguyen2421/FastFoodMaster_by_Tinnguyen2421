package com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appfood_by_tinnguyen2421.Categories;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefDishes;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.Chef;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.Chef_Update_Cate;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefCateAdapter extends RecyclerView.Adapter<ChefCateAdapter.ViewHolder> {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataaa;
    private Context mcont;
    private List<Categories> categoriesList;
    String State, City, Sub;
    public ChefCateAdapter(Context context, List<Categories> categoriesList) {
        this.categoriesList = categoriesList;
        this.mcont = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcont).inflate(R.layout.chef_cate_update_delete, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Categories categories = categoriesList.get(position);
        holder.dishcate.setText(categories.getTentheloai());
        Glide.with(mcont).load(categories.getImage()).into(holder.imgCate);
        holder.cateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mcont, ChefDishes.class);
                intent.putExtra("matl",categories.getMatheloai());
                mcont.startActivity(intent);
            }
        });
        holder.edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(mcont, Chef_Update_Cate.class);
                intent1.putExtra("mtl",categories.getRandomUID());
                mcont.startActivity(intent1);
            }
        });
        holder.dlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                dataaa = firebaseDatabase.getInstance().getReference("Chef").child(userid);
                dataaa.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Chef chefc = dataSnapshot.getValue(Chef.class);
                        State = chefc.getState();
                        City = chefc.getCity();
                        Sub = chefc.getSuburban();
                        AlertDialog.Builder builder = new AlertDialog.Builder(mcont);
                        builder.setMessage("Bạn có chắc chắn muốn xóa món ăn này");
                        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                firebaseDatabase.getInstance().getReference("Categories").child(State).child(City).child(Sub).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(categories.getRandomUID()).removeValue();

                                AlertDialog.Builder food = new AlertDialog.Builder(mcont);
                                food.setMessage("Thể loại đã được xóa");
                                food.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //mcont.startActivity(new Intent((Context) mcont, ChefFoodPanel_BottomNavigation.class));
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }




    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout cateLayout;
        TextView dishcate;
        TextView edt,dlt;
        ImageView imgCate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cateLayout=itemView.findViewById(R.id.CateLayout);
            dishcate = itemView.findViewById(R.id.dish_cate);
            edt=itemView.findViewById(R.id.editButton);
            dlt=itemView.findViewById(R.id.deleteButton);
            imgCate=itemView.findViewById(R.id.ImageCategory);
        }
    }
}