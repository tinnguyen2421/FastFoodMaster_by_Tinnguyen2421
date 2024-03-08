package com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appfood_by_tinnguyen2421.Categories;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.CustomerCategoryAdapter;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.CustomerDishesAdapter;
import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.Item;
import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.SlideViewPayer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class CustomerHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SlideViewPayer QuangCao;
    private TextView tvSlogan;
    private RecyclerView recyclerView, rcvhome;
    private List<UpdateDishModel> updateDishModelList;
    private ArrayList<Categories> categoryList;
    private CustomerDishesAdapter adapter;
    private CustomerCategoryAdapter adapterCate;
    private String district, city, ward, address;
    private DatabaseReference dataaa, databaseReference, databaseReference1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText search;
    private TextView addresss;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmenthome, container, false);
        setHasOptionsMenu(true);
        initializeViews(v);
        setupRecyclerViews();
        initializeSlider(v);
        loadData();
        setListeners();
        return v;
    }

    private void initializeViews(View v) {
        tvSlogan = v.findViewById(R.id.txtslogan);
        recyclerView = v.findViewById(R.id.recycle_menu);
        rcvhome = v.findViewById(R.id.rcvhome);
        addresss = v.findViewById(R.id.diaChi);
        search = v.findViewById(R.id.timKiem);
        swipeRefreshLayout = v.findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
    }

    private void setupRecyclerViews() {
        recyclerView.setHasFixedSize(true);
        rcvhome.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        rcvhome.setLayoutManager(linearLayoutManager);
    }

    private void initializeSlider(View v) {
        SliderView sliderView = v.findViewById(R.id.imageSlider);
        QuangCao = new SlideViewPayer(getContext());
        sliderView.setSliderAdapter(QuangCao);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        renewItems(v);
        removeLastItem(v);
        addNewItem(v);
    }
    public void renewItems(View view) {
        List<Item> sliderItemList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Item sliderItem = new Item();
            if (i % 2 == 0) {
                sliderItem.setImageurl("https://images.squarespace-cdn.com/content/v1/53883795e4b016c956b8d243/1597821998048-538UNQI253SYL3KE9NGD/chup-anh-mon-an-breakfast-10.jpg");
            } else {
                sliderItem.setImageurl("https://toplist.vn/images/800px/-790915.jpg");
            }
            sliderItemList.add(sliderItem);
        }
        QuangCao.ViewPagerAdapter(sliderItemList);
    }
    public void removeLastItem(View view) {
        if (QuangCao.getCount() - 1 >= 0)
            QuangCao.deleteitem(QuangCao.getCount() - 1);
    }
    public void addNewItem(View view) {
        Item sliderItem = new Item();
        sliderItem.setImageurl("https://eventusproduction.com/wp-content/uploads/2017/02/fresh-box.jpg");
        QuangCao.addItem(sliderItem);
    }
    private void loadData() {
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
            dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel cust = dataSnapshot.getValue(UserModel.class);
                    district = cust.getDistrict();
                    city = cust.getCity();
                    ward = cust.getWard();
                    address = cust.getAddress();
                    addresss.setText(address + "," + ward + "," + district + "," + city);
                    customerCate();
                    customerDishes();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        });
    }

    private void setListeners() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString();
                search(searchText);
            }
        });
    }

    @Override
    public void onRefresh() {
        customerDishes();
    }

    private void customerCate() {
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Categories").child(city).child(district).child(ward);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Categories categories = snapshot1.getValue(Categories.class);
                        categoryList.add(categories);
                    }
                }
                adapterCate = new CustomerCategoryAdapter(categoryList, getContext());
                rcvhome.setAdapter(adapterCate);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void customerDishes() {
        swipeRefreshLayout.setRefreshing(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(city).child(district).child(ward);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateDishModelList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        UpdateDishModel updateDishModel = snapshot1.getValue(UpdateDishModel.class);
                        updateDishModelList.add(updateDishModel);
                    }
                }
                adapter = new CustomerDishesAdapter(getContext(), updateDishModelList);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void search(final String searchtext) {
        ArrayList<UpdateDishModel> mylist = new ArrayList<>();
        for (UpdateDishModel object : updateDishModelList) {
            if (object.getDishName().toLowerCase().contains(searchtext.toLowerCase())) {
                mylist.add(object);
            }
        }
        adapter = new CustomerDishesAdapter(getContext(), mylist);
        recyclerView.setAdapter(adapter);
    }
}
