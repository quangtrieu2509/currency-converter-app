package com.example.CurrencyConverter;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.CurrencyConverter.data_manager.DataLocalManager;
import com.example.CurrencyConverter.model.CurrencyList;
import com.example.CurrencyConverter.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import CurrencyConverter.R;
import CurrencyConverter.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataLocalManager.init(getApplicationContext()); // init data manager for app
        CurrencyList.initCurrList(CurrencyList.currList);  // init currency list

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View root = binding.getRoot();

        // gọi API khi vừa install app hoặc bật tự động cập nhật
        if(DataLocalManager.getFirstInstalled() || DataLocalManager.getAutoUpdate())
            HomeFragment.currencyAPICall(root);

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_favorite, R.id.nav_settings, R.id.nav_about_us, R.id.nav_first_installed)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // Khi vừa install thì set fragment welcome để đợi gọi API xong
        if(DataLocalManager.getFirstInstalled()){
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.mobile_navigation);
            navGraph.setStartDestination(R.id.nav_first_installed);
            navController.setGraph(navGraph);
        }

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}