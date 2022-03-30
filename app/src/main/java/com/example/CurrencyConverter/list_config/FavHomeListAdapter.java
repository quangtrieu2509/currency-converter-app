package com.example.CurrencyConverter.list_config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.CurrencyConverter.model.CurrencyList;
import com.example.CurrencyConverter.model.CurrencyUnit;
import com.example.CurrencyConverter.ui.home.HomeFragment;

import java.util.List;

import CurrencyConverter.R;

public class FavHomeListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<CurrencyUnit> currList;

    public FavHomeListAdapter(Context context, int layout, List<CurrencyUnit> currList) {
        this.context = context;
        this.layout = layout;
        this.currList = currList;
    }

    @Override
    public int getCount() {
        return currList.size();
    }

    @Override
    public CurrencyUnit getItem(int position) {
        return currList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtName, txtCode, txtExchangeResult;
        ImageView flagImg;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder = new ViewHolder();
            holder.txtName = convertView.findViewById(R.id.name_of_currency);
            holder.txtCode = convertView.findViewById(R.id.code_of_currency);
            holder.flagImg = convertView.findViewById(R.id.flag_of_currency);
            holder.txtExchangeResult = convertView.findViewById(R.id.value_of_cal);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        CurrencyUnit currencyUnit = currList.get(position);

        holder.txtName.setText(currencyUnit.getName());
        holder.txtCode.setText(currencyUnit.getCode());
        holder.flagImg.setImageResource(currencyUnit.getFlag());
        String symbol = convertView.getContext().getString(currencyUnit.getSymbol());
        holder.txtExchangeResult.setText
                (HomeFragment.doubleToStringWithComma(HomeFragment.ExRateCalc(currencyUnit.getCode()))+ " " + symbol);

        return convertView;
    }

    public void removeItem(String item){
        currList.remove(CurrencyList.currList.get(item));
        notifyDataSetChanged();
    }

}
