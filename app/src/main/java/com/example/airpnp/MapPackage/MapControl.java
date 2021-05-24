package com.example.airpnp.MapPackage;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.LocationPackage.LocationControl;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class MapControl {
    public  ArrayList<MarkerButton> markerButtonsList;
    private final ParkingSpaceControl parkingSpaceControl;
    private final GoogleMap googleMap;
    private final Context context;

    public MapControl(Context context, GoogleMap map){
        markerButtonsList = new ArrayList<>();
        parkingSpaceControl = ParkingSpaceControl.getInstance();
        googleMap = map;
        this.context = context;
    }

    /*
    parkingSpaceRef = FirebaseDatabase.getInstance().getReference(path);
        parkingSpaces = new ArrayList<>();
        parkingSpaceListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data:
                        dataSnapshot.getChildren()) {
                    ParkingSpace parkingSpace = data.getValue(ParkingSpace.class);
                    parkingSpaces.add(parkingSpace);
                    //createMarkers(parkingSpace);
                }
                actionDone.onSuccess();
                //addMarkersOnMap(googleMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        parkingSpaceRef.addValueEventListener(parkingSpaceListener);
        /*
        Query query = parkingSpaceRef.orderByChild("available").equalTo(true);
        query.addValueEventListener(parkingSpaceListener);
         */
    /*
    public void getDeviceLocation(){
        locationControl.getDeviceLocation(mapActivity, new ActionDone() {
            @Override
            public void onSuccess() {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(locationControl.getLastKnownLocation().getLatitude(),
                                locationControl.getLastKnownLocation().getLongitude()), 19));
                userLocation();
            }

            @Override
            public void onFailed() {

            }
        });
    }

     */

    public void updateLocationUI(Activity activity) {
        if (googleMap == null) {
            return;
        }
        try {
            if (LocationControl.locationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                LocationControl.getLocationPermission(context, activity);
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
//    public void userLocation(){
//            Log.v("Path", "ParkingSpaces/"+locationControl.getCity().getKeyID());
//            String path = ("ParkingSpaces/"+locationControl.getCity().getKeyID());
//            firebaseHelper.getAllParkingSpaces(path, new ActionDone() {
//                @Override
//                public void onSuccess() {
//                   createMarkers();
//                /*
//             Log.v("ButtonListArray", String.valueOf(mapControl.getLength()));
//             Log.v("ParkingSpaceList", String.valueOf(parkingSpaceControl.parkingSpacesList.size()));
//              */
//                }
//
//                @Override
//                public void onFailed(){
//
//                }
//            });
//    }

    public void createMarkers(){
        markerButtonsList.clear();
        for (ParkingSpace parkingSpace: parkingSpaceControl.parkingSpacesList) {
            /*
            if (!markerDisplayed(parkingSpace)){
                MarkerButton markerButton = new MarkerButton(googleMap, parkingSpace);
                markerButtonsList.add(markerButton);
                markerButton.addMarkerOnMap();
            }

             */
            createMarkerButton(parkingSpace);
        }
    }

    public void createMarkerButton(ParkingSpace parkingSpace){
        MarkerButton markerButton = new MarkerButton(googleMap, parkingSpace);
        markerButtonsList.add(markerButton);
        markerButton.addMarkerOnMap();
    }

    /*
    public void addMarkersOnMap(){
        for (MarkerButton markerButton: markerButtonsList){

            markerButton.addMarkerOnMap();
        }
    }

     */
    public void onMarkerClick(Marker marker) {
        for (MarkerButton markerButton: markerButtonsList){
            if (marker.getId().equals(markerButton.getMarker().getId())){
                Log.v("ParkingSpace", markerButton.getParkingSpace().toString());
                marker.setTitle(String.valueOf(markerButton.getParkingSpace().getPrice()+" "+markerButton.getParkingSpace().getAddress()));
                break;
            }
        }
    }

    public MarkerButton getMarkerButtonClicked(Marker marker) {
        for (MarkerButton markerButton : markerButtonsList) {
            if (marker.equals(markerButton.getMarker())) {
                Log.v("ParkingSpace", markerButton.getParkingSpace().toString());
//                marker.setTitle(String.valueOf(markerButton.getParkingSpace().getPrice() + " " + markerButton.getParkingSpace().getAddress()));
                return markerButton;
            }
        }
        return null;
    }

//    public MarkerButton getMarkerButtonClicked(Marker marker) {
//        for (MarkerButton markerButton : markerButtonsList) {
//            if (marker.getId().equals(markerButton.getMarker().getId())) {
//                Log.v("ParkingSpace", markerButton.getParkingSpace().toString());
////                marker.setTitle(String.valueOf(markerButton.getParkingSpace().getPrice() + " " + markerButton.getParkingSpace().getAddress()));
//                return markerButton;
//            }
//        }
//        return null;
//    }

    public MarkerButton getMarkerButton(Marker marker){
        for (MarkerButton markerButton: markerButtonsList){
            if (marker.equals(markerButton.getMarker())){
                return markerButton;
            }
        }
        return null;
    }

    public int getLength(){
        return markerButtonsList.size();
    }

    public void addMarkerButton(MarkerButton markerButton){
        markerButtonsList.add(markerButton);
    }

    public void setUserParkingSpaceToRent(String s) {
        for (ParkingSpace parkingSpace:
             parkingSpaceControl.userParkingSpacesList) {
            if (parkingSpace.getAddress().equals(s)){
                createMarkerButton(parkingSpace);
            }
        }
    }
}
