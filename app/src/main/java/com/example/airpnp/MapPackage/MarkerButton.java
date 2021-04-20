package com.example.airpnp.MapPackage;

import com.example.airpnp.UserPackage.ParkingSpace;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerButton {
    private MarkerOptions markerOptions;
    private Marker marker;
    private ParkingSpace parkingSpace;
    private GoogleMap googleMap;
    private boolean userParkingSpace;

    public MarkerButton(GoogleMap googleMap,ParkingSpace parkingSpace, boolean userParkingSpace){
        this.parkingSpace = parkingSpace; this.googleMap = googleMap; this.userParkingSpace = userParkingSpace;
        markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(parkingSpace.getLatitude(), parkingSpace.getLongitude())).title(parkingSpace.getAddress());
    }

    public void addMarkerOnMap(){
        if (markerOptions == null)
            return;
        marker = googleMap.addMarker(markerOptions);
    }

    public void updateMarker(ParkingSpace parkingSpace){

    }

    public Marker getMarker(){
        return marker;
    }

    public ParkingSpace getParkingSpace(){
        return this.parkingSpace;
    }

    public boolean isUserParkingSpace() {
        return userParkingSpace;
    }

    public void setUserParkingSpace(boolean userParkingSpace) {
        this.userParkingSpace = userParkingSpace;
    }
}