package com.example.airpnp.UserPackage;

import java.util.ArrayList;

//fullName, age, email;
public class User {

    private String userID;
    private String fullName;
    private String age;
    private String email;
    private String logImage;
    private ArrayList<UserCar> userCars;
    private ArrayList<String> parkingSpaceID;


    public User(String UID, String fullName, String age, String email){
        this.userID = UID; this.fullName = fullName; this.age = age; this.email = email;
        parkingSpaceID = new ArrayList<String>();
        userCars = new ArrayList<UserCar>();
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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
}
