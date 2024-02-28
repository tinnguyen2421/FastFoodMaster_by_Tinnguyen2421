package com.example.appfood_by_tinnguyen2421.DeliveryPerson.DeliveryModel;

//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class DeliveryShipOrders1 {

    private String Address, ChefID, ChefName, GrandTotalPrice, MobileNumber, Name, RandomUID, UserID,Date;

    public DeliveryShipOrders1(String address, String chefId, String chefName, String grandTotalPrice, String mobileNumber, String name, String randomUID, String userId,String date) {
        Address = address;
        ChefID = chefId;
        ChefName = chefName;
        GrandTotalPrice = grandTotalPrice;
        MobileNumber = mobileNumber;
        Name = name;
        RandomUID = randomUID;
        UserID = userId;
        Date=date;
    }

    public DeliveryShipOrders1() {

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getChefID() {
        return ChefID;
    }

    public void setChefID(String chefID) {
        ChefID = chefID;
    }

    public String getChefName() {
        return ChefName;
    }

    public void setChefName(String chefName) {
        ChefName = chefName;
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

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
