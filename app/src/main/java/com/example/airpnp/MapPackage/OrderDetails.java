package com.example.airpnp.MapPackage;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.airpnp.Helper.DataTransferHelper;
import com.example.airpnp.R;
import com.example.airpnp.UserPackage.Order;
import com.example.airpnp.UserPackage.ParkingSpace;

public class OrderDetails extends Fragment {
    DataTransferHelper dataTransferHelper;
    public OrderDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_order_details, container, false);
        DataTransferHelper dataTransferHelper = DataTransferHelper.getInstance();

        ImageButton backBtn = root.findViewById(R.id.back_myZone);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyProfileFragment) requireParentFragment()).popBackStack();
            }
        });
        LinearLayout layout_orderInfo;
        TextView tv_orderInfoTitle;
        layout_orderInfo = root.findViewById(R.id.orderInfo);
        tv_orderInfoTitle = root.findViewById(R.id.tv_orderTitle);

        String parentTAG = dataTransferHelper.getParentTAG();
        ParkingSpace parkingSpace = dataTransferHelper.getParkingSpaceByKey("ParkingSpaceForTransfer");
        if (parentTAG.contentEquals(ParkingSpacesFragment.PARKING_SPACE_FRAGMENT_TAG)){
            layout_orderInfo.setVisibility(View.GONE);
            tv_orderInfoTitle.setVisibility(View.GONE);

            displayParkingSpace(root, parkingSpace);
        }
        else{
            layout_orderInfo.setVisibility(View.VISIBLE);
            tv_orderInfoTitle.setVisibility(View.VISIBLE);
            Order order = dataTransferHelper.getOrderByKey("OrderForTransfer");
            displayParkingSpace(root,parkingSpace);
            displayOrder(root, order);
        }
        return root;
    }

    private  void displayParkingSpace(View root, ParkingSpace parkingSpace){

        TextView tv_name, tv_address, tv_price, tv_status;
        ImageView imageView;

        tv_name = root.findViewById(R.id.tv_parkingName);
        tv_address = root.findViewById(R.id.tv_parkingAddress);
        tv_price = root.findViewById(R.id.tv_price);
        tv_status = root.findViewById(R.id.tv_status);
        imageView = root.findViewById(R.id.image_size_icon);

        tv_name.setText(parkingSpace.getParkingSpaceName());
        tv_address.setText(parkingSpace.getAddress());
        tv_price.setText("Price p/h"+" "+parkingSpace.getPrice()+"₪");

        if (parkingSpace.isActive()){
            tv_status.setText("פעילה");
            tv_status.setTextColor(Color.GREEN);
        }
        else {
            tv_status.setText("לא פעילה");
            tv_status.setTextColor(Color.RED);
        }

        int iconId = -1;
        switch (parkingSpace.getSize()){
            case 0: iconId = R.drawable.car_icon_a; break;
            case 1: iconId = R.drawable.van_icon; break;
            case 2: iconId = R.drawable.truck_icon; break;
        }
        if (iconId != -1) {
            imageView.setImageResource(iconId);
        }
    }

    private void displayOrder(View root, Order order){
        TextView tv_totalPrice, tv_checkIn, tv_checkOut, tv_orderStatus;
        tv_totalPrice = root.findViewById(R.id.tv_totalPrice);
        tv_checkIn = root.findViewById(R.id.tv_checkIn);
        tv_checkOut = root.findViewById(R.id.tv_checkOut);
        tv_orderStatus  = root.findViewById(R.id.tv_orderStatus);

        tv_totalPrice.setText("Total: "+(order.getPrice())+"₪");
        tv_checkIn.setText("CheckIn: "+order.getCheckInTime());
        tv_checkOut.setText("CheckOut: "+order.getCheckOutTime());

        if (order.isActive()){
            tv_orderStatus.setText("פעילה");
            tv_orderStatus.setTextColor(Color.GREEN);
        }
        else {
            tv_orderStatus.setText("לא פעילה");
            tv_orderStatus.setTextColor(Color.RED);
        }
    }
}