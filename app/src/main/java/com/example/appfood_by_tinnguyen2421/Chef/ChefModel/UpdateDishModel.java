package com.example.appfood_by_tinnguyen2421.Chef.ChefModel;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class UpdateDishModel {



    String CateID,Dishes,RandomUID,Description,Price,ImageURL,ChefId,ReducePrice,PercentDecrease,OnSale,AvailableDish;


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

    public String getPercentDecrease() {
        return PercentDecrease;
    }

    public void setPercentDecrease(String percentDecrease) {
        PercentDecrease = percentDecrease;
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getRandomUID() {

        return RandomUID;
    }

    public void setRandomUID(String randomUID) {

        RandomUID = randomUID;
    }

    public String getDishes()
    {
        return Dishes;
    }

    public void setDishes(String dishes) {

        Dishes = dishes;
    }

    public String getChefId() {
        return ChefId;
    }

    public void setChefId(String chefId) {
        ChefId = chefId;
    }
}
