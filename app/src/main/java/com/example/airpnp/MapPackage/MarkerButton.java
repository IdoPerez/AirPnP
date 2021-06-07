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

    public MarkerButton(GoogleMap googleMap,ParkingSpace parkingSpace){
        this.parkingSpace = parkingSpace; this.googleMap = googleMap;
        markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(parkingSpace.getLatitude(), parkingSpace.getLongitude())).title(parkingSpace.getAddress());
    }

    public void addMarkerOnMap(){
        if (markerOptions == null)
            return;
        marker = googleMap.addMarker(markerOptions);
    }

    public Marker getMarker(){
        return marker;
    }

    public ParkingSpace getParkingSpace(){
        return this.parkingSpace;
    }
}