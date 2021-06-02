package com.example.airpnp.MapPackage;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.airpnp.R;
import com.example.airpnp.Resources.CardRecyclerViewAdapter;
import com.example.airpnp.Resources.PaymentDetails;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class PaymentChoiceFragment extends Fragment {

    RecyclerView parkingTimeRecycler, paymentTypeRecycler;
    TextView tvTitleName, tvAddress, tvTotalPrice;
    ParkingSpaceControl parkingSpaceControl;
    ArrayList<PaymentDetails> paymentPriceDetails, paymentPayTypeDetails;


    public PaymentChoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pay_choice, container, false);

        parkingTimeRecycler = root.findViewById(R.id.parkingTimeRecyclerView);
        paymentTypeRecycler = root.findViewById(R.id.payment_recyclerView);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        parkingTimeRecycler.setLayoutManager(horizontalLayoutManagaer);

        LinearLayoutManager horizontalLayoutManagaer2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        paymentTypeRecycler.setLayoutManager(horizontalLayoutManagaer2);

        tvTitleName = root.findViewById(R.id.titleParkingSpaceName);
        tvAddress = root.findViewById(R.id.payAddress);
        tvTotalPrice = root.findViewById(R.id.tv_paymentPrice);

        parkingSpaceControl = ParkingSpaceControl.getInstance();
        tvTitleName.setText(parkingSpaceControl.parkingSpaceOnBooking.getParkingSpaceName());
        tvAddress.setText(parkingSpaceControl.parkingSpaceOnBooking.getAddress());

        paymentPriceDetails = new ArrayList<>();
        paymentPayTypeDetails = new ArrayList<>();

        paymentPriceDetails.add(new PaymentDetails(String.valueOf(1)+"hour", String.valueOf(parkingSpaceControl.parkingSpaceOnBooking.getPrice())));

        final ParkingSpace parkingSpace = parkingSpaceControl.parkingSpaceOnBooking;
        for (int i = 2; i <=12; i++){
            paymentPriceDetails.add(new PaymentDetails(String.valueOf(i)+" "+"hours", String.valueOf(parkingSpace.getPrice() * i) + "₪"));
            paymentPayTypeDetails.add(new PaymentDetails("Random", "random"));
        }

        CardRecyclerViewAdapter timeAdapter = new CardRecyclerViewAdapter(paymentPriceDetails);
        CardRecyclerViewAdapter paymentTypeAdapter = new CardRecyclerViewAdapter(paymentPayTypeDetails);

        parkingTimeRecycler.setAdapter(timeAdapter);

        paymentTypeRecycler.setAdapter(paymentTypeAdapter);

        timeAdapter.setClickListener(new CardRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                tvTotalPrice.setText(String.valueOf(parkingSpace.getPrice()*position)+"₪");
            }
        });
        return root;
    }
}