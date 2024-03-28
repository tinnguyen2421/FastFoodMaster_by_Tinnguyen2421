package com.example.appfood_by_tinnguyen2421.DesignPattern.AbstractFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

public interface AppFactory  {
    ProgressDialog createProgressDialog(Context context);
    Intent createIntent(Context context, Class<?> cls);
    void showToast(Context context, String message);
}
