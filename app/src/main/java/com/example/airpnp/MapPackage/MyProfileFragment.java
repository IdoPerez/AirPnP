package com.example.airpnp.MapPackage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.airpnp.R;
import com.google.android.material.tabs.TabLayout;

public class MyProfileFragment extends Fragment {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private final int FRAGMENT_ACTIVITY_POS = 0;
    private final int FRAGMENT_MyParkingSpace_POS = 1;
    private final int FRAGMENT_Orders_POS = 2;

    Fragment userActivityFragment, ordersFragment, parkingSpacesFragment;
    public MyProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        tabLayout = view.findViewById(R.id.tabLayout);

        userActivityFragment = new UserActivityFragment();
        ordersFragment = new OrdersFragment();
        parkingSpacesFragment = new ParkingSpacesFragment();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case FRAGMENT_ACTIVITY_POS:{
                        Toast.makeText(requireContext(), "Fragment activity", Toast.LENGTH_SHORT).show();
                    } break;
                    case FRAGMENT_MyParkingSpace_POS:{
                        Toast.makeText(requireContext(), "Fragment MyParkingSpace", Toast.LENGTH_SHORT).show();
                    } break;
                    case FRAGMENT_Orders_POS: {
                        Toast.makeText(requireContext(), "Fragment MyOrders", Toast.LENGTH_SHORT).show();
                    } break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
}