package com.example.appfood_by_tinnguyen2421.Chef.ChefModel;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class FoodSupplyDetails {

    public String CateID, DishName, DishPrice,Description,ImageURL,RandomUID, ChefID,ReducePrice, DecreasePercent,OnSale,AvailableDish;

    public FoodSupplyDetails(String cateID,String dishName,  String price, String description, String imageURL, String randomUID, String chefId,String reducePrice,String percentDecrease,Boolean onSale,Boolean availableDish) {
        CateID=cateID;
        DishName = dishName;
        DishPrice = price;
        Description = description;
        ImageURL = imageURL;
        RandomUID=randomUID;
        ChefID =chefId;
        ReducePrice=reducePrice;
        DecreasePercent =percentDecrease;
        OnSale= String.valueOf(onSale);
        AvailableDish= String.valueOf(availableDish);

    }


}
