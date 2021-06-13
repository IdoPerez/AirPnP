package com.example.airpnp.Resources;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.airpnp.R;
import com.example.airpnp.UserPackage.Order;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterCardList extends ArrayAdapter<ParkingSpace> {
    private Context mContext;
    private List<ParkingSpace> items;
    //Declare timer
    CountDownTimer cTimer = null;

    public CustomAdapterCardList(@NonNull Context context, ArrayList<ParkingSpace> list) {
        super(context, 0, list);
        mContext = context;
        items = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.card_view_orders,
                    parent,
                    false);
        TextView tvName, tvAddress, tvTime;

        //initialing button
        //Button aboard = (Button) listItem.findViewById(R.id.aboardBtn);
        //aboard.setOnClickListener(cardItem.getAboardOnClick());

        //initialing textviews
        tvAddress = (TextView) listItem.findViewById(R.id.tv_address);
        tvName = listItem.findViewById(R.id.tv_name);
        tvTime = listItem.findViewById(R.id.tv_time);
//        //user = (TextView) listItem.findViewById(R.id.userTextView);
//        //price = (TextView) listItem.findViewById(R.id.currentPrice);
//        time = (TextView) listItem.findViewById(R.id.orderTime);
//        timeLeft = (TextView) listItem.findViewById(R.id.timeLeft);
////        checkIn = (TextView) listItem.findViewById(R.id.checkinTextView);
////        checkOut = (TextView) listItem.findViewById(R.id.checkoutTextView);
        ParkingSpace parkingSpace = items.get(position);
        if (parkingSpace == null)
            return listItem;

        tvAddress.setText(parkingSpace.getAddress());
        tvName.setText(parkingSpace.getParkingSpaceName());
        //startTimer(tvTime);
        return listItem;
    }

    @Override
    public int getPosition(@Nullable ParkingSpace item) {
        return super.getPosition(item);
    }

    //start timer function
//    public void startTimer(final TextView textView) {
//        cTimer = new CountDownTimer(30000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                textView.setText(String.valueOf(millisUntilFinished));
//            }
//            public void onFinish() {
//                textView.setText("Done");
//                cancelTimer();
//            }
//        };
//        cTimer.start();
//    }


    //cancel timer
//    public void cancelTimer() {
//        if(cTimer!=null)
//            cTimer.cancel();
//    }
}
