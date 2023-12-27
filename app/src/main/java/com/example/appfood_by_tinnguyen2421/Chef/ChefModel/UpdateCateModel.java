package com.example.appfood_by_tinnguyen2421.Chef.ChefModel;

public class UpdateCateModel {
    String CateID,tentheloai,mota,image,ChefId,RandomUID;


    public UpdateCateModel()
    {

    }

    public String getCateID() {
        return CateID;
    }

    public void setCateID(String cateID) {
        CateID = cateID;
    }

    public String getTentheloai() {
        return tentheloai;
    }

    public void setTentheloai(String tentheloai) {
        this.tentheloai = tentheloai;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChefId() {
        return ChefId;
    }

    public void setChefId(String chefId) {
        ChefId = chefId;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }
}
