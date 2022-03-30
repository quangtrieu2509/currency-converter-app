package com.example.CurrencyConverter.data_manager;

import android.content.Context;
import android.content.SharedPreferences;

public class ExchangeRateSharedPrefs {

    private static final String EXCHANGE_RATE_SHARED_PREFS = "EXCHANGE_RATE_SHARED_PREFS";
    private Context mContext;

    public ExchangeRateSharedPrefs(Context mContext){
        this.mContext = mContext;
    }


    public void putFloatValue(String key, float value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(EXCHANGE_RATE_SHARED_PREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloatValue(String key, float defValue){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(EXCHANGE_RATE_SHARED_PREFS,
                Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, defValue);
    }


}
