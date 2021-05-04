package com.example.airpnp.MapPackage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.airpnp.Helper.ActionDone;
import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.LocationPackage.LocationControl;
import com.example.airpnp.MainActivity;
import com.example.airpnp.R;
import com.example.airpnp.UserPackage.Order;
import com.example.airpnp.UserPackage.OrdersControl;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import static com.example.airpnp.LocationPackage.LocationControl.ACCESS_LOCATION_REQUEST_CODE;

public class MapActivity extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    public static String TAG = "Map_Fragment";

    MapView mapView;
    GoogleMap mMap;
    FloatingActionButton fab;
    MapControl mapControl;
    FirebaseHelper firebaseHelper;
    LocationControl locationControl;
    ParkingSpaceControl parkingSpaceControl;

    FusedLocationProviderClient fusedLocationProviderClient;
    Location lastKnownLocation;
    OrdersControl ordersControl;

    CoordinatorLayout bottomSheetLayout;
    BottomSheetBehavior bottomSheetBehavior;
    TextView tvSize, tvPrice, tvAddress,tvCity;
    Button rentButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        /*
        String languageToLoad = "iw_IL";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

         */

        View root = inflater.inflate(R.layout.activity_map, container, false);

        bottomSheetLayout = root.findViewById(R.id.bottom_sheet_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        //UI views inside the bottom sheet and initialize them
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setFitToContents(false);
        bottomSheetBehavior.setHalfExpandedRatio(0.4f);

        tvAddress = root.findViewById(R.id.address);
        tvCity = root.findViewById(R.id.city);
        tvPrice = root.findViewById(R.id.price);
        tvSize = root.findViewById(R.id.size);
        rentButton = root.findViewById(R.id.rentButtonBottomSheet);

        bottomSheetCallBack(bottomSheetBehavior);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firebaseHelper = new FirebaseHelper();
        locationControl = new LocationControl(requireContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapControl = new MapControl(getContext(), mMap);
        mMap.setOnMarkerClickListener(MapActivity.this);
        if (ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationControl.locationPermissionGranted = true;
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_LOCATION_REQUEST_CODE);
        }
        deviceLocationAction();
        mapControl.updateLocationUI(requireActivity());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LocationControl.locationPermissionGranted = false;
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationControl.locationPermissionGranted = true;
                mapControl.updateLocationUI(requireActivity());
                deviceLocationAction();
            }
        }
    }

    private void deviceLocationAction(){
        getDeviceLocation(new ActionDone() {
            @Override
            public void onSuccess() {
                Log.v("Path",ParkingSpaceControl.parkingSpacesPath+locationControl.getUserCityName());
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
                });;
            }

            @Override
            public void onFailed() {

            }
        });
    }

    private void getDeviceLocation(final ActionDone actionDone) {
        try {
            if (LocationControl.locationPermissionGranted) {
                final Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {

                                locationControl.setLastKnownLocation(lastKnownLocation);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 19));
                                //mMap.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())));

                                locationControl.updateUserLocation();
                                actionDone.onSuccess();
                            }
                        } else {
                            actionDone.onFailed();
                            /*
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(new LatLng(-33.868820,151.209290), 18));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);

                             */
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
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
//                mapControl.updateLocationUI(requireActivity());
//                deviceLocationAction();
//            }
//        }
//    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.hideInfoWindow();
        Log.v("MarkerClicked", marker.toString());
        final MarkerButton markerButton = mapControl.getMarkerButtonClicked(marker);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        ParkingSpace parkingSpace = markerButton.getParkingSpace();
        tvAddress.setText("Address: "+parkingSpace.getAddress());
        tvCity.setText("City: "+parkingSpace.getParkingSpaceCity());
        tvPrice.setText("Price: "+parkingSpace.getPrice());
        tvSize.setText("Size: "+parkingSpace.getSize());

        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersControl.placeNewOrder(markerButton.getParkingSpace().getParkingSpaceID(), markerButton.getParkingSpace().getUserUID(), firebaseHelper.getUserUid());
                Toast.makeText(requireContext(), "new order created", Toast.LENGTH_LONG).show();
            }
        });
        //createNewOrder(markerButton.getParkingSpace());
        //customInfoWindowAdapter.onMarkerClicked(markerButton);
        //marker.showInfoWindow();
        return false;
    }

    public void createNewOrder(ParkingSpace parkingSpace){
        Order userOrder = new Order(parkingSpace.getParkingSpaceID(), parkingSpace.getUserUID(), firebaseHelper.getUserUid(), null, null);
        ordersControl.userOrdersList.add(userOrder);
        firebaseHelper.uploadOrder(userOrder, OrdersControl.ordersPath);
    }
}