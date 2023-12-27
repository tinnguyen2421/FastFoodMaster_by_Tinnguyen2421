package com.example.appfood_by_tinnguyen2421.Chef.ChefModel;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefFinalOrders1 {

    private String Address,GrandTotalPrice,MobileNumber,Name,RandomUID,Status,DateTime,AceptDate,ShippingDate;

    public ChefFinalOrders1(String address, String grandTotalPrice, String mobileNumber, String name, String randomUID, String status, String dateTime,String aceptDate,String shippingDate) {
        Address = address;
        GrandTotalPrice = grandTotalPrice;
        MobileNumber = mobileNumber;
        Name = name;
        RandomUID = randomUID;
        Status = status;
        DateTime=dateTime;
        AceptDate=aceptDate;
        ShippingDate=shippingDate;
    }

    public String getShippingDate() {
        return ShippingDate;
    }

    public void setShippingDate(String shippingDate) {
        ShippingDate = shippingDate;
    }

    public ChefFinalOrders1()
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

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
    public String getAceptDate() {
        return AceptDate;
    }

    public void setAceptDate(String aceptDate) {
        AceptDate = aceptDate;
    }
    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }
}
