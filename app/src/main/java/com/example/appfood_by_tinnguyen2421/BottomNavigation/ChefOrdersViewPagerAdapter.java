package com.example.appfood_by_tinnguyen2421.BottomNavigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appfood_by_tinnguyen2421.Chef.ChefFragment.ChefOrdersFragment.ChefOrderTobePreparedFragment;
import com.example.appfood_by_tinnguyen2421.Chef.ChefFragment.ChefOrdersFragment.ChefPendingOrdersFragment;
import com.example.appfood_by_tinnguyen2421.Chef.ChefFragment.ChefOrdersFragment.ChefPreparedOrderFragment;
import com.example.appfood_by_tinnguyen2421.Chef.ChefFragment.ChefOrderTablayoutFragment;

public class ChefOrdersViewPagerAdapter extends FragmentStateAdapter {
    public ChefOrdersViewPagerAdapter(@NonNull ChefOrderTablayoutFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:return new ChefPendingOrdersFragment();
            case 1:return new ChefOrderTobePreparedFragment();
            case 2:return new ChefPreparedOrderFragment();
            default:return new ChefPendingOrdersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
