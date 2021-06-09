package com.example.airpnp.MapPackage;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.airpnp.Helper.ActionDone;
import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.LocationPackage.LocationControl;
import com.example.airpnp.R;
import com.example.airpnp.Resources.CardItem;
import com.example.airpnp.UserPackage.OrdersControl;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.example.airpnp.UserPackage.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.airpnp.LocationPackage.LocationControl.ACCESS_LOCATION_REQUEST_CODE;

public class MapActivity extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, AdapterView.OnItemClickListener {

    public static String TAG = "Map_Fragment";
    private final int MAP_ZOOM_VALUE = 17;
    GoogleMap mMap;
    FloatingActionButton fab;
    MapControl mapControl;
    FirebaseHelper firebaseHelper;
    LocationControl locationControl;
    ParkingSpaceControl parkingSpaceControl;

    FusedLocationProviderClient fusedLocationProviderClient;
    Location lastKnownLocation;
    OrdersControl ordersControl;

    LinearLayout bottomSheetLayout;
    BottomSheetBehavior bottomSheetBehavior;

    TextView tvPrice, tvAddress, tvParkingSpaceName, timeAvailable, tvStatus, tvPhoneNum, tvEmail;
    ImageView sizeImage;
    Button rentButton;
    Fragment mapFragment;
    FrameLayout frameLayout;

    ListView userParkingSpaceList;
    ArrayList<String> userParkingSpacesNameList;
    ArrayList<CardItem> cardItemList;

    LinearLayout bottomLayout, topLayout;

    ImageButton expendBtn, closeBtn;

    float topLayoutHeight, bottomLayoutHeight, screenHeight,screenWidth, bottomSheetRatio;

    User myUser;
    Button bookButton;

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

        final View root = inflater.inflate(R.layout.activity_map, container, false);

        View layout = getActivity().findViewById(R.id.bottom_sheet_quick_sell);
        userParkingSpaceList = layout.findViewById(R.id.listView_parkingSpaces);


        bottomSheetLayout = root.findViewById(R.id.bottom_sheet_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        bottomLayout = root.findViewById(R.id.bottomLayout_bottomSheet);
        topLayout = root.findViewById(R.id.topLayout);
//
//        CoordinatorLayout coordinatorLayout = getActivity().findViewById(R.id.bottom_sheet_quick_sell);
//        BottomSheetBehavior bottomSheetBehavior= BottomSheetBehavior.from(coordinatorLayout);

        //UI views inside the bottom sheet and initialize them
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setFitToContents(false);

        setsLayoutParams();

        tvAddress = root.findViewById(R.id.addressBotSheet);
        tvParkingSpaceName = root.findViewById(R.id.titleName);
        tvPrice = root.findViewById(R.id.pricePerHour);
        sizeImage = root.findViewById(R.id.size_icon);
        rentButton = root.findViewById(R.id.rentButtonBottomSheet);
        tvPhoneNum = root.findViewById(R.id.phoneNum);
        //tvStatus = root.findViewById(R.id.parkingSpaceStatus);
        tvEmail = root.findViewById(R.id.emailTv);
        timeAvailable = root.findViewById(R.id.workingTime);

        expendBtn = root.findViewById(R.id.expandButton);
        closeBtn = root.findViewById(R.id.btn_close_botSheet);
        expendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expendBtn.getScaleY() == 1){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    bottomLayout.setVisibility(View.VISIBLE);
                    expendBtn.setScaleY(-1);
                }
                else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    expendBtn.setScaleY(1);
                }
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        bottomSheetCallBack(bottomSheetBehavior);

       SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
              .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        parkingSpaceControl = ParkingSpaceControl.getInstance();
        ordersControl = OrdersControl.getInstance();

        firebaseHelper = new FirebaseHelper();
        locationControl = new LocationControl(requireContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        return root;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
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
        getDeviceLocation();
        mapControl.updateLocationUI(requireActivity());
    }

    private void setsLayoutParams(){

        //gets the screen height
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        //gets the action bar height
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            screenHeight -= actionBarHeight;
        }

        //callback when the layout has been drawn
        final ViewTreeObserver vto = topLayout.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    topLayoutHeight = topLayout.getMeasuredHeight();
                    bottomLayoutHeight = bottomLayout.getMeasuredHeight();
                    bottomSheetBehavior.setPeekHeight(topLayout.getMeasuredHeight());
                    halfScreenRatio();
                    // handle viewWidth here...

                    if (Build.VERSION.SDK_INT < 16) {
                        vto.removeGlobalOnLayoutListener(this);
                    } else {
                        topLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    private void halfScreenRatio(){
        bottomSheetRatio = ((topLayoutHeight+bottomLayoutHeight)*100)/screenHeight;
        bottomSheetBehavior.setHalfExpandedRatio(bottomSheetRatio/100);
        Log.v("Ratio", String.valueOf(bottomSheetRatio)+" "+topLayoutHeight+" "+bottomLayoutHeight+" "+screenHeight);
    }

    public void bottomSheetCallBack(final BottomSheetBehavior bottomSheetBehavior){
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheetView, int newState) {
//                if (newState != BottomSheetBehavior.STATE_COLLAPSED)
//                    bottomLayout.setVisibility(View.VISIBLE);
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_HALF_EXPANDED: {
//                        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
//                        bottomNavigationView.setVisibility(View.VISIBLE);
                        break;
                    }
                    case BottomSheetBehavior.STATE_DRAGGING:{
                        if (expendBtn.getScaleY() == 1)
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        else
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    }
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;

                    case BottomSheetBehavior.STATE_HIDDEN:{
                        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                    }
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheetView, float slideOffset) {}
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);

        marker.hideInfoWindow();
        Log.v("MarkerClicked", marker.toString());
        final MarkerButton markerButton = mapControl.getMarkerButtonClicked(marker);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        final ParkingSpace parkingSpace = markerButton.getParkingSpace();
        displayParkingSpace(parkingSpace);

        final Fragment fragment = new PaymentChoiceFragment();
        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkingSpaceControl.parkingSpaceOnBooking = parkingSpace;
                ((MainActivityBotNav) getActivity()).replaceFragments(fragment.getClass());
            }
        });
        return false;
    }

    private void displayParkingSpace(ParkingSpace parkingSpace){
        tvParkingSpaceName.setText(parkingSpace.getParkingSpaceName());
        int iconId = -1;
        tvAddress.setText(parkingSpace.getAddress());
        switch (parkingSpace.getSize()){
            case 0: iconId = R.drawable.car_icon_a; break;
            case 1: iconId = R.drawable.van_icon; break;
            case 2: iconId = R.drawable.truck_icon; break;
        }
        if (iconId != -1) {
            sizeImage.setImageResource(iconId);
        }
        tvPrice.setText(String.valueOf(parkingSpace.getPrice()) + " "+"p/h");
        timeAvailable.setText(parkingSpace.getParkingSpaceWorkingHours());
    }

