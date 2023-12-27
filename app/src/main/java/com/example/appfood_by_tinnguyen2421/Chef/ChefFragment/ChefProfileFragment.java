package com.example.appfood_by_tinnguyen2421.Chef.ChefFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appfood_by_tinnguyen2421.ChefEditProfile;
import com.example.appfood_by_tinnguyen2421.ChefRevenue;
import com.example.appfood_by_tinnguyen2421.MainMenu;
import com.example.appfood_by_tinnguyen2421.ChefOrdersHistory;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;

//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefProfileFragment extends Fragment {


    RelativeLayout DoanhThu,Account,History,Profile;
    LinearLayout LogOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chef_profile, container, false);
        LogOut = v.findViewById(R.id.logout_layout);
        Account=v.findViewById(R.id.Account_layout);
        DoanhThu=v.findViewById(R.id.Revenue_layout);
        History=v.findViewById(R.id.OrdersHistory);
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ChefEditProfile.class);
                startActivity(intent);
            }
        });
        DoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( getActivity(), ChefRevenue.class);
                startActivity(intent);
            }
        });
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent( getActivity(), ChefOrdersHistory.class);
               startActivity(intent);
            }
        });
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn có muốn đăng xuất ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), MainMenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


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

        return v;
    }
}