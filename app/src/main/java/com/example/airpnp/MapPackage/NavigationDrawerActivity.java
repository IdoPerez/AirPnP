package com.example.airpnp.MapPackage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.airpnp.Helper.ActionDone;
import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.LocationPackage.LocationControl;
import com.example.airpnp.R;
import com.example.airpnp.UserPackage.OrdersControl;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.example.airpnp.UserPackage.ProfileControl;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class NavigationDrawerActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener  {

    private AppBarConfiguration mAppBarConfiguration;
    private ProfileControl profileControl;

    //Map types
    GoogleMap mMap;
    MapControl mapControl;
    ImageView profileImage;
    CustomInfoWindowAdapter customInfoWindowAdapter;

    //Firebase helper
    FirebaseHelper firebaseHelper;

    //locations types
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationControl locationControl;

    //order types
    OrdersControl ordersControl;

    //Buttons types:
    FloatingActionButton fab;

    //Bottom sheet types:
    ConstraintLayout bottomSheetLayout;
    BottomSheetBehavior bottomSheetBehavior;
    TextView tvSize, tvPrice, tvAddress,tvCity;
    Button rentButton;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Fragment mapFragment = new MapActivity();
//    MapActivity mapActivity = new MapActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //profileImage = findViewById(R.id.profileImage);
        setNavigationDrawer();
        View headerView = navigationView.getHeaderView(0);

//        bottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
//        tvAddress = findViewById(R.id.address);
//        tvCity = findViewById(R.id.city);
//        tvPrice = findViewById(R.id.price);
//        tvSize = findViewById(R.id.size);
//        //UI views inside the bottom sheet and initialize them
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        bottomSheetBehavior.setFitToContents(false);
//        bottomSheetBehavior.setHalfExpandedRatio(0.4f);
        //finish

        //bottomSheet call back
//        bottomSheetCallBack(bottomSheetBehavior);
        //finish

        //BottomSheet buttons on click
            //rentButton = findViewById(R.id.rentButtonBottomSheet);
        //finish

        profileImage = (ImageView) headerView.findViewById(R.id.profileImage);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });


//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mapFragment)
                .commit();

        firebaseHelper = new FirebaseHelper();
        locationControl = new LocationControl(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        profileControl = new ProfileControl(this, profileImage);
        ordersControl = OrdersControl.getInstance();

        //profileControl.downloadProfileImage();
        if (profileImage != null){
            if (profileControl.isProfileImageEmpty()){
                Toast.makeText(this, "Enter profile image", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setNavigationDrawer(){
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.parkingSpaces_profile:{
                        fragment = new UserPSdisplayFragment();
                        transferFragment(fragment);
                        drawerLayout.close();
                        } break;
                    case R.id.nav_map:{
                        fragment = new MapActivity();
                        transferFragment(fragment);
                        drawerLayout.close();
                    }
                }
                return false;
            }
        });
    }

    private void transferFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    

//    public void bottomSheetCallBack(final BottomSheetBehavior bottomSheetBehavior){
//        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//                switch (newState) {
//
//                    case BottomSheetBehavior.STATE_COLLAPSED:{
//                        break;
//                    }
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED:
//                        break;
//                    case BottomSheetBehavior.STATE_HALF_EXPANDED:{
//                    }
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
//    }

    public void pickImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, ProfileControl.IMAGE_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnInfoWindowClickListener(this);
        mapControl = new MapControl(this, mMap);
        //customInfoWindowAdapter = new CustomInfoWindowAdapter(this);

        //mMap.setInfoWindowAdapter(customInfoWindowAdapter);

//        LocationControl.getLocationPermission(this, this);

        deviceLocationAction();
        mapControl.updateLocationUI(this);
        ordersControl = OrdersControl.getInstance();

        //BottomSheet test with manual marker.
        /*
        ParkingSpace parkingSpace = new ParkingSpace("address", "parkingSpaceCity",25, 20, firebaseHelper.getUserUid(), -33.868820,151.209290);
        parkingSpace.setParkingSpaceID("1234");
          markerButton = new MarkerButton(mMap, parkingSpace);
          mapControl.addMarkerButton(markerButton);
        mMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(-33.868820,151.209290), 18));
        markerButton.addMarkerOnMap();
         */
    }

    private void deviceLocationAction(){
        getDeviceLocation(new ActionDone() {
            @Override
            public void onSuccess() {
                onDeviceLocationFound();
            }

            @Override
            public void onFailed() {

            }
        });
    }

    public void onDeviceLocationFound(){
        Log.v("Path", ParkingSpaceControl.parkingSpacesPath+locationControl.getUserCityName());
        firebaseHelper.getAllParkingSpacesInCity(locationControl, new ActionDone() {
            @Override
            public void onSuccess() {
                mapControl.createMarkers();
                /*
             Log.v("ButtonListArray", String.valueOf(mapControl.getLength()));
             Log.v("ParkingSpaceList", String.valueOf(parkingSpaceControl.parkingSpacesList.size()));
              */
            }
            @Override
            public void onFailed(){

            }
        });
    }

    private void getDeviceLocation(final ActionDone actionDone) {
        try {
            if (LocationControl.locationPermissionGranted) {
                final Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location lastKnownLocation;
                            lastKnownLocation = task.getResult();
                            locationControl.lastKnownLocation = lastKnownLocation;
                            if (lastKnownLocation != null) {

                                locationControl.setLastKnownLocation(lastKnownLocation);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 19));
                                locationControl.updateUserLocation();
                                actionDone.onSuccess();
                            }
                        } else {
                            actionDone.onFailed();
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProfileControl.IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            profileControl.uploadImage(data.getData());
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        LocationControl.locationPermissionGranted = false;
//        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                LocationControl.locationPermissionGranted = true;
////                mapControl.updateLocationUI(this);
////                deviceLocationAction();
//                mapActivity.onR
//            }
//        }
//    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        customInfoWindowAdapter.onInfoWindowClickListener(marker);
        MarkerButton markerButton = mapControl.getMarkerButtonClicked(marker);
        //createNewOrder(markerButton.getParkingSpace());
        marker.showInfoWindow();
    }

//    public void createNewOrder(ParkingSpace parkingSpace){
//        Order userOrder = new Order(parkingSpace.getParkingSpaceID(), parkingSpace.getUserUID(), firebaseHelper.getUserUid());
//        ordersControl.userOrdersList.add(userOrder);
//        firebaseHelper.uploadOrder(userOrder, OrdersControl.ordersPath);
//    }
}