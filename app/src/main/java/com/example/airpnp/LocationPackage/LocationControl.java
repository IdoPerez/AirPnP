package com.example.airpnp.LocationPackage;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.airpnp.RentPackage.City;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationControl {
    private Locale locale;
    public static final int ACCESS_LOCATION_REQUEST_CODE = 10001;
    public static boolean locationPermissionGranted = false;
    public Location lastKnownLocation;
    private List<Address> addresses;
    private String address;
    private String userCityName;
    private String country;
    private final Context context;
    private Geocoder geoCoder;

    public LocationControl(Context context){
        this.context = context;
    }

    public static void getLocationPermission(Context activityContext ,Activity activity) {
        if (ContextCompat.checkSelfPermission(activityContext.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_LOCATION_REQUEST_CODE);
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {
        locale = new Locale.Builder().setLanguage("en").setScript("Latn").setRegion("IL").build();
        geoCoder = new Geocoder(context, locale);
        List<Address> address;
        LatLng p1 = null;
        try {
            // May throw an IOException
            address = geoCoder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    private String splitAddressLocation(String address){
        String[] split = address.split(",", 2);
        //Log.v("CityFromRent", split[1]);
        return split[1].replaceAll("\\s","");
    }

    public void getAddressFromLocation(Location location) {
        locale = new Locale.Builder().setLanguage("en").build();
        Geocoder geoCoder = new Geocoder(context, locale);
        try {
            addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
             address = addresses.get(0).getAddressLine(0);
             userCityName = addresses.get(0).getLocality();
             country = addresses.get(0).getCountryName();
             Log.v("cityName", userCityName);
            //Log.v("City"+" "+ "Address", city+" "+ address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void updateUserLocation() {
        //locale = new Locale.Builder().setLanguage("en").build();
        locale = new Locale.Builder().setLanguage("en").setScript("Latn").setRegion("IL").build();
        Geocoder geoCoder = new Geocoder(context, locale);

        if (lastKnownLocation != null){
            try {
                addresses = geoCoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
                address = addresses.get(0).getAddressLine(0);
                userCityName = addresses.get(0).getLocality();
                country = addresses.get(0).getCountryName();
                userCityName = clearString(userCityName);
                country = clearString(country);
                Log.v("UserLocationDetails", "address:"+" "+address+" "+"city:"+" "+userCityName+"country:"+" "+country);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String clearString(String input){
        return input.replaceAll("\\s+","");
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public String getUserCityName() {
        return userCityName;
    }

    public boolean getLocationPermissionGranted(){
        return locationPermissionGranted;
    }
}
