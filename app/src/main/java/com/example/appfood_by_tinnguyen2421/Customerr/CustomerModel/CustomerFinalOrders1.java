package com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerFinalOrders1 {

    private String Address,GrandTotalPrice,MobileNumber,Name,Note,RandomUID,Status,Date,AceptDate;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public CustomerFinalOrders1(String address, String grandTotalPrice, String mobileNumber, String name, String note, String randomUID, String status, String date,String aceptDate) {
        Address = address;
        GrandTotalPrice = grandTotalPrice;
        MobileNumber = mobileNumber;
        Name = name;
        Note = note;
        RandomUID = randomUID;
        Status = status;
        Date=date;
        AceptDate=aceptDate;
    }

    public String getAceptDate() {
        return AceptDate;
    }

    public void setAceptDate(String aceptDate) {
        AceptDate = aceptDate;
    }

    public CustomerFinalOrders1()
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
}
