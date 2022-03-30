package com.example.CurrencyConverter.data_manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class FavoriteCurrencyPrefs {

    private static final String FAV_CURRENCY_PREFS = "FAV_CURRENCY_PREFS";
    private Context mContext;

    public FavoriteCurrencyPrefs(Context mContext){
        this.mContext = mContext;
    }


    public void putStringValue(String key, String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FAV_CURRENCY_PREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FAV_CURRENCY_PREFS,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public void removeStringValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FAV_CURRENCY_PREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public Map<String, ?> getAllKey(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FAV_CURRENCY_PREFS,
                Context.MODE_PRIVATE);
        return sharedPreferences.getAll();
    }


}
