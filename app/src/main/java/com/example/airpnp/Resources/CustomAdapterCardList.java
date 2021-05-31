package com.example.airpnp.Resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.airpnp.R;
import com.example.airpnp.UserPackage.Order;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterCardList extends ArrayAdapter<CardItem> {
    private Context mContext;
    private List<CardItem> cardItems;

    public CustomAdapterCardList(@NonNull Context context, ArrayList<CardItem> list) {
        super(context, 0, list);
        mContext = context;
        cardItems = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.card_view_orders,
                    parent,
                    false);
        CardItem cardItem = cardItems.get(position);

        TextView address, user, price, time, checkIn, checkOut, timeLeft;

        //initialing button
        //Button aboard = (Button) listItem.findViewById(R.id.aboardBtn);
        //aboard.setOnClickListener(cardItem.getAboardOnClick());

        //initialing textviews
        address = (TextView) listItem.findViewById(R.id.titleAddress);
        //user = (TextView) listItem.findViewById(R.id.userTextView);
        price = (TextView) listItem.findViewById(R.id.currentPrice);
        time = (TextView) listItem.findViewById(R.id.orderTime);
        timeLeft = (TextView) listItem.findViewById(R.id.timeLeft);
//        checkIn = (TextView) listItem.findViewById(R.id.checkinTextView);
//        checkOut = (TextView) listItem.findViewById(R.id.checkoutTextView);

        Order itemOrder = cardItem.getOrder();
        ParkingSpaceControl parkingSpaceControl = ParkingSpaceControl.getInstance();
        ParkingSpace parkingSpace = parkingSpaceControl.getParkingSpaceById(itemOrder.getParkingSpaceID());

        address.setText(parkingSpace.getAddress());
        String st = mContext.getResources().getString(R.string.user_parkingSpace);
        //user.setText(st+"שם משתמש");
        price.setText(String.valueOf(itemOrder.getPrice()));
        //time.setText(itemOrder.getTime());
//        checkIn.setText(itemOrder.getCheckInTime());
//        checkOut.setText(itemOrder.getCheckOutTime());
//        checkIn.setText("random time");
//        checkOut.setText("random time1");

        return listItem;
    }
}
