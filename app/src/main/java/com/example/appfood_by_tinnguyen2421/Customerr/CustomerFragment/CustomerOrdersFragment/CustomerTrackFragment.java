package com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment.CustomerOrdersFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.CustomerTrackAdapter;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.CustomerOrders1;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerTrackFragment extends Fragment {

    RecyclerView recyclerView;
    private List<CustomerOrders> customerOrdersList;
    private CustomerTrackAdapter adapter;
    DatabaseReference databaseReference;
    TextView grandtotal,orderStatus,orderID,orderDate;
    LinearLayout total,orderInfo,otherInfo1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Theo dõi đơn hàng");
        View v = inflater.inflate(R.layout.fragment_customertrack, null);
        recyclerView = v.findViewById(R.id.recyclefinalorders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        grandtotal = v.findViewById(R.id.GrandTotal);
        total = v.findViewById(R.id.btnn);
        orderInfo=v.findViewById(R.id.OrdersInfo);
        otherInfo1=v.findViewById(R.id.OtherInfo1);
        orderStatus=v.findViewById(R.id.OrderStatus);
        orderID=v.findViewById(R.id.OrdersID);
        orderDate=v.findViewById(R.id.OrderDate);

        customerOrdersList = new ArrayList<>();
        CustomerTrackOrder();

        return v;
    }

    private void CustomerTrackOrder() {

        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerOrdersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Dishes");
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                CustomerOrders customerOrders = snapshot1.getValue(CustomerOrders.class);
                                customerOrdersList.add(customerOrders);
                            }
                            if (customerOrdersList.size() == 0) {
                                total.setVisibility(View.INVISIBLE);
                                orderInfo.setVisibility(View.INVISIBLE);
                                otherInfo1.setVisibility(View.INVISIBLE);
                            } else {
                                total.setVisibility(View.VISIBLE);
                                orderInfo.setVisibility(View.VISIBLE);
                                otherInfo1.setVisibility(View.VISIBLE);
                            }
                            adapter = new CustomerTrackAdapter(getContext(), customerOrdersList);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            CustomerOrders1 customerOrders1 = dataSnapshot.getValue(CustomerOrders1.class);
                            try{
                                int a=customerOrders1.getRandomUID().length()-10;
                                orderID.setText(customerOrders1.getRandomUID().substring(a));
                                orderDate.setText(customerOrders1.getOrderDate());
                                orderStatus.setText(customerOrders1.getOrderStatus());
                                grandtotal.setText(customerOrders1.getGrandTotalPrice()+"đ");
                            }catch (Exception e){
                                Log.d("CustomerTrackFragment", "onDataChange: "+e);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
