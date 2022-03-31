package com.example.CurrencyConverter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.CurrencyConverter.data_manager.DataLocalManager;
import com.example.CurrencyConverter.model.CurrencyList;
import com.example.CurrencyConverter.ui.home.HomeFragment;

import CurrencyConverter.R;
import CurrencyConverter.databinding.ActivityMainBinding;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DataLocalManager.init(getApplicationContext()); // init data manager for app
        CurrencyList.initCurrList(CurrencyList.currList);  // init currency list

        // gọi API khi bật tự động cập nhật
        if(DataLocalManager.getAutoUpdate())
            HomeFragment.currencyAPICall(ActivityMainBinding.inflate(getLayoutInflater()).getRoot());

        if (DataLocalManager.getFirstInstalled())
            startActivity(OnBoardingActivity.class);
        else
            startActivity(MainActivity.class);

    }

    private void startActivity(Class<?> cls){
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

}