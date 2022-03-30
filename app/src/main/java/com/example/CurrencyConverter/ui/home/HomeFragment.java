package com.example.CurrencyConverter.ui.home;

import android.annotation.SuppressLint;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.CurrencyConverter.data_manager.DataLocalManager;
import com.example.CurrencyConverter.list_config.ChoosingListViewAdapter;
import com.example.CurrencyConverter.list_config.FavHomeListAdapter;
import com.example.CurrencyConverter.model.CurrencyList;
import com.example.CurrencyConverter.model.CurrencyUnit;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

import CurrencyConverter.R;
import CurrencyConverter.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private static final String SOURCE = "SOURCE";
    private static final String DESTINATION = "DESTINATION";

    private TextView srcCurName, desCurName;
    @SuppressLint("StaticFieldLeak")
    public static TextView amountOfSrcCur, amountOfDesCur, emptyFavListNoti;
    private ImageView srcCurFlag, desCurFlag;
    private ImageView reverseBtn;
    private RelativeLayout choosingSrcCur, choosingDesCur;
    private ListView favListView;

    private static double convertedNum;
    public static String currentSrcCur, currentDesCur;

    //available currency list
    private ArrayList<CurrencyUnit> choosingList, favHomeList;
    private ChoosingListViewAdapter adapter;
    private FavHomeListAdapter adapterForFavList;

    private FragmentHomeBinding binding;


    @SuppressLint({"ResourceType", "SetTextI18n"})
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapping();

        //init choosingList
        initChoosingList();

        //hồi phục trạng thái cũ
        Number parse = 0;
//        Toast.makeText(root.getContext(), Locale.getDefault().getDisplayLanguage(), Toast.LENGTH_SHORT).show();
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.ENGLISH);
        try {
            parse = numberFormat.parse(DataLocalManager.getCurrentConvertedNum());
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Toast.makeText(root.getContext(), parse.toString(), Toast.LENGTH_SHORT).show();
        convertedNum = parse.doubleValue();
        currentSrcCur= DataLocalManager.getCurrentSrcCur();
        currentDesCur= DataLocalManager.getCurrentDesCur();
        setUpSourceCurrency();


        /*các events onClick*/
        choosingSrcCur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("kkk", "wait or not when click");
                choosingCurrencyDialog(root, SOURCE);
            }
        });

        choosingDesCur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingCurrencyDialog(root, DESTINATION);
            }
        });

        reverseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentDesCur != null && currentSrcCur != null){
                    //swap
                    String tmp = currentDesCur;
                    currentDesCur = currentSrcCur;
                    currentSrcCur = tmp;
                    //reset up
                    setUpSourceCurrency();
                }
            }
        });

        amountOfSrcCur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingUserInput(root);
            }
        });

        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //đổi currentDesCur
                currentDesCur = adapterForFavList.getItem(position).getCode();
                //chỉ cần reset ở desCur, trong desCur đã có reset ở favList
                setUpDestinationCurrency();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void mapping(){
        srcCurName = binding.convertFrom;
        desCurName = binding.convertTo;
        amountOfSrcCur = binding.amountOfConvert;
        amountOfDesCur = binding.resultOfConvert;
        srcCurFlag = binding.flagOfSource;
        desCurFlag = binding.flagOfDestination;
        reverseBtn = binding.reverseIcon;

        choosingSrcCur = binding.relativeLayout1;
        choosingDesCur = binding.relativeLayout2;

        favListView = binding.favHomeListview;
        emptyFavListNoti = binding.emptyFavListNoti;
    }

    public static void currencyAPICall(View view){
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        String apikey = "a3772cbeca94844d1eb98817";
        String url = "https://v6.exchangerate-api.com/v6/"+apikey+"/latest/USD";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject objectData = response.getJSONObject("conversion_rates");
                            for(int i = 0; i< objectData.names().length(); i++){
                                String key = objectData.names().getString(i);
                                //Log.i("AAA", objectData.get(key)+"");
                                if(!CurrencyList.currList.containsKey(key)) Log.i("AAB", key);
                                else {
                                    DataLocalManager.setExchangeRate(key, (float) objectData.getDouble(key));
                                    Log.i("AAA", key);
                                }
                            }
                            Toast.makeText(view.getContext(), R.string.successful_update_noti,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("AAA", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void choosingCurrencyDialog(View view, String check){

        Dialog dialog = new Dialog(view.getContext(), R.style.Dialog);
        dialog.setContentView(R.layout.showed_list_view);
        dialog.setTitle(R.string.home_dialog_title);

        adapter = new ChoosingListViewAdapter(view.getContext(),
                R.layout.choosing_listview_item, choosingList);

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

        // event chọn currency, check = SOURCE hoặc check = DESTINATION
        if(check.equals(SOURCE))
            choosingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    currentSrcCur = adapter.getItem(position).getCode();
                    setUpSourceCurrency();
                    dialog.dismiss();
                }
            });
        else choosingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentDesCur = adapter.getItem(position).getCode();

                setUpDestinationCurrency();
                dialog.dismiss();
            }
        });

        // event ấn nút cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void choosingUserInput(View view){
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.user_input_dialog);

        // resize dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView inputTextView;
        final String[] process = new String[1];
        Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0;
        Button btnDot, btnDeleteText, btnClear, btnCancel, btnOK;
        inputTextView = dialog.findViewById(R.id.input_textview); btnOK = dialog.findViewById(R.id.ok_button);

        btn1 = dialog.findViewById(R.id.button1); btn2 = dialog.findViewById(R.id.button2);
        btn3 = dialog.findViewById(R.id.button3); btn4 = dialog.findViewById(R.id.button4);
        btn5 = dialog.findViewById(R.id.button5); btn6 = dialog.findViewById(R.id.button6);
        btn7 = dialog.findViewById(R.id.button7); btn8 = dialog.findViewById(R.id.button8);
        btn9 = dialog.findViewById(R.id.button9); btn0 = dialog.findViewById(R.id.button0);
        btnDot = dialog.findViewById(R.id.buttonDot); btnDeleteText = dialog.findViewById(R.id.delete_button);
        btnCancel = dialog.findViewById(R.id.cancel_button); btnClear = dialog.findViewById(R.id.clear_button);
        //lấy current converted number cho vào inputTextview
        inputTextView.setText(doubleToStringNormal(convertedNum));
        process[0] = inputTextView.getText().toString();

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTextView.setText("0");
                process[0] = inputTextView.getText().toString();
            }
        });

        btnDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process[0] = inputTextView.getText().toString();
                int size = process[0].length();
                if(size == 1) process[0] = "0";
                else{
                    process[0] = process[0].substring(0, size-1);
                }
                inputTextView.setText(process[0]);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process[0] = inputTextView.getText().toString();
