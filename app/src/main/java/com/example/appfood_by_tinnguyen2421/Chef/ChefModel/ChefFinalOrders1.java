package com.example.appfood_by_tinnguyen2421.Chef.ChefModel;

//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefFinalOrders1 {

    private String Address,GrandTotalPrice,MobileNumber,Name,Note,RandomUID,AceptDate,SendDate,PaymentMethod,OrderStatus,DeliveryDate,DateTime,ShippingDate;

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getShippingDate() {
        return ShippingDate;
    }

    public void setShippingDate(String shippingDate) {
        ShippingDate = shippingDate;
    }

    public ChefFinalOrders1(String address, String grandTotalPrice, String mobileNumber, String name, String note, String randomUID, String aceptDate, String sendDate, String paymentMethod, String orderStatus, String deliveryDate, String dateTime, String shippingDate) {
        Address = address;
        GrandTotalPrice = grandTotalPrice;
        MobileNumber = mobileNumber;
        Name = name;
        Note = note;
        RandomUID = randomUID;
        AceptDate=aceptDate;
        SendDate=sendDate;
        OrderStatus=orderStatus;
        PaymentMethod=paymentMethod;
        DeliveryDate=deliveryDate;
        DateTime=dateTime;
        ShippingDate=shippingDate;
    }
    public ChefFinalOrders1()
    {

    }


    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }
    public String getOrderStatus() {
        return OrderStatus;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }
    public String getAceptDate() {
        return AceptDate;
    }

    public void setAceptDate(String aceptDate) {
        AceptDate = aceptDate;
    }

    public String getSendDate() {
        return SendDate;
    }

    public void setSendDate(String sendDate) {
        SendDate = sendDate;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
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
