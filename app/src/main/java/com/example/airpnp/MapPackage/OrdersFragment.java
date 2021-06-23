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

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment {

    public static final String ordersFragmentTAG = "ordersFragment";
    ListView listView;
    OrdersControl ordersControl;
    CustomAdapterCardList customAdapterCardList;

    public OrdersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final Fragment detailsFragment = new OrderDetails();
        // Inflate the layout for this detailsFragment
        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        listView = root.findViewById(R.id.listView_orders);
        ordersControl = OrdersControl.getInstance();

        final DataTransferHelper dataTransferHelper = DataTransferHelper.newInstance();
        ParkingSpaceControl parkingSpaceControl = ParkingSpaceControl.getInstance();
        final ArrayList<ParkingSpace> parkingSpaces = new ArrayList<>();
        for (Order order:
             ordersControl.userOrdersList) {
            parkingSpaces.add(parkingSpaceControl.getParkingSpaceById(order.getParkingSpaceID()));
        }

        customAdapterCardList = new CustomAdapterCardList(requireContext(), parkingSpaces);
        listView.setAdapter(customAdapterCardList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dataTransferHelper.setParentTAG(ordersFragmentTAG);
                dataTransferHelper.setParentTAG(ordersFragmentTAG);
                dataTransferHelper.putOrder("OrderForTransfer", ordersControl.userOrdersList.get(position));
                dataTransferHelper.putParkingSpace("ParkingSpaceForTransfer", parkingSpaces.get(position));
//                dataTransferHelper.putOrder(ordersControl.userOrdersList.get(position));
//                dataTransferHelper.putParkingSpace(parkingSpaces.get(position));
                ((MyProfileFragment) requireParentFragment()).replaceFragments(detailsFragment);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //customAdapterCardList.cancelTimer();
    }
}