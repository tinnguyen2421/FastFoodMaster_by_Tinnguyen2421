package com.example.appfood_by_tinnguyen2421.Chef.ChefModel;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class UpdateDishModel {



    String CateID, DishName,RandomUID,Description, DishPrice,ImageURL, ChefID,ReducePrice, DecreasePercent,OnSale,AvailableDish;


    public UpdateDishModel()
    {

    }

    public String getAvailableDish() {
        return AvailableDish;
    }

    public void setAvailableDish(String availableDish) {
        AvailableDish = availableDish;
    }

    public String getCateID() {
        return CateID;
    }

    public String getReducePrice() {
        return ReducePrice;
    }

    public void setReducePrice(String reducePrice) {
        ReducePrice = reducePrice;
    }

    public String getDecreasePercent() {
        return DecreasePercent;
    }

    public void setDecreasePercent(String decreasePercent) {
        DecreasePercent = decreasePercent;
    }

    public String getOnSale() {
        return OnSale;
    }

    public void setOnSale(String onSale) {
        OnSale = onSale;
    }

    public void setCateID(String cateID) {
        CateID = cateID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getDishPrice() {
        return DishPrice;
    }

    public void setDishPrice(String dishPrice) {
        DishPrice = dishPrice;
    }

    public String getRandomUID() {

        return RandomUID;
    }

    public void setRandomUID(String randomUID) {

        RandomUID = randomUID;
    }

    public String getDishName()
    {
        return DishName;
    }

    public void setDishName(String dishName) {

        DishName = dishName;
    }

    public String getChefID() {
        return ChefID;
    }

    public void setChefID(String chefID) {
        ChefID = chefID;
    }
}
