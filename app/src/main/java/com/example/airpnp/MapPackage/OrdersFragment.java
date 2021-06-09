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
    ListView listView;
    OrdersControl ordersControl;
    CustomAdapterCardList customAdapterCardList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final Fragment fragment = new OrderDetails();
        // Inflate the layout for this fragment
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
//                dataTransferHelper.putOrder(ordersControl.userOrdersList.get(position));
//                dataTransferHelper.putParkingSpace(parkingSpaces.get(position));
                ((MyProfileFragment) getParentFragment()).replaceFragments(fragment);
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