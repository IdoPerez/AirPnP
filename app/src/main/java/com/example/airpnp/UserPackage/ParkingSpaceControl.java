package com.example.airpnp.UserPackage;

import com.example.airpnp.Helper.FirebaseHelper;

import java.util.ArrayList;

public class ParkingSpaceControl {
    private static ParkingSpaceControl parkingSpaceControl_instance = null;
    public ArrayList<ParkingSpace> parkingSpacesList;
    public ArrayList<ParkingSpace> userParkingSpacesList;
    public static final String parkingSpacesPath = "ParkingSpaces/"; // after creating start app class set the path!!!!!!!

    private ParkingSpaceControl(){
        parkingSpacesList = new ArrayList<>();
        userParkingSpacesList = new ArrayList<>();
    }

    public static ParkingSpaceControl getInstance(){
        if (parkingSpaceControl_instance == null){
            parkingSpaceControl_instance = new ParkingSpaceControl();
        }
        return parkingSpaceControl_instance;
    }

    public void getUserParkingSpaces(){
        for (ParkingSpace parkingSpace:
             parkingSpacesList) {
            if (parkingSpace.getUserUID().equals(FirebaseHelper.CURRENT_USER_UID))
                userParkingSpacesList.add(parkingSpace);
        }
    }

    public ParkingSpace getParkingSpaceByAddress(String address){
        for (ParkingSpace parkingSpace:
                userParkingSpacesList) {
            if (parkingSpace.getAddress().equals(address))
                return parkingSpace;
        }
        return null;
    }

    public ParkingSpace getParkingSpaceById(String parkingSpaceID){
        for (ParkingSpace parking:
             parkingSpacesList) {
            if (parking.getParkingSpaceID().equals(parkingSpaceID))
                return parking;
        }
        return null;
    }
}
