package com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerOrders {

    private String ChefID, DishID,DishName,DishPrice,DishQuantity,RandomUID,TotalPrice, UserID,ImageURL;

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public CustomerOrders(String chefId, String dishId, String dishName, String dishPrice, String dishQuantity, String randomUID, String totalPrice, String userId, String imageURL) {
        ChefID = chefId;
        DishID = dishId;
        DishName = dishName;
        DishPrice = dishPrice;
        DishQuantity = dishQuantity;
        RandomUID = randomUID;
        TotalPrice = totalPrice;
        UserID = userId;
        ImageURL=imageURL;
    }

    public CustomerOrders()
    {

    }

    public String getChefID() {
        return ChefID;
    }

    public void setChefID(String chefID) {
        ChefID = chefID;
    }

    public String getDishID() {
        return DishID;
    }

    public void setDishID(String dishID) {
        DishID = dishID;
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public String getDishPrice() {
        return DishPrice;
    }

    public void setDishPrice(String dishPrice) {
        DishPrice = dishPrice;
    }

    public String getDishQuantity() {
        return DishQuantity;
    }

    public void setDishQuantity(String dishQuantity) {
        DishQuantity = dishQuantity;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
