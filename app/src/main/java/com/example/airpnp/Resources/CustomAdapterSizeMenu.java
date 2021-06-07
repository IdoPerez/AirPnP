package com.example.airpnp.Resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.airpnp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterSizeMenu extends BaseAdapter {
    private Context mContext;
    private List<SizeItem> sizeIcon;

    public CustomAdapterSizeMenu(@NonNull Context context, ArrayList<SizeItem> list) {
        mContext = context;
        sizeIcon = list;
    }

    @Override
    public int getCount() {
        return sizeIcon.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.custom_item_size_menu,parent,false);

        SizeItem currentItem = sizeIcon.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.sizeItemIcon);
        image.setImageResource(currentItem.getImageDrawable());
        return listItem;
    }
}
//    public CustomAdapterSizeMenu(@NonNull Context context, ArrayList<SizeItem> list) {
//        super(context, 0 , list);
//        mContext = context;
//        sizeItemIcons = list;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//        View listItem = convertView;
//        if(listItem == null)
//            listItem = LayoutInflater.from(mContext).inflate(R.layout.custom_item_size_menu,parent,false);
//
//        SizeItem currentItem = sizeItemIcons.get(position);
//
//        ImageView image = (ImageView)listItem.findViewById(R.id.sizeItemIcon);
//        image.setImageResource(currentItem.getImageDrawable());
//        return listItem;
//    }
//
//    @Override
//    public int getCount() {
//        return super.getCount();
//    }