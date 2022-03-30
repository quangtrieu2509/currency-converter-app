package com.example.CurrencyConverter.data_manager;

import android.content.Context;
import android.content.SharedPreferences;

public class StatusSharedPrefs {
    private static final String STATUS_SHARED_PREFS = "STATUS_SHARED_PREFS";
    private Context mContext;


    public StatusSharedPrefs(Context mContext){
        this.mContext = mContext;
    }


    public void putBooleanValue(String key, boolean value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(STATUS_SHARED_PREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key, boolean defValue){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(STATUS_SHARED_PREFS,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defValue);
    }

    public void putIntValue(String key, int value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(STATUS_SHARED_PREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getIntValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(STATUS_SHARED_PREFS,
                Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 2);
    }

//    public void putFloatValue(String key, float value){
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences(STATUS_SHARED_PREFS,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putFloat(key, value);
//        editor.apply();
//    }
//
//    public float getFloatValue(String key){
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences(STATUS_SHARED_PREFS,
//                Context.MODE_PRIVATE);
//        return sharedPreferences.getFloat(key, 1);
//    }

    public void putStringValue(String key, String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(STATUS_SHARED_PREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key, String defValue){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(STATUS_SHARED_PREFS,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defValue);
    }


}
