package com.example.appfood_by_tinnguyen2421.DesignPattern.AbstractFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DefaultAppFactory implements AppFactory  {

    @Override
    public ProgressDialog createProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang đăng nhập...");
        return progressDialog;
    }

    @Override
    public Intent createIntent(Context context, Class<?> cls) {
        return new Intent(context, cls);
    }

    @Override
    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
