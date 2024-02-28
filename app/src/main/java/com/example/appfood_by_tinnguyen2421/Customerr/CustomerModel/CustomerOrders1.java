package com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerOrders1 {

    private String Address,GrandTotalPrice,MobileNumber,Name,Note,RandomUID, OrderDate, PaymentMethod,OrderStatus,ShippingDate;

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public CustomerOrders1(String address, String grandTotalPrice, String mobileNumber, String name, String note, String randomUID, String date, String paymentMethod, String orderStatus,String shippingDate) {
        Address = address;
        GrandTotalPrice = grandTotalPrice;
        MobileNumber = mobileNumber;
        Name = name;
        Note = note;
        RandomUID = randomUID;
        OrderDate =date;
        PaymentMethod =paymentMethod;
        OrderStatus=orderStatus;
        ShippingDate=shippingDate;

    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public CustomerOrders1() {

    }

    public String getShippingDate() {
        return ShippingDate;
    }

    public void setShippingDate(String shippingDate) {
        ShippingDate = shippingDate;
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

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }
}
