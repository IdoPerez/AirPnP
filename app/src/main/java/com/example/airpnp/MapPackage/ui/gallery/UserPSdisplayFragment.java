package com.example.airpnp.MapPackage.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.R;
import com.example.airpnp.RentPackage.RentActivity;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;


public class UserPSdisplayFragment extends Fragment implements AdapterView.OnItemClickListener{

    ListView parkingSpacesListView;
    TextView listHeader;
    private ParkingSpaceControl parkingSpaceControl;
    private ArrayList<String> parkingSpacesNames;
    ArrayAdapter<String> adapter;

    CoordinatorLayout bottomSheetLayout;
    BottomSheetBehavior bottomSheetBehavior;

    TextView tvSize, tvPrice, tvAddress,tvCity;
    Button edit, delete;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        parkingSpacesListView = root.findViewById(R.id.parkingSpacesList);

        bottomSheetLayout = root.findViewById(R.id.bottom_sheet_layout_UPD);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        //UI views inside the bottom sheet and initialize them
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setFitToContents(false);
        bottomSheetBehavior.setHalfExpandedRatio(0.4f);

        tvAddress = root.findViewById(R.id.address);
        tvCity = root.findViewById(R.id.city);
        tvPrice = root.findViewById(R.id.price);
        tvSize = root.findViewById(R.id.size);

        edit = root.findViewById(R.id.editButtonBottomSheet);
        delete = root.findViewById(R.id.deleteButtonBottomSheet);

        bottomSheetCallBack(bottomSheetBehavior);


        listHeader = root.findViewById(R.id.listHeader);
        parkingSpaceControl = ParkingSpaceControl.getInstance();

        parkingSpacesNames = new ArrayList<>();
        for (ParkingSpace parkingSpace:
             parkingSpaceControl.userParkingSpacesList) {
            parkingSpacesNames.add(parkingSpace.getAddress());
        }

        parkingSpacesListView.setOnItemClickListener(this);

        adapter = new ArrayAdapter<String>(requireContext(), R.layout.support_simple_spinner_dropdown_item, parkingSpacesNames);
        parkingSpacesListView.setAdapter(adapter);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(requireActivity(), RentActivity.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete the parking space.
            }
        });

        return root;
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

    private void openBottomSheet(ParkingSpace parkingSpace){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        tvAddress.setText("Address: "+parkingSpace.getAddress());
        tvCity.setText("City: "+parkingSpace.getParkingSpaceCity());
        tvPrice.setText("Price: "+parkingSpace.getPrice());
        tvSize.setText("Size: "+parkingSpace.getSize());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parkingSpaceControl.parkingSpacesList.size() > position){
            ParkingSpace parkingSpace = parkingSpaceControl.userParkingSpacesList.get(position);
            openBottomSheet(parkingSpace);
        }
    }
}
