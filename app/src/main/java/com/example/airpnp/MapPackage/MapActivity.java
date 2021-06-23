package com.example.airpnp.MapPackage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.airpnp.Helper.DataTransferHelper;
import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.LocationPackage.LocationControl;
import com.example.airpnp.R;
import com.example.airpnp.Resources.CardItem;
import com.example.airpnp.Resources.CustomAdapterCarList;
import com.example.airpnp.UserPackage.OrdersControl;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.example.airpnp.UserPackage.User;
import com.example.airpnp.UserPackage.UserCar;
import com.example.airpnp.UserPackage.UserInstance;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import static com.example.airpnp.LocationPackage.LocationControl.ACCESS_LOCATION_REQUEST_CODE;
/**
 * @author Ido Perez
 * @version 1.2
 * @since 25.12.2020
 */
public class MapActivity extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, AdapterView.OnItemClickListener {

    public static String MAP_TAG = "Map_Fragment";
    private final int MAP_ZOOM_VALUE = 17;
    private final int FAB_DELAY = 3000;
    GoogleMap mMap;
    ExtendedFloatingActionButton fab_userCar;
    AlertDialog carDialog;
    MapControl mapControl;
    FirebaseHelper firebaseHelper;
    LocationControl locationControl;
    ParkingSpaceControl parkingSpaceControl;

    FusedLocationProviderClient fusedLocationProviderClient;
    Location lastKnownLocation;
    OrdersControl ordersControl;

    LinearLayout bottomSheetLayout;
    BottomSheetBehavior bottomSheetBehavior;

    //bottomSheet variables
    TextView tvPrice, tvAddress, tvParkingSpaceName, timeAvailable, tvStatus, tvPhoneNum, tvEmail;
    ImageView sizeImage;
    Button rentButton;
    LinearLayout bottomLayout, topLayout;
    ImageButton expendBtn, closeBtn;
    float topLayoutHeight, bottomLayoutHeight, screenHeight,screenWidth, bottomSheetRatio;
    //end

    ListView userParkingSpaceList;
    ArrayList<String> userParkingSpacesNameList;
    User tempUser;

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

        View layout = requireActivity().findViewById(R.id.bottom_sheet_quick_sell);
        userParkingSpaceList = layout.findViewById(R.id.listView_parkingSpaces);
        fab_userCar = root.findViewById(R.id.extended_fab_userCar);
        fab_userCar.shrink();
        createCustomDialog();
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
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        parkingSpaceControl = ParkingSpaceControl.getInstance();
        ordersControl = OrdersControl.getInstance();

        firebaseHelper = new FirebaseHelper();
        locationControl = new LocationControl(requireContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        return root;
    }

