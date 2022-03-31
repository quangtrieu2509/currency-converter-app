package com.example.CurrencyConverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.CurrencyConverter.data_manager.DataLocalManager;

import CurrencyConverter.R;

public class OnBoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        findViewById(R.id.get_started_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnBoardingActivity.this, MainActivity.class);

                DataLocalManager.setFirstInstalled(false); //sau khi ấn Get started thì first-installed sẽ false

                startActivity(intent);
                finish();
            }
        });
    }
}