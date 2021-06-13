package com.example.airpnp.MapPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.R;
import com.example.airpnp.RentPackage.RentActivity;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
/**
 * @author Ido Perez
 * @version 0.1
 * @since 1.6.2021
 */
public class MainActivityBotNav extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    Fragment mapFragment, rentFragment, fragment;
    BottomSheetBehavior bottomSheetQuickSell;
    CoordinatorLayout bottomSheetLayout;

    ArrayList<String> userParkingSpacesNameList;
    ParkingSpaceControl parkingSpaceControl;
    ListView userParkingSpaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bot_nav);

//        userParkingSpacesNameList = new ArrayList<>();
//        parkingSpaceControl = ParkingSpaceControl.getInstance();
//        userParkingSpaceList = findViewById(R.id.botSheetUserParkingSpacesList);
//        setUserParkingSpacesListView();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setSelectedItemId(R.id.around);
        mapFragment = new MapActivity();
        transferFragment(mapFragment);
        rentFragment = new RentActivity();
        final Fragment myZoneFragment = new MyProfileFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if (item.getItemId() == R.id.quick_sell){
//                    bottomSheetQuickSell.setState(BottomSheetBehavior.STATE_EXPANDED);
//                }
//                else{
//                    bottomSheetQuickSell.setState(BottomSheetBehavior.STATE_HIDDEN);
//                }
                switch (item.getItemId()){
                    case R.id.around:{
                        transferFragment(mapFragment);
                    } break;
                    case R.id.createParkingSpace: transferFragment(rentFragment); break;

                    case R.id.item_myZone: transferFragment(myZoneFragment); break;
                }
                return true;
            }
        });

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

    /**
     * transfer fragmens in activity.
     * @param fragment
     */
    private void transferFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    /**
     * transfer fragments not from the activity.
     * @param fragmentClass
     */
    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null)
                .commit();
    }

    /**
     * pop back to the previous fragment in the stack.
     */
    public void popFromStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    /**
     * bottom sheet state control.
     * @param bottomSheetBehavior
     */
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

    private void setUserParkingSpacesListView(){
        for (ParkingSpace parkingSpace:
                parkingSpaceControl.userParkingSpacesList) {
            userParkingSpacesNameList.add(parkingSpace.getAddress());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,
                userParkingSpacesNameList);
        userParkingSpaceList.setAdapter(adapter);
    }
}