//                Log.i("xxx", process[0]);
                convertedNum = Double.parseDouble(process[0]);
                setUpSourceCurrency();
                dialog.dismiss();
            }
        });

        btn0.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(!inputTextView.getText().toString().equals("0")){
                    process[0] = inputTextView.getText().toString();
                    inputTextView.setText(process[0] +"0");

                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(inputTextView.getText().toString().equals("0"))
                    inputTextView.setText("1");
                else{
                    process[0] = inputTextView.getText().toString();
                    inputTextView.setText(process[0] +"1");
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(inputTextView.getText().toString().equals("0"))
                    inputTextView.setText("2");
                else{
                    process[0] = inputTextView.getText().toString();
                    inputTextView.setText(process[0] +"2");
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(inputTextView.getText().toString().equals("0"))
                    inputTextView.setText("3");
                else{
                    process[0] = inputTextView.getText().toString();
                    inputTextView.setText(process[0] +"3");
                }
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(inputTextView.getText().toString().equals("0"))
                    inputTextView.setText("4");
                else{
                    process[0] = inputTextView.getText().toString();
                    inputTextView.setText(process[0] +"4");
                }
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(inputTextView.getText().toString().equals("0"))
                    inputTextView.setText("5");
                else{
                    process[0] = inputTextView.getText().toString();
                    inputTextView.setText(process[0] +"5");
                }
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(inputTextView.getText().toString().equals("0"))
                    inputTextView.setText("6");
                else{
                    process[0] = inputTextView.getText().toString();
                    inputTextView.setText(process[0] +"6");
                }
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(inputTextView.getText().toString().equals("0"))
                    inputTextView.setText("7");
                else{
                    process[0] = inputTextView.getText().toString();
                    inputTextView.setText(process[0] +"7");
                }
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(inputTextView.getText().toString().equals("0"))
                    inputTextView.setText("8");
                else{
                    process[0] = inputTextView.getText().toString();
                    inputTextView.setText(process[0] +"8");
                }
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(inputTextView.getText().toString().equals("0"))
                    inputTextView.setText("9");
                else{
                    process[0] = inputTextView.getText().toString();
                    inputTextView.setText(process[0] +"9");
                }
            }
        });

        btnDot.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                process[0] = inputTextView.getText().toString();
                if(!process[0].contains(".")) inputTextView.setText(process[0] +".");

            }
        });


        dialog.show();
    }

    public static double ExRateCalc(String desCur){
        if(currentSrcCur == null || currentDesCur == null ||
                DataLocalManager.getExchangeRate(currentSrcCur) == 0) return 0;

        double des = round(DataLocalManager.getExchangeRate(desCur), 4);
        double src = round(DataLocalManager.getExchangeRate(currentSrcCur), 4);
        return round(convertedNum * des / src, DataLocalManager.getDecimalNum());
    }

    //làm tròn cho kết quả của desCur
    public static double round(double value, int place){
        if(place < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(place, RoundingMode.HALF_UP);
        return bd.doubleValue();

    }

    //double convert sang string không chứa ký hiệu
    public static String doubleToStringWithComma(double value){
        BigDecimal bd = BigDecimal.valueOf(value);

        //format nhóm 3 chữ số : 123,456.789 = 123456.789
        DecimalFormat numberFormat = new DecimalFormat("#.################");
        numberFormat.setGroupingUsed(true);
        numberFormat.setGroupingSize(3);

        return numberFormat.format(Double.parseDouble(bd.stripTrailingZeros().toPlainString()));
    }

    public String doubleToStringNormal(double value){
        BigDecimal bd = BigDecimal.valueOf(value);
        return bd.stripTrailingZeros().toPlainString();
    }

    @SuppressLint("SetTextI18n")
    private void setUpSourceCurrency(){
        CurrencyUnit currencyUnit = CurrencyList.currList.get(currentSrcCur);

        srcCurName.setText(currencyUnit.getName());
        srcCurFlag.setImageResource(currencyUnit.getFlag());
        String symbol = getContext().getString(currencyUnit.getSymbol());
        amountOfSrcCur.setText(doubleToStringWithComma(convertedNum)+ " " + symbol);

        DataLocalManager.setCurrentSrcCur(currentSrcCur);
        DataLocalManager.setCurrentConvertedNum(convertedNum+"");

        // thay đổi srcCur thì thay đổi luôn cả giá trị ở desCur
        setUpDestinationCurrency();
    }

    @SuppressLint("SetTextI18n")
    private void setUpDestinationCurrency(){
        if(currentDesCur != null){
            CurrencyUnit currencyUnit = CurrencyList.currList.get(currentDesCur);

            desCurName.setText(currencyUnit.getName());
            desCurFlag.setImageResource(currencyUnit.getFlag());
            String symbol = getContext().getString(currencyUnit.getSymbol());
            amountOfDesCur.setText(doubleToStringWithComma(ExRateCalc(currentDesCur))+ " " + symbol);

            DataLocalManager.setCurrentDesCur(currentDesCur);

            // thay đổi srcCur hay desCur thì thay đổi luôn cả giá trị ở favList
            setUpFavList();
        }
    }

    private void initChoosingList(){
        choosingList = new ArrayList<>();
        for(Map.Entry<String, CurrencyUnit> set : CurrencyList.currList.entrySet())
            if(DataLocalManager.getExchangeRate(set.getKey()) != 0){
                Log.i("EXE", set.getKey()+DataLocalManager.getExchangeRate(set.getKey()));
                choosingList.add(set.getValue());
            }

        //sort choosingList
        Collections.sort(choosingList, new Comparator<CurrencyUnit>() {
            @Override
            public int compare(CurrencyUnit o1, CurrencyUnit o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        });
    }

    private void setUpFavList(){
        favHomeList = new ArrayList<>();
        Map<String, ?> allEntries = DataLocalManager.getAllSavedFavList();

        for(Map.Entry<String, ?> entry : allEntries.entrySet()){
            Log.i("FavList", entry.getKey()+": " + entry.getValue().toString());
            favHomeList.add(CurrencyList.currList.get(entry.getKey()));
        }
        Log.i("LengFav", favHomeList.size()+"");

        //sort favList theo thời gian được add
        Collections.sort(favHomeList, new Comparator<CurrencyUnit>() {
            @Override
            public int compare(CurrencyUnit o1, CurrencyUnit o2) {
                return DataLocalManager.getFavCurrency(o1.getCode()).
                        compareTo(DataLocalManager.getFavCurrency(o2.getCode()));
            }
        });

        adapterForFavList = new FavHomeListAdapter(binding.getRoot().getContext(),
                R.layout.favhome_listview_item, favHomeList);
        favListView.setAdapter(adapterForFavList);

        //nếu currentDesCur là tiền tệ trong FavList thì không xuất hiện nữa
        if(favHomeList.contains(CurrencyList.currList.get(currentDesCur)))
            adapterForFavList.removeItem(currentDesCur);

        if(favHomeList.size() == 0) emptyFavListNoti.setVisibility(View.VISIBLE);
        else emptyFavListNoti.setVisibility(View.INVISIBLE);

    }

}