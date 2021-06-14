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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.airpnp.R;
import com.example.airpnp.Resources.CardRecyclerViewAdapter;
import com.example.airpnp.Resources.PaymentDetails;
import com.example.airpnp.UserPackage.OrdersControl;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

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
                MaterialCardView cardView = view.findViewById(R.id.paymentCard);
                if (cardView.isChecked()){
                    parkingHours = position+1;
                    parkingSpaceTotalPrice = parkingSpace.getPrice()*(position+1);
                    payBtn.setEnabled(true);
                }
                tvTotalPrice.setText(parkingSpaceTotalPrice+"₪");
                payBtn.setText("Pay"+" "+parkingSpaceTotalPrice+"₪");
            }
        });
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersControl.createNewOrder(parkingSpace, parkingHours);
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
}