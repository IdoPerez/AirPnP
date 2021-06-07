package com.example.airpnp.RentPackage;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.LocationPackage.LocationControl;
import com.example.airpnp.R;
import com.example.airpnp.Resources.CustomAdapterSizeMenu;
import com.example.airpnp.Resources.SizeItem;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class RentActivity extends Fragment {
    EditText priceLayout, timeLayout, edName;
    Spinner sizeSpinner;
    AutocompleteSupportFragment autocompleteSupportFragment;
    Button nextStep;
    FirebaseHelper firebaseHelper;
    ParkingSpaceControl parkingSpaceControl;
    LocationControl locationControl;
    private static final String API_KEY = "AIzaSyANALN_AusNyUUV5oD_DE_U2hO__5GEm48";
    private String address, city, stPrice;
    TextView sundayTV, mondayTV, tuesdayTV, wednesdayTV, thursdayTV, fridayTV, saturdayTV;
    TextView sundayTime, mondayTime, tuesdayTime, wednesdayTime, thursdayTime, fridayTime, saturdayTime;
    View dayCheckBoxView;
    int sizePosition = 0;

    ArrayList<SizeItem> sizeItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_rent, container, false);
        priceLayout = root.findViewById(R.id.layoutPrice);
        ///sizeLayout = root.findViewById(R.id.layoutSize);
        timeLayout = root.findViewById(R.id.layoutTime);
        sizeSpinner = root.findViewById(R.id.layoutSize);
        edName = root.findViewById(R.id.ed_parkingSpace_name);
        nextStep = root.findViewById(R.id.nextBtn);

        sizeItems.add(new SizeItem(R.drawable.car_icon_a));
        sizeItems.add(new SizeItem(R.drawable.van_icon));
        sizeItems.add(new SizeItem(R.drawable.truck_icon));

        CustomAdapterSizeMenu adapterSizeMenu = new CustomAdapterSizeMenu(requireContext(), sizeItems);
        //adapterSizeMenu.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapterSizeMenu);

        dayCheckBoxView = root.findViewById(R.id.days_check_box_group);
        sundayTV = dayCheckBoxView.findViewById(R.id.sundayTV);
        mondayTV = dayCheckBoxView.findViewById(R.id.mondayTV);
        tuesdayTV = dayCheckBoxView.findViewById(R.id.tuesdayTV);
        wednesdayTV = dayCheckBoxView.findViewById(R.id.wednesdayTV);
        thursdayTV = dayCheckBoxView.findViewById(R.id.thursdayTV);
        fridayTV = dayCheckBoxView.findViewById(R.id.fridayTV);
        saturdayTV = dayCheckBoxView.findViewById(R.id.saturdayTV);

        sundayTime = dayCheckBoxView.findViewById(R.id.sundayTime);
        mondayTime = dayCheckBoxView.findViewById(R.id.mondayTime);
        tuesdayTime = dayCheckBoxView.findViewById(R.id.tuesdayTime);
        wednesdayTime = dayCheckBoxView.findViewById(R.id.wednesdayTime);
        thursdayTime = dayCheckBoxView.findViewById(R.id.thursdayTime);
        fridayTime = dayCheckBoxView.findViewById(R.id.fridayTime);
        saturdayTime = dayCheckBoxView.findViewById(R.id.saturdayTime);

        locationControl = new LocationControl(requireContext());
        firebaseHelper = new FirebaseHelper();
        parkingSpaceControl = ParkingSpaceControl.getInstance();

        Places.initialize(getActivity().getApplicationContext(), API_KEY);
        PlacesClient placesClient = Places.createClient(requireContext());

         autocompleteSupportFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        assert autocompleteSupportFragment != null;
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);

        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(34.2654333839, 29.5013261988),
                new LatLng(35.8363969256, 33.2774264593))
        );
        autocompleteSupportFragment.setCountries("IL");
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                address = place.getAddress();
                assert address != null;
                Log.v("Place", address);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.v("An eror", status.toString());
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputCheck();
            }
        });
        return root;
    }

    private void inputCheck(){
        String workingHours = timeLayout.getText().toString();
        String stPrice = Objects.requireNonNull(priceLayout.getText().toString().trim());
        String parkingSpaceName = edName.getText().toString();
        double price = 0;

        if (!stPrice.isEmpty()){
             price = Double.parseDouble(stPrice);

            if(price < 0){
                priceLayout.setError("Please set a price");
                priceLayout.requestFocus();
                return;
            }
        }

        if(address.isEmpty()){
            Toast.makeText(requireContext(), "Please enter parking space address", Toast.LENGTH_LONG).show();
            return;
        }

        if(parkingSpaceName.isEmpty()){
            Toast.makeText(requireContext(), "Please enter parking space name", Toast.LENGTH_LONG).show();
            return;
        }
        sizePosition = sizeSpinner.getSelectedItemPosition();
        createParkingSpace(parkingSpaceName,price, address, sizePosition, workingHours);
    }

    private void createParkingSpace(String parkingSpaceName, double price, String address, int sizeNum, String workingHours){
        String[] splitAddress = address.split(",");
        String parkingSpaceCity = splitAddress[1];
        parkingSpaceCity = parkingSpaceCity.replaceAll("\\s+","");
        Log.v("ParkingSpaceCity", parkingSpaceCity);
        String parkingSpaceCountry = splitAddress[2].replaceAll("\\s+","");
        Log.v("parkingSpaceContry", parkingSpaceCountry);
        Log.v("Path",ParkingSpaceControl.parkingSpacesPath+parkingSpaceCity);

        LatLng addressLatlng = locationControl.getLocationFromAddress(address);
        ParkingSpace parkingSpace = new ParkingSpace(parkingSpaceName,
                address,
                parkingSpaceCity,
                parkingSpaceCountry,
                price,
                sizeNum,
                firebaseHelper.getUserUid(),
                addressLatlng.latitude,
                addressLatlng.longitude, workingHours);
        parkingSpace.setActive(true);

        parkingSpaceControl.userParkingSpacesList.add(parkingSpace);
        Log.v("ParkingSpace", parkingSpace.toString());
        firebaseHelper.uploadParkingSpace(parkingSpace,ParkingSpaceControl.parkingSpacesPath);
    }
    private void onCheckBoxClicked(int checkBoxId, TextView checkBoxTV, TextView timeTV){
        CheckBox checkBox = dayCheckBoxView.findViewById(checkBoxId);
        if (checkBox.isChecked()){
            //getResources().getColor(R.color.checkBoxCheckedTextColor, null
            checkBox.setTextColor(getResources().getColor(R.color.checkBoxCheckedTextColor, null));
            checkBoxTV.setVisibility(View.VISIBLE);
            timeTV.setVisibility(View.VISIBLE);
        } else{
            checkBox.setTextColor(Color.BLACK);
            checkBoxTV.setVisibility(View.INVISIBLE);
            timeTV.setVisibility(View.INVISIBLE);
        }
    }
    private void onDayCheckBoxClicked(View view) {
        switch (view.getId()){
            case R.id.sundayCheckBox: {
                onCheckBoxClicked(R.id.sundayCheckBox, sundayTV, sundayTime);
                break;
            }
            case R.id.mondayCheckBox: {
                onCheckBoxClicked(R.id.mondayCheckBox, mondayTV, mondayTime);
                break;
            }
            case R.id.tuesdayCheckBox:{
                onCheckBoxClicked(R.id.tuesdayCheckBox, tuesdayTV, tuesdayTime);
                break;
            }
            case R.id.wednesdayCheckBox:{
                onCheckBoxClicked(R.id.wednesdayCheckBox, wednesdayTV, wednesdayTime);
                break;
            }
            case R.id.thursdayCheckBox:{
                onCheckBoxClicked(R.id.thursdayCheckBox, thursdayTV, thursdayTime);
                break;
            }
            case R.id.fridayCheckBox:{
                onCheckBoxClicked(R.id.fridayCheckBox, fridayTV, fridayTime);
                break;
            }
            case R.id.saturdayCheckBox:{
                onCheckBoxClicked(R.id.saturdayCheckBox, saturdayTV, saturdayTime);
                break;
            }
        }
    }

    //                checkBox = dayCheckBoxView.findViewById(R.id.mondayCheckBox);
    //                if (checkBox.isChecked()){
    //                    //getResources().getColor(R.color.checkBoxCheckedTextColor, null
    //                    checkBox.setTextColor(getResources().getColor(R.color.checkBoxCheckedTextColor, null));
    //                    sundayTV.setVisibility(View.VISIBLE);
    //                    sundayTime.setVisibility(View.VISIBLE);
    //                } else{
    //                    checkBox.setTextColor(Color.BLACK);
    //                    sundayTV.setVisibility(View.INVISIBLE);
    //                    sundayTime.setVisibility(View.INVISIBLE);
    //                }

}