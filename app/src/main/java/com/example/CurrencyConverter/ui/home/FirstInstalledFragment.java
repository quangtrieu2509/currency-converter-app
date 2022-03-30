package com.example.CurrencyConverter.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.CurrencyConverter.data_manager.DataLocalManager;
import com.example.CurrencyConverter.network_infor.AppStatus;

import CurrencyConverter.databinding.FirstInstalledLayoutBinding;

public class FirstInstalledFragment extends Fragment {
    private FirstInstalledLayoutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FirstInstalledLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Log.i("WhenSEt", "STT "+ DataLocalManager.getFirstInstalled());
        if(DataLocalManager.getFirstInstalled() && AppStatus.getInstance(binding.getRoot().getContext()).isOnline())
            DataLocalManager.setFirstInstalled(false);
        binding = null;
    }
}