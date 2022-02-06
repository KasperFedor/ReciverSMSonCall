package com.example.reciversmsoncall;

import static android.content.Context.ACTIVITY_SERVICE;

import android.content.Intent;

import androidx.core.content.ContextCompat;

public class UserClass {
    static String numbSet = "89525497709"; //Переменная номера для вызова
    public boolean isCall = false;
    public void call(){
        isCall = true;
    }
    public void nCall()
    {
        isCall = false;
    }
}

