package com.example.appfood_by_tinnguyen2421.DesignPattern.FactoryMethod;

import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefRevenue;

public class ChefRevenueFactoryImpl implements ChefRevenueFactory  {

    @Override
    public ChefRevenue createChefRevenue() {
        return new ChefRevenue();
    }
}
