package com.example.airpnp.UserPackage;

import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.MapPackage.MarkerButton;

import java.util.ArrayList;
/**
 * @author Ido Perez
 * @version 0.1
 * @since 20.11.2020
 */
public class ParkingSpaceControl {
    private static ParkingSpaceControl parkingSpaceControl_instance = null;
    public ArrayList<ParkingSpace> parkingSpacesList;
    public ArrayList<ParkingSpace> userParkingSpacesList;
    public ArrayList<MarkerButton> markerButtons;
    public static final String parkingSpacesPath = "ParkingSpaces/"; // after creating start app class set the path!!!!!!!
    public ParkingSpace parkingSpaceOnBooking;

    private ParkingSpaceControl(){
        parkingSpacesList = new ArrayList<>();
        userParkingSpacesList = new ArrayList<>();
        markerButtons = new ArrayList<>();
    }

    public static ParkingSpaceControl getInstance(){
        if (parkingSpaceControl_instance == null){
            parkingSpaceControl_instance = new ParkingSpaceControl();
        }
        return parkingSpaceControl_instance;
    }

    public ParkingSpace getParkingSpaceByAddress(String address){
        for (ParkingSpace parkingSpace:
                userParkingSpacesList) {
            if (parkingSpace.getAddress().equals(address))
                return parkingSpace;
        }
        return null;
    }

    public boolean isFitParkingSpace(ParkingSpace parkingSpace, UserCar userCar){
        return parkingSpace.getSize() >= userCar.getCarSize();
    }

    public ParkingSpace getParkingSpaceById(String parkingSpaceID){
        for (ParkingSpace parking:
             parkingSpacesList) {
            if (parking.getParkingSpaceID().contentEquals(parkingSpaceID))
                return parking;
        }
        return null;
    }

    public void updateParkingSpaceById(ParkingSpace updateParkingSpace){

    }
}
