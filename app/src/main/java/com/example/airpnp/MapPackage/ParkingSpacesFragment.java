package com.example.airpnp.MapPackage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.airpnp.Helper.DataTransferHelper;
import com.example.airpnp.R;
import com.example.airpnp.Resources.CustomAdapterCardList;
import com.example.airpnp.UserPackage.Order;
import com.example.airpnp.UserPackage.OrdersControl;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;

import java.util.ArrayList;


public class ParkingSpacesFragment extends Fragment {
    ListView listView;
    ParkingSpaceControl parkingSpaceControl;
    CustomAdapterCardList customAdapterCardList;
    public ParkingSpacesFragment() {
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
        final Fragment fragment = new OrderDetails();
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_parking_spaces, container, false);
        listView = root.findViewById(R.id.listView_parkingSpaces);
        parkingSpaceControl = ParkingSpaceControl.getInstance();
        final DataTransferHelper dataTransferHelper = DataTransferHelper.newInstance();
        customAdapterCardList = new CustomAdapterCardList(requireContext(), parkingSpaceControl.userParkingSpacesList);
        listView.setAdapter(customAdapterCardList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                dataTransferHelper.putOrder(ordersControl.userOrdersList.get(position));
//                dataTransferHelper.putParkingSpace(parkingSpaces.get(position));
                ((MyProfileFragment) getParentFragment()).replaceFragments(fragment);
            }
        });
        return root;
    }
}