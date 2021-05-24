package com.example.airpnp.RentPackage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airpnp.AuthPackage.Authentication;
import com.example.airpnp.AuthPackage.RegisterUser;
import com.example.airpnp.ContactUser.SmsSender;
import com.example.airpnp.MapPackage.MapActivity;
import com.example.airpnp.R;
import com.example.airpnp.Resources.CustomAdapterSizeMenu;
import com.example.airpnp.Resources.SizeItem;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class RentActivity extends AppCompatActivity {
    TextInputLayout priceLayout, sizeLayout;
    AutocompleteSupportFragment autocompleteSupportFragment;
    AutoCompleteTextView sizeTextView;
    private RentControl rentControl;
    private static final String API_KEY = "AIzaSyANALN_AusNyUUV5oD_DE_U2hO__5GEm48";
    private String address, city, stPrice;
    TextView sundayTV, mondayTV, tuesdayTV, wednesdayTV, thursdayTV, fridayTV, saturdayTV;
    TextView sundayTime, mondayTime, tuesdayTime, wednesdayTime, thursdayTime, fridayTime, saturdayTime;
    View dayCheckBoxView;

    ArrayList<SizeItem> sizeItems = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        priceLayout = findViewById(R.id.layoutPrice);
        sizeLayout = findViewById(R.id.layoutSize);
//        sizeTextView = findViewById(R.id.sizeTextView);

        sizeItems.add(new SizeItem(R.drawable.car_icon_a));

        CustomAdapterSizeMenu adapterSizeMenu = new CustomAdapterSizeMenu(this, sizeItems);
        sizeTextView.setAdapter(adapterSizeMenu);

        dayCheckBoxView = findViewById(R.id.days_check_box_group);

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



        rentControl = new RentControl(this);
        Places.initialize(getApplicationContext(), API_KEY);
        PlacesClient placesClient = Places.createClient(this);

         autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
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
    }

    public void nextStep(View view) {
        createParkingSpace();
    }

    public void createParkingSpace(){
        String stPrice = Objects.requireNonNull(priceLayout.getEditText()).getText().toString().trim();
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
            Toast.makeText(this, "Please enter parking space address", Toast.LENGTH_LONG).show();
            return;
        }

        rentControl.createParkingSpace(price, address);
    }

    public void onDayCheckBoxClicked(View view) {
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

    public void cancelRent(View view) {

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

        /*
    LatLng addressLatlang = locationControl.getLocationFromAddress(address, this);
    ParkingSpace parkingSpace = new ParkingSpace(address, splitLocation(address),price, 20, firebaseHelper.getUserUid(), addressLatlang.latitude, addressLatlang.longitude);
        parkingSpace.setAvailable(true);
    City city = null;
    String cityKey = null;
    String key = null;
        if (cityControl.isExists(parkingSpace.getCity())){
        city = cityControl.getCurrentCity();
    } else {
        city = new City(parkingSpace.getCity());
        cityKey = mDataBase.child("ParkingSpaces").push().getKey();
        city.setKeyID(cityKey);
    }
        if (city != null && city.getKeyID() != null){
        key = mDataBase.child("ParkingSpaces").child(city.getKeyID()).push().getKey();
        parkingSpace.setParkingSpaceID(key);
        mDataBase.child("ParkingSpaces").child(city.getKeyID()).child(key).setValue(parkingSpace);
    }

     */

    /*
    public void previousStep(View view) { viewPager.setCurrentItem(getNextPossibleItemIndex(-1), true); }

       String[] input = new String[3];
       input = rentFirstStepFragment.getInputText();

    createParkingSpace();
        viewPager.setCurrentItem(getNextPossibleItemIndex(1), true);

        //onCreate:
                viewPager = findViewById(R.id.viewPager);
        adapter = new FragmentCollectionAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        rentFirstStepFragment = new RentFirstStep();
        //Valuables:
            private ViewPager viewPager;
            private FragmentCollectionAdapter adapter;
            private RentFirstStep rentFirstStepFragment;

    private int getNextPossibleItemIndex(int change) {
        int currentIndex = viewPager.getCurrentItem();
        int total = viewPager.getAdapter().getCount();

        if (currentIndex + change < 0) {
            return 0;
        }
        return Math.abs((currentIndex + change) % total) ;
    }
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        switch (id){
            case R.id.Auth:{
                intent = new Intent(this, Authentication.class);
                startActivity(intent);
                break;
            }
            case R.id.MapActiv:{
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.SmsSender:{
                intent = new Intent(this, SmsSender.class);
                startActivity(intent);
                break;
            }
            case R.id.RentActivity:{
                intent = new Intent(this, RentActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.register:{
                intent = new Intent(this, RegisterUser.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }
}