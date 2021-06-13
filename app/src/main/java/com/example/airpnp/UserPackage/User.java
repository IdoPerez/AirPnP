package com.example.airpnp.UserPackage;

import java.util.ArrayList;

//fullName, tv_phoneNum, email;
public class User {

    private String userID;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String logImage;
    private ArrayList<UserCar> userCars;
    private ArrayList<String> parkingSpaceID;


    public User(String UID, String fullName, String email, String phoneNumber, ArrayList<UserCar> userCars){
        this.userID = UID; this.fullName = fullName; this.email = email;
        this.phoneNumber = phoneNumber;
        parkingSpaceID = new ArrayList<String>();
        this.userCars = userCars;
    }

    public User(){

    }


    public String getUserID(){return userID;}

    public void setImage(String bitmap){
        this.logImage = bitmap;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogImage() {
        return logImage;
    }

    public ArrayList<String> getParkingSpaceID() {return parkingSpaceID;}

    public void addParkingSpace(String id){parkingSpaceID.add(id);}

    public ArrayList<UserCar> getUserCars() {
        return userCars;
    }

    public void setUserCars(ArrayList<UserCar> userCars) {
        this.userCars = userCars;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
