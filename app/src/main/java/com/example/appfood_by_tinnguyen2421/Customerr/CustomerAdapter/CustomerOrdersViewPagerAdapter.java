package com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment.CustomerOrderTablayoutFragment;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment.CustomerOrdersFragment.CustomerCartFragment;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment.CustomerOrdersFragment.CustomerOrdersCanceledFragment;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment.CustomerOrdersFragment.CustomerTrackFragment;

public class CustomerOrdersViewPagerAdapter extends FragmentStateAdapter {
    public CustomerOrdersViewPagerAdapter(@NonNull CustomerOrderTablayoutFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:return new CustomerCartFragment();
            case 1:return new CustomerTrackFragment();
            case 2:return new CustomerOrdersCanceledFragment();
            default:return new CustomerCartFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
