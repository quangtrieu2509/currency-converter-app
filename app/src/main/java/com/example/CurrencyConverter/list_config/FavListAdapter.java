package com.example.CurrencyConverter.list_config;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.CurrencyConverter.data_manager.DataLocalManager;
import com.example.CurrencyConverter.model.CurrencyList;
import com.example.CurrencyConverter.model.CurrencyUnit;
import com.example.CurrencyConverter.ui.favorite.FavoriteFragment;

import java.util.List;

import CurrencyConverter.R;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.FavListViewHolder>{

    private List<CurrencyUnit> mList;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public FavListAdapter(List<CurrencyUnit> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public FavListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_listview_item, parent, false);
        return new FavListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavListViewHolder holder, int position) {
        CurrencyUnit currencyUnit = mList.get(position);
        if(currencyUnit == null) return;

        viewBinderHelper.bind(holder.swipeRevealLayout, currencyUnit.getCode());
        viewBinderHelper.setOpenOnlyOne(true);
        holder.flagCur.setImageResource(currencyUnit.getFlag());
        holder.txtName.setText(currencyUnit.getName());
        holder.txtCode.setText(currencyUnit.getCode());

        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                //do something
                CurrencyUnit removedItem = mList.get(holder.getBindingAdapterPosition());
                mList.remove(removedItem);
                DataLocalManager.deleteFavCurrency(removedItem.getCode());
                FavoriteFragment.setEmptyNoti();
//                Log.e("size", FavoriteFragment.favList.size()+"");
                Toast.makeText(v.getContext(), v.getContext().getString(R.string.deleted)+
                        removedItem.getCode(), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList != null) return mList.size();
        return 0;
    }

    public class FavListViewHolder extends RecyclerView.ViewHolder {

        private SwipeRevealLayout swipeRevealLayout;
        private RelativeLayout deleteLayout;
        private ImageView flagCur;
        private TextView txtName, txtCode;


        public FavListViewHolder(@NonNull View itemView) {
            super(itemView);

            swipeRevealLayout = itemView.findViewById(R.id.fav_listview_item);
            deleteLayout = itemView.findViewById(R.id.delete_layout);
            flagCur = itemView.findViewById(R.id.flag_of_currency);
            txtName = itemView.findViewById(R.id.name_of_currency);
            txtCode = itemView.findViewById(R.id.code_of_currency);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(String addedItem){
        mList.add(CurrencyList.currList.get(addedItem));
        notifyDataSetChanged();
    }
}
