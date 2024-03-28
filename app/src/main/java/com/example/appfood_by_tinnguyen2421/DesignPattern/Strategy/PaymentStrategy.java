package com.example.appfood_by_tinnguyen2421.DesignPattern.Strategy;

import com.example.appfood_by_tinnguyen2421.Account.UserModel;

public interface PaymentStrategy {
    void processPayment(String grandTotal, String randomUID, String address, String addNote, UserModel userModel);
}
