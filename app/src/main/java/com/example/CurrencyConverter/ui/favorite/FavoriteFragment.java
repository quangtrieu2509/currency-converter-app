package com.example.CurrencyConverter.ui.favorite;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CurrencyConverter.data_manager.DataLocalManager;
import com.example.CurrencyConverter.list_config.ChoosingListViewAdapter;
import com.example.CurrencyConverter.list_config.FavListAdapter;
import com.example.CurrencyConverter.model.CurrencyList;
import com.example.CurrencyConverter.model.CurrencyUnit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import CurrencyConverter.R;
import CurrencyConverter.databinding.FragmentFavoriteBinding;

public class FavoriteFragment extends Fragment {
    private FloatingActionButton add_fab;
    private RecyclerView favRecycleView;
    public static TextView emptyListNoti;

    public static ArrayList<CurrencyUnit> favList;
    private ArrayList<CurrencyUnit> choosingFavList;
    private ChoosingListViewAdapter adapter;
    private FavListAdapter adapterForFavList;

    private FragmentFavoriteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapping();
        setUpFavList(root);
        initChoosingFavList();
//        Log.e("choosinglistsize", choosingFavList.size()+"");
        setEmptyNoti();

        add_fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                choosingFavCurrencyDialog(root);
            }
        });

        //drag and drop item
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int positionDragged = dragged.getBindingAdapterPosition();
                int positionTarget = target.getBindingAdapterPosition();

                favListDataSwap(positionDragged, positionTarget);
                Collections.swap(favList, positionDragged, positionTarget);
                adapterForFavList.notifyItemMoved(positionDragged, positionTarget);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) { }
        });
        helper.attachToRecyclerView(favRecycleView);


        return root;
    }

    private void mapping(){
        add_fab = binding.addFab;
        favRecycleView = binding.favListview;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(binding.getRoot().getContext());
            favRecycleView.setLayoutManager(linearLayoutManager);
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(binding.getRoot().getContext(),
                    DividerItemDecoration.VERTICAL);
            favRecycleView.addItemDecoration(itemDecoration);
        emptyListNoti = binding.emptyListNoti;
    }


    @SuppressLint("ResourceAsColor")
    private void choosingFavCurrencyDialog(View view){

        Dialog dialog = new Dialog(view.getContext(), R.style.Dialog);
        dialog.setContentView(R.layout.showed_list_view);
        dialog.setTitle(R.string.fav_dialog_title);

        adapter = new ChoosingListViewAdapter(view.getContext(),
                R.layout.choosing_listview_item, choosingFavList);
//        Log.e("choosinglistsize", choosingFavList.size()+" "+"when dialog appeared");

        EditText txtSearch;
        txtSearch = dialog.findViewById(R.id.search_text);

        Button btnCancel;
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        ListView choosingListView;
        choosingListView = dialog.findViewById(R.id.showed_list_view);
        choosingListView.setAdapter(adapter);

        // resize dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // tạo listener cho search
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s);
                choosingListView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        /* set on item click listener */
        choosingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String addedItem = adapter.getItem(position).getCode();

                DataLocalManager.setFavCurrency(addedItem, LocalDateTime.now().toString());
                adapterForFavList.addItem(addedItem);

                Toast.makeText(view.getContext(), getContext().getString(R.string.added)+ addedItem
                        + getContext().getString(R.string.to_the_fav_list), Toast.LENGTH_SHORT).show();

                adapter.removeItem(addedItem);
//                Log.e("choosinglistsize", choosingFavList.size()+" "+"after remove" );
            }
        });


        // event ấn nút cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("choosinglistsize", choosingFavList.size()+" "+ "when dismiss");
                setEmptyNoti();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void initChoosingFavList(){
        choosingFavList = new ArrayList<>();
        for(Map.Entry<String, CurrencyUnit> set : CurrencyList.currList.entrySet())
            if(DataLocalManager.getFavCurrency(set.getKey()) == null && DataLocalManager.getExchangeRate(set.getKey())!=0)
                choosingFavList.add(set.getValue());

        //sort choosingFavList
        Collections.sort(choosingFavList, new Comparator<CurrencyUnit>() {
            @Override
            public int compare(CurrencyUnit o1, CurrencyUnit o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        });
    }

    private void setUpFavList(View view){
        favList = new ArrayList<>();
        Map<String, ?> allEntries = DataLocalManager.getAllSavedFavList();

        for(Map.Entry<String, ?> entry : allEntries.entrySet()){
//            Log.i("FavList", entry.getKey()+": " + entry.getValue().toString());
            favList.add(CurrencyList.currList.get(entry.getKey()));
        }
//        Log.i("LengFav", favList.size()+"");

        //sort favList theo thời gian được add
        Collections.sort(favList, new Comparator<CurrencyUnit>() {
            @Override
            public int compare(CurrencyUnit o1, CurrencyUnit o2) {
                return DataLocalManager.getFavCurrency(o1.getCode()).
                        compareTo(DataLocalManager.getFavCurrency(o2.getCode()));
            }
        });

        adapterForFavList = new FavListAdapter(favList);
        favRecycleView.setAdapter(adapterForFavList);

    }

    private void favListDataSwap(int positionDragged, int positionTarget){
        String draggedItem = favList.get(positionDragged).getCode();
        String targetItem = favList.get(positionTarget).getCode();

        String tmp = DataLocalManager.getFavCurrency(draggedItem);
        DataLocalManager.setFavCurrency(draggedItem, DataLocalManager.getFavCurrency(targetItem));
        DataLocalManager.setFavCurrency(targetItem, tmp);
    }

    public static void setEmptyNoti(){
        if(favList.size() == 0) emptyListNoti.setVisibility(View.VISIBLE);
        else emptyListNoti.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}