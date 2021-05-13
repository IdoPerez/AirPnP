package com.example.airpnp.RentPackage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airpnp.AuthPackage.Authentication;
import com.example.airpnp.AuthPackage.RegisterUser;
import com.example.airpnp.ContactUser.SmsSender;
import com.example.airpnp.MapPackage.MapActivity;
import com.example.airpnp.R;
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

import java.util.Arrays;
import java.util.Objects;

public class RentActivity extends AppCompatActivity {
    TextInputLayout priceLayout, sizeLayout;
    AutocompleteSupportFragment autocompleteSupportFragment;
    private RentControl rentControl;
    private static final String API_KEY = "AIzaSyANALN_AusNyUUV5oD_DE_U2hO__5GEm48";
    private String address, city, stPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        priceLayout = findViewById(R.id.layoutPrice);
        sizeLayout = findViewById(R.id.layoutSize);

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

    public void cancelRent(View view) {

    }


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

    public void onDayCheckBoxClicked(View view) {
    }
}