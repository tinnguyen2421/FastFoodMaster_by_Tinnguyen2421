package com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.FoodSupplyDetails;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;

public class Favorite {
    private String idfavorite;
    private String uiduser;
    FoodSupplyDetails idfood;
    boolean checkfavorite;

    public Favorite(String key, String uid, UpdateDishModel updateDishModel, boolean checkfavorite) {
    }

    public Favorite(String idfavorite, String uiduser, FoodSupplyDetails idfood, boolean checkfavorite) {
        this.idfavorite = idfavorite;
        this.uiduser = uiduser;
        this.idfood = idfood;
        this.checkfavorite = checkfavorite;
    }

    public FoodSupplyDetails getIdfood() {
        return idfood;
    }

    public void setIdfood(FoodSupplyDetails idfood) {
        this.idfood = idfood;
    }

    public boolean isCheckfavorite() {
        return checkfavorite;
    }

    public void setCheckfavorite(boolean checkfavorite) {
        this.checkfavorite = checkfavorite;
    }

    public String getIdfavorite() {
        return idfavorite;
    }

    public void setIdfavorite(String idfavorite) {
        this.idfavorite = idfavorite;
    }

    public String getUiduser() {
        return uiduser;
    }

    public void setUiduser(String uiduser) {
        this.uiduser = uiduser;
    }


}
