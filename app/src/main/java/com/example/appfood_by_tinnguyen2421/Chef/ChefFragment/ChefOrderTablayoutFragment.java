package com.example.appfood_by_tinnguyen2421.Chef.ChefFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appfood_by_tinnguyen2421.BottomNavigation.ChefOrdersViewPagerAdapter;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.android.material.tabs.TabLayout;

public class ChefOrderTablayoutFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
     ChefOrdersViewPagerAdapter viewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chef_order_view_pager_adapter, container, false);
        tabLayout=v.findViewById(R.id.Tablayout);
        viewPager2=v.findViewById(R.id.ViewPager);
        viewPagerAdapter=new ChefOrdersViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
        return v;
    }

}