package com.example.airpnp.MapPackage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airpnp.Helper.ActionDone;
import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.R;
import com.example.airpnp.Resources.AlertReceiver;
import com.example.airpnp.Resources.CardRecyclerViewAdapter;
import com.example.airpnp.Resources.PaymentDetails;
import com.example.airpnp.UserPackage.Order;
import com.example.airpnp.UserPackage.OrdersControl;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.CalendarConstraints;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class PaymentChoiceFragment extends Fragment {

    RecyclerView parkingTimeRecycler, paymentTypeRecycler;
    TextView tvTitleName, tvAddress, tvTotalPrice;
    ImageButton cancelBtn;
    Button payBtn;
    ParkingSpaceControl parkingSpaceControl;
    OrdersControl ordersControl;
    ArrayList<PaymentDetails> paymentPriceDetails, paymentPayTypeDetails;
    MaterialCardView checkedCardView;
    double parkingSpaceTotalPrice;
    int parkingHours;
    FirebaseHelper firebaseHelper;
    boolean example = false;
    public PaymentChoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pay_choice, container, false);
        parkingSpaceControl = ParkingSpaceControl.getInstance();
        ordersControl = OrdersControl.getInstance();
        firebaseHelper = new FirebaseHelper();

        parkingTimeRecycler = root.findViewById(R.id.parkingTimeRecyclerView);
        paymentTypeRecycler = root.findViewById(R.id.payment_recyclerView);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        parkingTimeRecycler.setLayoutManager(horizontalLayoutManagaer);

        LinearLayoutManager horizontalLayoutManagaer2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        paymentTypeRecycler.setLayoutManager(horizontalLayoutManagaer2);

        tvTitleName = root.findViewById(R.id.titleParkingSpaceName);
        tvAddress = root.findViewById(R.id.payAddress);
        tvTotalPrice = root.findViewById(R.id.tv_paymentPrice);
        payBtn = root.findViewById(R.id.payButton);
        cancelBtn = root.findViewById(R.id.cancelBtn_payment);

        payBtn.setEnabled(false);

        tvTitleName.setText(parkingSpaceControl.parkingSpaceOnBooking.getParkingSpaceName());
        tvAddress.setText(parkingSpaceControl.parkingSpaceOnBooking.getAddress());

        paymentPriceDetails = new ArrayList<>();
        paymentPayTypeDetails = new ArrayList<>();

        final ParkingSpace parkingSpace = parkingSpaceControl.parkingSpaceOnBooking;
        paymentPriceDetails.add(new PaymentDetails(1+"Min example",String.valueOf(parkingSpace.getPrice() ) ) );
        paymentPriceDetails.add(new PaymentDetails(String.valueOf(1)+"hour", String.valueOf(parkingSpaceControl.parkingSpaceOnBooking.getPrice())));
        for (int i = 2; i <=12; i++){
            paymentPriceDetails.add(new PaymentDetails(i +" "+"hours", parkingSpace.getPrice() * i + "₪"));
            paymentPayTypeDetails.add(new PaymentDetails("Random", "random"));
        }

        CardRecyclerViewAdapter timeAdapter = new CardRecyclerViewAdapter(paymentPriceDetails);
        CardRecyclerViewAdapter paymentTypeAdapter = new CardRecyclerViewAdapter(paymentPayTypeDetails);

        parkingTimeRecycler.setAdapter(timeAdapter);

        paymentTypeRecycler.setAdapter(paymentTypeAdapter);
        int inc = 1;
        timeAdapter.setClickListener(new CardRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MaterialCardView cardView = view.findViewById(R.id.paymentCard);
                example = false;
                int pos = position;
                if (cardView.isChecked()){
                    if (position == 0){
                        example = true;
                        pos++;
                    }
                    //inc 1 for real parking hours
                    parkingHours = pos;
                    parkingSpaceTotalPrice = parkingSpace.getPrice()*(pos);
                    payBtn.setEnabled(true);
                }
                tvTotalPrice.setText(parkingSpaceTotalPrice+"₪");
                payBtn.setText("Pay"+" "+parkingSpaceTotalPrice+"₪");
            }
        });
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOrder(parkingSpace);
                //setTimer(parkingHours);
                ((MainActivityBotNav) requireActivity()).popFromStack();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivityBotNav) requireActivity()).popFromStack();
            }
        });

        return root;
    }
    private String convertTime(int y,int m,int d,int hours, int min, int sec){
        String dateFormat;
        dateFormat = y +"/";
        dateFormat += checkTime(m);
        dateFormat += checkTime(d);
        dateFormat += checkTime(hours);
        dateFormat += checkTime(min);
        if (sec > 9)
            dateFormat += sec;
        else
            dateFormat += "0"+sec;
//        if (m > 9)
//            dateFormat += m +"/";
//        else
//            dateFormat += "0"+ m +"/";
//        if (d > 9)
//            dateFormat += d+"/";
//        else
//            dateFormat += "0"+d+"/";
//        if (hours > 9)
        return dateFormat;
    }

    private String checkTime(int num){
        String s;
        if (num > 9)
            s = num + "/";
        else
            s = "0"+num+"/";
        return s;
    }

    Order order;
    private String checkIn, checkOut;
    private void startOrder(ParkingSpace parkingSpace){
        final Calendar c = setTimer();
        order = new Order(parkingSpace.getParkingSpaceID(), parkingSpace.getUserUID(), FirebaseHelper.USER_UID, parkingHours, checkIn , checkOut, true);
        order.setPrice(parkingSpaceTotalPrice);
        assert c != null;
        startAlarm(c);
        //ordersControl.createNewOrder(order);
    }

    private Calendar setTimer(){
        int sec = 0, min = 0;
        ZonedDateTime now;
        ZonedDateTime nextRun;
        Calendar c;
        int hourOfDay = -1;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            now = ZonedDateTime.now(ZoneId.of("Asia/Jerusalem"));
            checkIn = convertTime(now.getYear(), now.getMonth().getValue(), now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond());
            if(now.getHour()+parkingHours > 23) {
                hourOfDay = now.getHour()+parkingHours - 24;
            }
            else
                hourOfDay = now.getHour()+parkingHours;
            if (example){
                sec= now.getSecond()+10;
                min = now.getMinute();
                hourOfDay = now.getHour();
                if (sec > 49){
                   min++;
                   sec = 0;
                }
            }else{
                sec = now.getSecond();
                min = now.getMinute();
            }

            checkOut = convertTime(now.getYear(), now.getMonth().getValue(), now.getDayOfMonth(), hourOfDay, min, sec);
            nextRun = now.withHour(hourOfDay).withMinute(min).withSecond(sec);
            if(now.compareTo(nextRun) > 0)
                nextRun = nextRun.plusDays(1);
            c = Calendar.getInstance();
//            c.set(Calendar.HOUR_OF_DAY, nextRun.getHour());
//            c.set(Calendar.MINUTE, nextRun.getMinute());
//            c.set(Calendar.SECOND, nextRun.getSecond());
            if (example){
                c.set(Calendar.HOUR_OF_DAY, now.getHour());
                c.set(Calendar.MINUTE, now.getMinute());
                c.set(Calendar.SECOND, nextRun.getSecond());
            }else{
                c.set(Calendar.HOUR_OF_DAY, nextRun.getHour());
                c.set(Calendar.MINUTE, nextRun.getMinute());
                c.set(Calendar.SECOND, nextRun.getSecond());
            }
            return c;
        }
        return null;
        //long currentTime = System.currentTimeMillis();
        //c.setTimeInMillis(currentTime);
        //int currentTimeInHours = (int) TimeUnit.MILLISECONDS.convert(currentTime, TimeUnit.HOURS);
    }
    private void startAlarm(final Calendar c){
        final AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(requireContext(), AlertReceiver.class);
        final Context myContext = requireContext();
        firebaseHelper.uploadOrder(order, new ActionDone() {
            @Override
            public void onSuccess() {
                intent.putExtra("orderId",order.getOrderID());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(myContext, 1, intent, 0);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }

            @Override
            public void onFailed() {

            }
        });
    }
    private void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 1, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}