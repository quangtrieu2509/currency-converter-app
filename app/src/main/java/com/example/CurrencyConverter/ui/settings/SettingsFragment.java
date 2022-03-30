package com.example.CurrencyConverter.ui.settings;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.CurrencyConverter.data_manager.DataLocalManager;
import com.example.CurrencyConverter.model.CurrencyList;
import com.example.CurrencyConverter.model.CurrencyUnit;
import com.example.CurrencyConverter.network_infor.AppStatus;
import com.example.CurrencyConverter.ui.home.HomeFragment;

import java.lang.reflect.Field;

import CurrencyConverter.R;
import CurrencyConverter.databinding.FragmentSettingsBinding;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class SettingsFragment extends Fragment {
    private RelativeLayout networkArea, appearanceArea;
    private TextView decimalNumStat, updateStat;
    private Switch updateSwitch;

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapping();

        setDecimalNumberStatus();
        setAutomaticUpdateStat();

        appearanceArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decimalChoosingDialog(root);
            }
        });

        networkArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                automaticUpdateOnClick();
            }
        });

        updateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                automaticUpdateOnClick();
            }
        });

        return root;
    }

    private void automaticUpdateOnClick() {
        boolean checkedItem = DataLocalManager.getAutoUpdate();
        if(checkedItem){
            DataLocalManager.setAutoUpdate(false);
            updateSwitch.setChecked(false);
            updateStat.setText(R.string.automatic_update_disabled);
        }
        else{
            DataLocalManager.setAutoUpdate(true);
            updateSwitch.setChecked(true);
            updateStat.setText(R.string.automatic_update_enabled);

            if(AppStatus.getInstance(binding.getRoot().getContext()).isOnline()){
                HomeFragment.currencyAPICall(binding.getRoot());
                Toast.makeText(binding.getRoot().getContext(), R.string.waiting_update_noti,Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(binding.getRoot().getContext(), R.string.fail_update_noti,Toast.LENGTH_LONG).show();
            }

        }
    }

    private void setAutomaticUpdateStat(){
        boolean checkedItem = DataLocalManager.getAutoUpdate();
        if(checkedItem){
            updateSwitch.setChecked(true);
            updateStat.setText(R.string.automatic_update_enabled);
        }
        else{
            updateSwitch.setChecked(false);
            updateStat.setText(R.string.automatic_update_disabled);
        }
    }


    private void decimalChoosingDialog(View root) {
        Dialog dialog = new Dialog(root.getContext(), R.style.Dialog);
        dialog.setContentView(R.layout.decimal_number_choosing);
        dialog.setTitle(R.string.decimal_number);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RadioButton btn1, btn2, btn3, btn4;
        Button cancelBtn;

        cancelBtn = dialog.findViewById(R.id.btn_cancel);
        btn1 = dialog.findViewById(R.id.one_decNum);
        btn2 = dialog.findViewById(R.id.two_decNum);
        btn3 = dialog.findViewById(R.id.three_decNum);
        btn4 = dialog.findViewById(R.id.four_decNum);
        int chosenItem = DataLocalManager.getDecimalNum();
        switch (chosenItem){
            case 2: btn2.setChecked(true); break;
            case 3: btn3.setChecked(true); break;
            case 1: btn1.setChecked(true); break;
            case 4: btn4.setChecked(true); break;
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLocalManager.setDecimalNum(1);
                setDecimalNumberStatus();
                resetResultOfDesCur();
                dialog.dismiss();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLocalManager.setDecimalNum(2);
                setDecimalNumberStatus();
                resetResultOfDesCur();
                dialog.dismiss();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLocalManager.setDecimalNum(3);
                setDecimalNumberStatus();
                resetResultOfDesCur();
                dialog.dismiss();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLocalManager.setDecimalNum(4);
                setDecimalNumberStatus();
                resetResultOfDesCur();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void mapping() {
        networkArea = binding.networkRelativeLayout;
        appearanceArea =binding.appearanceRelativeLayout;
        decimalNumStat = binding.decimalNumberStatus;
        updateStat = binding.updateStatus;
        updateSwitch = binding.automaticUpdateSwitch;
    }

    //lấy id bằng resourceName
    private int getId(String resourceName) {
        try {
            Field idField = R.string.class.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "+resourceName+" / "+ R.string.class, e);
        }
    }


    private void setDecimalNumberStatus() {
        String resourceName = "decimal_number_" + DataLocalManager.getDecimalNum();
        int resId = getId(resourceName);
        decimalNumStat.setText(resId);
    }

    @SuppressLint("SetTextI18n")
    public void resetResultOfDesCur(){
        String currentDesCur = HomeFragment.currentDesCur;
        CurrencyUnit currencyUnit = CurrencyList.currList.get(currentDesCur);
        String symbol = getContext().getString(currencyUnit.getSymbol());
        HomeFragment.amountOfDesCur.setText(HomeFragment.doubleToStringWithComma(HomeFragment.ExRateCalc(currentDesCur))+ " " + symbol);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}