    public void initUserCarFab(){
        fab_userCar.extend();
        UserCar userCar = UserInstance.currentCar;
        if (userCar == null)
            return;
        fab_userCar.setText(userCar.getCarName());
        int id;
        switch (userCar.getCarSize()){
            case 0: id = R.drawable.car_icon_a; break;
            case 1: id = R.drawable.van_icon; break;
            case 2: id = R.drawable.truck_icon; break;
            default: id = -1;
        }
        if (id != -1)
            fab_userCar.setIconResource(id);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fab_userCar.shrink();
            }
        }, FAB_DELAY);
    }

    public void createCustomDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_alert_dialog_user_cars, null);
        builder.setView(convertView);
        builder.setTitle("Select car");
        ListView lv = (ListView) convertView.findViewById(R.id.list_view_cars);
        carDialog = builder.create();
        CustomAdapterCarList customAdapterCarList = new CustomAdapterCarList(requireContext(), UserInstance.currentUser.getUserCars());
        customAdapterCarList.setOnItemClickListener(new CustomAdapterCarList.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UserInstance.currentCar = UserInstance.currentUser.getUserCars().get(position);
                carDialog.dismiss();
                initUserCarFab();
            }
        });
        lv.setAdapter(customAdapterCarList);
        fab_userCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carDialog.show();
            }
        });
    }

    /**
     * @param googleMap
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
        if (UserInstance.currentCar == null)
            carDialog.show();
    }

    /**
     * gets all the screen and layouts Measurements for measure the topLayout pixels and the layouts ratio for the bottom sheet
     * @see BottomSheetBehavior
     */
    private void setsLayoutParams(){

        //gets the screen height
        DisplayMetrics displaymetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        //gets the action bar height
        TypedValue tv = new TypedValue();
        if (requireActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
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

    /**
     * calculate the ratio of the topLayout bottom sheet and the bottomLayout bottom sheet combined.
     * by percents equation
     */
    private void halfScreenRatio(){
        bottomSheetRatio = ((topLayoutHeight+bottomLayoutHeight)*100)/screenHeight;
        bottomSheetBehavior.setHalfExpandedRatio(bottomSheetRatio/100);
        Log.v("Ratio", bottomSheetRatio +" "+topLayoutHeight+" "+bottomLayoutHeight+" "+screenHeight);
    }

    /**
     * controls all the bottom sheet state changes.
     * @param bottomSheetBehavior
     */
    public void bottomSheetCallBack(final BottomSheetBehavior bottomSheetBehavior){
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheetView, int newState) {
//                if (newState != BottomSheetBehavior.STATE_COLLAPSED)
//                    bottomLayout.setVisibility(View.VISIBLE);
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:{
                        expendBtn.setScaleY(1);
                    } break;
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
                        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                    }
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheetView, float slideOffset) {}
        });
    }

    /**
     * called when marker click.
     * @see ParkingSpaceControl
     * @see ParkingSpace
     * @see MarkerButton
     * @see BottomSheetBehavior
     * @param marker the clicked map marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);

        marker.hideInfoWindow();
        Log.v("MarkerClicked", marker.toString());
        final MarkerButton markerButton = mapControl.getMarkerButtonClicked(marker);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        final ParkingSpace parkingSpace = markerButton.getParkingSpace();
        displayParkingSpace(parkingSpace);
        displayUser(parkingSpace);

        final Fragment fragment = new PaymentChoiceFragment();
        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compareSize(parkingSpace, fragment);
            }
        });
        return false;
    }

    public void transferFragment(Fragment fragment){
        ((MainActivityBotNav) requireActivity()).replaceFragments(fragment.getClass());
    }

    public void saveData(ParkingSpace parkingSpace){
        parkingSpaceControl.parkingSpaceOnBooking = parkingSpace;
        DataTransferHelper dataTransferHelper = DataTransferHelper.newInstance();
        dataTransferHelper.setParentTAG(MAP_TAG);
        dataTransferHelper.putParkingSpace("parkingOnBooking", parkingSpace);
        dataTransferHelper.putUser("parkingSpaceUser", tempUser);
    }

    private void compareSize(final ParkingSpace parkingSpace, final Fragment fragment){
        if (UserInstance.currentCar == null){
            carDialog.show();
            return;
        }
        if (parkingSpace.getSize() >= UserInstance.currentCar.getCarSize()){
            saveData(parkingSpace);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            transferFragment(fragment);
            return;
        }
        final AlertDialog.Builder adb= new AlertDialog.Builder(requireContext());
        adb.setTitle("Warning");
        adb.setMessage("The parking can be too small for your vehicle");
        adb.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData(parkingSpace);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                transferFragment(fragment);
            }
        });
        adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
//        adb.setNegativeButton("Change car", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        carDialog.show();
//                    }
//                }, 1000);
//            }
//        });
        AlertDialog sizeDialog = adb.create();
        sizeDialog.show();
    }

    /**
     * seting the parking space details in the bottom sheet vies.
     * @param parkingSpace
     */
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

    public void displayUser(final ParkingSpace parkingSpace){
        firebaseHelper.getUserById(parkingSpace.getUserUID(), new FirebaseHelper.GetUserOnActionDone() {
            @Override
            public void singleUserRead(User user) {
//                ordersControl.orderUsers.put(parkingSpace.getParkingSpaceID(), user);
                tempUser = user;
                tvPhoneNum.setText(user.getPhoneNumber());
                tvEmail.setText(user.getEmail());
            }

            @Override
            public void groupUserRead(ArrayList<User> users) {

            }
        });
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
//                firebaseHelper.getAllParkingSpaces(locationControl, new ActionDone() {
//                    @Override
//                    public void onSuccess() {
//                         mMap.clear();
//                        mapControl.createMarkers();
//                        //setActivityListView();
//
//             Log.v("ButtonListArray", String.valueOf(mapControl.getLength()));
//             Log.v("ParkingSpaceList", String.valueOf(parkingSpaceControl.parkingSpacesList.size()));
//
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
        parkingSpaceControl.userParkingSpacesList.clear();
        parkingSpaceControl.parkingSpacesList.clear();
        mMap.clear();
        //if (!FirebaseHelper.isParkingSpacesListenerOn){
        //if (FirebaseHelper.childEventListener == null){
            FirebaseHelper.childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    ParkingSpace parkingSpace = snapshot.getValue(ParkingSpace.class);
                    if (parkingSpace != null && parkingSpace.getUserUID().equals(firebaseHelper.getUserUid())){
                        parkingSpaceControl.userParkingSpacesList.add(parkingSpace);
                    }
                    //add else
                    parkingSpaceControl.parkingSpacesList.add(parkingSpace);
                    mapControl.createMarkerButton(parkingSpace);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            firebaseHelper.getAllParkingSpaces();
        //}
        //}

//        firebaseHelper.getAllParkingSpaces(locationControl, new ActionDone() {
//            @Override
//            public void onSuccess() {
//                mMap.clear();
//                mapControl.createMarkers();
//                //setActivityListView();
//
//             Log.v("ButtonListArray", String.valueOf(mapControl.getLength()));
//             Log.v("ParkingSpaceList", String.valueOf(parkingSpaceControl.parkingSpacesList.size()));
//
//            }
//            @Override
//            public void onFailed(){
//
//            }
//        });;
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