package com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerPendingOrders1 {

    private String Address,GrandTotalPrice,MobileNumber,Name,Note,Date;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public CustomerPendingOrders1(String address, String grandTotalPrice, String mobileNumber, String name, String note, String date) {
        Address = address;
        GrandTotalPrice = grandTotalPrice;
        MobileNumber = mobileNumber;
        Name = name;
        Note=note;
        Date=date;

    }

    public CustomerPendingOrders1()
    {

    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getGrandTotalPrice() {
        return GrandTotalPrice;
    }

    public void setGrandTotalPrice(String grandTotalPrice) {
        GrandTotalPrice = grandTotalPrice;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
