package com.example.CurrencyConverter.list_config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.CurrencyConverter.model.CurrencyList;
import com.example.CurrencyConverter.model.CurrencyUnit;

import java.util.ArrayList;
import java.util.List;

import CurrencyConverter.R;

public class ChoosingListViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private final List<CurrencyUnit> currList;
    private List<CurrencyUnit> pickedList = new ArrayList<>();

    public ChoosingListViewAdapter(Context context, int layout, List<CurrencyUnit> currList) {
        this.context = context;
        this.layout = layout;
        this.currList = currList;
        pickedList.addAll(this.currList);
    }

    @Override
    public int getCount() {
        return pickedList.size();
    }

    @Override
    public CurrencyUnit getItem(int position) {
        return pickedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtName, txtCode;
        ImageView flagImg;
    }

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
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        CurrencyUnit currencyUnit = pickedList.get(position);

        holder.txtName.setText(currencyUnit.getName());
        holder.txtCode.setText(currencyUnit.getCode());
        holder.flagImg.setImageResource(currencyUnit.getFlag());

        return convertView;
    }

    public void removeItem(String item){
        currList.remove(CurrencyList.currList.get(item));
        pickedList.remove(CurrencyList.currList.get(item));
        notifyDataSetChanged();
    }

    public void filter(CharSequence charText) {
        String text = charText.toString();
        text = text.toLowerCase();
        pickedList.clear();
        if (text.length() == 0) {
            pickedList.addAll(currList);
        } else {
            for (CurrencyUnit picked : currList) {
                if (picked.getCode().toLowerCase().contains(text) ||
                        context.getString(picked.getName()).toLowerCase().contains(text)) {
                    pickedList.add(picked);
                }
            }
        }
        notifyDataSetChanged();
    }

}
