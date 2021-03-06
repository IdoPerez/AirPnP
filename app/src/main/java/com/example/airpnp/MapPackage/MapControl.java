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
/**
 * @author Ido Perez
 * @version 1.1
 * @since 26.12.2020
 */
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

    /**
     * update the location Ui in the map
     * @param activity
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

    /**
     * creates markerButtons by the parkingSpaces array
     * @see ParkingSpaceControl
     */
    public void createMarkers(){
        markerButtonsList.clear();
        for (ParkingSpace parkingSpace: parkingSpaceControl.parkingSpacesList) {
            createMarkerButton(parkingSpace);
        }
    }

    /**
     * creats new markerButton
     * @param parkingSpace
     */
    public void createMarkerButton(ParkingSpace parkingSpace){
        MarkerButton markerButton = new MarkerButton(googleMap, parkingSpace);
        markerButtonsList.add(markerButton);
        markerButton.addMarkerOnMap();
    }

    /**
     * return the marker that was clicked in the map.
     * @see MarkerButton
     * @param marker
     * @return
     */
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

    public ParkingSpace getSelectedUserParkingSpace(String address){
        for (ParkingSpace parkingSpace:
             parkingSpaceControl.userParkingSpacesList) {
            if (parkingSpace.getAddress().equals(address))
                return parkingSpace;
        }
        return null;
    }

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
}
