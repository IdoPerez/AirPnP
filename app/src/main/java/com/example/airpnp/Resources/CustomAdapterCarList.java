package com.example.airpnp.Resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.airpnp.R;
import com.example.airpnp.UserPackage.UserCar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapterCarList extends BaseAdapter implements View.OnClickListener {
    Context context;
    ArrayList<UserCar> carItems;
    ItemClickListener itemClickListener;
    Map<View, Integer> items;

    public CustomAdapterCarList(Context context, ArrayList<UserCar> carItems){
        this.context = context;
        this.carItems = carItems;
        items = new HashMap<>();
    }

    @Override
    public int getCount() {
        return carItems.size();
    }

    @Override
    public UserCar getItem(int position) {
        return carItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.car_item_design,parent,false);
        TextView tvCarName = listItem.findViewById(R.id.tv_carName);
        TextView tvCarNumber = listItem.findViewById(R.id.tv_carNumber);
        ImageView sizeIcon = listItem.findViewById(R.id.icon_size);

        UserCar carItem = carItems.get(position);
        tvCarName.setText(carItem.getCarName());
        tvCarNumber.setText(carItem.getCarNumber());
        int size = carItem.getCarSize();
        int id = -1;
        if (size == 0)
            id = R.drawable.car_icon_a;
        else if (size == 1)
            id = (R.drawable.van_icon);
        else
            id = R.drawable.truck_icon;
        sizeIcon.setImageResource(id);
        listItem.setOnClickListener(this);
        items.put(listItem, position);
        return listItem;
    }

    public void setOnItemClickListener(ItemClickListener clickListener){
        itemClickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        int pos = items.get(v);
        if (itemClickListener != null) itemClickListener.onItemClick(v, pos);
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
