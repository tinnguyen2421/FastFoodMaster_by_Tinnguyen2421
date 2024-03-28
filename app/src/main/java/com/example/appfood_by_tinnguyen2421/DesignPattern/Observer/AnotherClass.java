package com.example.appfood_by_tinnguyen2421.DesignPattern.Observer;

import android.content.Context;
import android.widget.Toast;

public class AnotherClass implements PostCateObserver {
    private Context mContext;

    public AnotherClass(Context context) {
        mContext = context;
    }

    @Override
    public void onCategoryPosted() {
        // Hiển thị thông báo khi thể loại được đăng thành công
        showToast("Đăng thể loại thành công");
    }

    @Override
    public void onCategoryPostFailed(String errorMessage) {
        // Hiển thị thông báo khi đăng thất bại
        showToast("Đăng thất bại: " + errorMessage);
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
