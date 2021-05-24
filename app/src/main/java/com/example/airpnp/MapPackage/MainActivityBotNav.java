package com.example.airpnp.MapPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.airpnp.R;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class MainActivityBotNav extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    Fragment mapFragment, rentFragment, fragment;
    BottomSheetBehavior bottomSheetQuickSell;
    CoordinatorLayout bottomSheetLayout;

    ArrayList<String> userParkingSpacesNameList;
    ParkingSpaceControl parkingSpaceControl;
    ListView userPSlistView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bot_nav);

        userParkingSpacesNameList = new ArrayList<>();
        parkingSpaceControl = ParkingSpaceControl.getInstance();
        userPSlistView = findViewById(R.id.botSheetUserParkingSpacesList);
        setUserParkingSpacesListView();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.around);
        mapFragment = new MapActivity();
        transferFragment(mapFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomSheetLayout = findViewById(R.id.bottom_sheet_quick_sell);
        bottomSheetQuickSell = BottomSheetBehavior.from(bottomSheetLayout);
//
//        CoordinatorLayout coordinatorLayout = getActivity().findViewById(R.id.bottom_sheet_quick_sell);
//        BottomSheetBehavior bottomSheetBehavior= BottomSheetBehavior.from(coordinatorLayout);

        //UI views inside the bottom sheet and initialize them
        bottomSheetQuickSell.setState(BottomSheetBehavior.STATE_HIDDEN);
        //bottomSheetQuickSell.setFitToContents(true);

        bottomSheetCallBack(bottomSheetQuickSell);
    }

    private void transferFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.around: transferFragment(mapFragment); break;

            case R.id.quick_sell: {
                bottomSheetQuickSell.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
        return false;
    }

    public void bottomSheetCallBack(final BottomSheetBehavior bottomSheetBehavior){
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {

                    case BottomSheetBehavior.STATE_COLLAPSED:{

                        break;
                    }
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:{
                    }
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public void quickSellBottomSheet(){

    }

    public void setUserParkingSpacesListView(){
        for (ParkingSpace parkingSpace:
             parkingSpaceControl.userParkingSpacesList) {
            userParkingSpacesNameList.add(parkingSpace.getAddress());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                userParkingSpacesNameList);
        userPSlistView.setAdapter(adapter);
        userPSlistView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}