package com.example.appfood_by_tinnguyen2421.DesignPattern.Observer;

public interface PostCateObserver {
    void onCategoryPosted();
    void onCategoryPostFailed(String errorMessage);
}
