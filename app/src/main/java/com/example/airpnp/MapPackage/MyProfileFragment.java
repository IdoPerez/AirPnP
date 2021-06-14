package com.example.airpnp.MapPackage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.airpnp.R;
import com.google.android.material.tabs.TabLayout;

/**
 * @author Ido Perez
 * @version 0.1
 * @since 3.6.2021
 */
public class MyProfileFragment extends Fragment {
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private final int FRAGMENT_MyParkingSpace_POS = 0;
    private final int FRAGMENT_Orders_POS = 1;

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

        ordersFragment = new OrdersFragment();
        parkingSpacesFragment = new ParkingSpacesFragment();
        tabLayout.selectTab(tabLayout.getTabAt(FRAGMENT_MyParkingSpace_POS));
        replaceFragments(parkingSpacesFragment);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case FRAGMENT_MyParkingSpace_POS:{
                        getChildFragmentManager().beginTransaction().replace(R.id.fragFrame_myProfile, parkingSpacesFragment).commit();
                        //Toast.makeText(requireContext(), "Fragment MyParkingSpace", Toast.LENGTH_SHORT).show();
                    } break;
                    case FRAGMENT_Orders_POS: {
                        getChildFragmentManager().beginTransaction().replace(R.id.fragFrame_myProfile, ordersFragment).commit();
                        //Toast.makeText(requireContext(), "Fragment MyOrders", Toast.LENGTH_SHORT).show();
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

    public void replaceFragments(Fragment fragment){
        getChildFragmentManager().beginTransaction().replace(R.id.fragFrame_myProfile, fragment).addToBackStack(null).commit();
        //Toast.makeText(requireContext(), "Fragment MyParkingSpace", Toast.LENGTH_SHORT).show();
    }

    public void popBackStack(){
        getChildFragmentManager().popBackStack();
    }
}