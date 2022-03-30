package com.example.CurrencyConverter.data_manager;

import android.content.Context;
import android.util.Log;

import java.util.Map;

public class DataLocalManager {
    private static final String AUTO_UPDATE = "AUTO_UPDATE";
    private static final String DECIMAL_NUMBER = "DECIMAL_NUMBER";
    private static final String FIRST_INSTALLED = "FIRST_INSTALLED";
    private static final String CURRENT_SRC_CUR = "CURRENT_SRC_CUR";
    private static final String CURRENT_DES_CUR = "CURRENT_DES_CUR";
    private static final String CONVERTED_NUM = "CONVERTED_NUM";

    private static DataLocalManager instance;
    private ExchangeRateSharedPrefs exchangeRateSharedPrefs;
    private StatusSharedPrefs statusSharedPrefs;
    private FavoriteCurrencyPrefs favoriteCurrencyPrefs;

    public static void init(Context context){
        instance = new DataLocalManager();
        instance.exchangeRateSharedPrefs = new ExchangeRateSharedPrefs(context);
        instance.statusSharedPrefs = new StatusSharedPrefs(context);
        instance.favoriteCurrencyPrefs = new FavoriteCurrencyPrefs(context);
    }

    public static DataLocalManager getInstance(){
        if(instance == null) instance = new DataLocalManager();
        return instance;
    }

    public static void setExchangeRate(String key, float value){
        Log.i("vvv", "setttt "+key);
        DataLocalManager.getInstance().exchangeRateSharedPrefs.putFloatValue(key, value);
    }

    public static float getExchangeRate(String key){
        Log.i("vvv", "get "+ key);
        return DataLocalManager.getInstance().exchangeRateSharedPrefs.getFloatValue(key, 0);
    }

    public static void setAutoUpdate(boolean value){
        DataLocalManager.getInstance().statusSharedPrefs.putBooleanValue(AUTO_UPDATE, value);
    }

    public static boolean getAutoUpdate(){
        return DataLocalManager.getInstance().statusSharedPrefs.getBooleanValue(AUTO_UPDATE, true);
    }

    public static void setDecimalNum(int value){
        DataLocalManager.getInstance().statusSharedPrefs.putIntValue(DECIMAL_NUMBER, value);
    }

    public static int getDecimalNum(){
        return DataLocalManager.getInstance().statusSharedPrefs.getIntValue(DECIMAL_NUMBER);
    }

    public static void setFirstInstalled(boolean value){
        DataLocalManager.getInstance().statusSharedPrefs.putBooleanValue(FIRST_INSTALLED, value);
    }

    public static boolean getFirstInstalled(){
        return DataLocalManager.getInstance().statusSharedPrefs.getBooleanValue(FIRST_INSTALLED, true);
    }

    public static void setCurrentSrcCur(String value){
        DataLocalManager.getInstance().statusSharedPrefs.putStringValue(CURRENT_SRC_CUR, value);
    }

    public static String getCurrentSrcCur(){
        return DataLocalManager.getInstance().statusSharedPrefs.getStringValue(CURRENT_SRC_CUR, "USD");
    }

    public static void setCurrentDesCur(String value){
        DataLocalManager.getInstance().statusSharedPrefs.putStringValue(CURRENT_DES_CUR, value);
    }

    public static String getCurrentDesCur(){
        return DataLocalManager.getInstance().statusSharedPrefs.getStringValue(CURRENT_DES_CUR, "VND");
    }

    public static void setCurrentConvertedNum(String value){
        DataLocalManager.getInstance().statusSharedPrefs.putStringValue(CONVERTED_NUM, value);
    }

    public static String getCurrentConvertedNum(){
        return DataLocalManager.getInstance().statusSharedPrefs.getStringValue(CONVERTED_NUM, "0");
    }

    public static void setFavCurrency(String key, String dateTime){
        DataLocalManager.getInstance().favoriteCurrencyPrefs.putStringValue(key, dateTime);
    }

    public static String getFavCurrency(String key){
        return DataLocalManager.getInstance().favoriteCurrencyPrefs.getStringValue(key);
    }

    public static void deleteFavCurrency(String key){
        DataLocalManager.getInstance().favoriteCurrencyPrefs.removeStringValue(key);
    }

    public static Map<String, ?> getAllSavedFavList(){
        return DataLocalManager.getInstance().favoriteCurrencyPrefs.getAllKey();
    }



}
