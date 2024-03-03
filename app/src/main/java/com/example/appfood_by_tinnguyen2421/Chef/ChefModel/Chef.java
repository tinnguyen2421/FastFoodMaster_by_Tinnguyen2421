package com.example.appfood_by_tinnguyen2421.Chef.ChefModel;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class Chef {

    private String City, ConfirmPassword,EmailID,Fname, Address,Lname,Mobile,Password, District, Ward;

    public Chef( String city, String confirmPassword, String emailID, String fname, String address, String lname, String mobile, String password, String district, String ward) {
        ConfirmPassword = confirmPassword;
        EmailID = emailID;
        Fname = fname;
        Lname = lname;
        Mobile = mobile;
        Password = password;
        City = city;
        District = district;
        Ward = ward;
        Address = address;
    }

    public Chef() {
    }


    public String getCity() {
        return City;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public String getEmailID() {
        return EmailID;
    }

    public String getFname() {
        return Fname;
    }

    public String getAddress() {
        return Address;
    }

    public String getLname() {
        return Lname;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getPassword() {
        return Password;
    }

    public String getDistrict() {
        return District;
    }

    public String getWard() {
        return Ward;
    }
}
