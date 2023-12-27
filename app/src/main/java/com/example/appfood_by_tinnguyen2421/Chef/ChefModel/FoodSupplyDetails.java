package com.example.appfood_by_tinnguyen2421.Chef.ChefModel;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class FoodSupplyDetails {

    public String CateID,Dishes,Price,Description,ImageURL,RandomUID,ChefId,ReducePrice,PercentDecrease,OnSale,AvailableDish;

    public FoodSupplyDetails(String cateID,String dishes,  String price, String description, String imageURL, String randomUID, String chefId,String reducePrice,String percentDecrease,Boolean onSale,Boolean availableDish) {
        CateID=cateID;
        Dishes = dishes;
        Price = price;
        Description = description;
        ImageURL = imageURL;
        RandomUID=randomUID;
        ChefId=chefId;
        ReducePrice=reducePrice;
        PercentDecrease=percentDecrease;
        OnSale= String.valueOf(onSale);
        AvailableDish= String.valueOf(availableDish);

    }


}