//    private void setActivityListView(){
//        Order order1 = new Order("-MXWhGX0aOLLg8j7yMvi", null, null, null, null, true);
//        order1.setTime(8);
//        order1.setPrice(25);
//        ordersControl.userOrdersList.add(order1);
//
//        View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "button clicked heyyyy", Toast.LENGTH_LONG).show();
//            }
//        };
//
//        cardItemList = new ArrayList<>();
//        for (Order order:
//                ordersControl.userOrdersList) {
//            if (order.isActive()){
//                cardItemList.add(new CardItem(order, "null", onClickListener));
//            }
//        }
//        CustomAdapterCardList customAdapterCardList = new CustomAdapterCardList(requireContext(), cardItemList);
//        userParkingSpaceList.setAdapter(customAdapterCardList);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParkingSpace parkingSpace = parkingSpaceControl.getParkingSpaceByAddress(userParkingSpacesNameList.get(position));
        if (parkingSpace != null){
            firebaseHelper.setParkingSpaceActive(parkingSpace, true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(parkingSpace.getLatitude(),
                            parkingSpace.getLongitude()), MAP_ZOOM_VALUE));
        }
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
                getDeviceLocation();
            }
        }
    }

//    private void deviceLocationAction(){
//        getDeviceLocation(new ActionDone() {
//            @Override
//            public void onSuccess() {
//                Log.v("Path",ParkingSpaceControl.parkingSpacesPath+locationControl.getUserCityName());
//                firebaseHelper.getAllParkingSpacesInCity(locationControl, new ActionDone() {
//                    @Override
//                    public void onSuccess() {
//                         mMap.clear();
//                        mapControl.createMarkers();
//                        //setActivityListView();
//                /*
//             Log.v("ButtonListArray", String.valueOf(mapControl.getLength()));
//             Log.v("ParkingSpaceList", String.valueOf(parkingSpaceControl.parkingSpacesList.size()));
//              */
//                    }
//                    @Override
//                    public void onFailed(){
//
//                    }
//                });;
//            }
//
//            @Override
//            public void onFailed() {
//
//            }
//        });
//    }

    private void downloadParkingSpaces(){
        Log.v("Path",ParkingSpaceControl.parkingSpacesPath+locationControl.getUserCityName());
        firebaseHelper.getAllParkingSpacesInCity(locationControl, new ActionDone() {
            @Override
            public void onSuccess() {
                mMap.clear();
                mapControl.createMarkers();
                //setActivityListView();
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

    private void getDeviceLocation() {
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
                                                lastKnownLocation.getLongitude()), MAP_ZOOM_VALUE));
                                //mMap.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())));

                                locationControl.updateUserLocation();
                                downloadParkingSpaces();
                                //actionDone.onSuccess();
                            }
                        } else {
                            //actionDone.onFailed();
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    public void moveCameraMap(){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lastKnownLocation.getLatitude(),
                        lastKnownLocation.getLongitude()), 17));
    }
}

//    public void setUserParkingSpacesListView(){
//        userParkingSpaceList = getActivity().findViewById(R.id.botSheetUserParkingSpacesList);
//        userParkingSpacesNameList = new ArrayList<>();
//        for (ParkingSpace parkingSpace:
//                parkingSpaceControl.userParkingSpacesList) {
//            userParkingSpacesNameList.add(parkingSpace.getAddress());
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(),
//                R.layout.support_simple_spinner_dropdown_item,
//                userParkingSpacesNameList);
//        userParkingSpaceList.setAdapter(adapter);
//        userParkingSpaceList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ParkingSpace parkingSpace = mapControl.getSelectedUserParkingSpace(userParkingSpacesNameList.get(position));
//                if (parkingSpace != null)
//                    firebaseHelper.setParkingSpaceActive(parkingSpace, true);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }

//    private void setUserParkingSpacesListView(){
//        userParkingSpacesNameList = new ArrayList<>();
//        for (ParkingSpace parkingSpace:
//                parkingSpaceControl.userParkingSpacesList) {
//            userParkingSpacesNameList.add(parkingSpace.getAddress());
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(),
//                R.layout.support_simple_spinner_dropdown_item,
//                userParkingSpacesNameList);
//        userParkingSpaceList.setAdapter(adapter);
//        userParkingSpaceList.setOnItemClickListener(this);
//    }