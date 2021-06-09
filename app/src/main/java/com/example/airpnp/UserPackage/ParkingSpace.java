package com.example.airpnp.UserPackage;

import java.util.ArrayList;

public class ParkingSpace {

    private String parkingSpaceID;
    private String userUID;
    private String parkingSpaceName;
    private String address;
    private String parkingSpaceCity;
    private String country;
    private double latitude, longitude;
    private double price;
    private int size;
    private boolean isActive;
    private boolean isAvailable;
    private ArrayList<String> parkingSpaceImages;
    private String parkingSpaceWorkingHours;

    public ParkingSpace(String parkingSpaceName, String address, String city, String country, double price, int size, String userUID, double latitude, double longitude, String parkingSpaceWorkingHours){
        this.parkingSpaceName = parkingSpaceName;
        this.address = address;
        this.parkingSpaceCity = city;
        this.price = price;
        this.size = size;
        this.userUID = userUID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.parkingSpaceWorkingHours = parkingSpaceWorkingHours;
        parkingSpaceImages = new ArrayList<>();
    }

    public String toString(){
        return "Address:"+address+", "+"City:"+", "+parkingSpaceCity+", "+"Price:"+price+", "+"UserUID:"+", "+userUID+", "+"Location:"+latitude+", "+longitude;
    }

    public String[] toStringArray(){
        String[] array = {parkingSpaceID, userUID, parkingSpaceName, address, parkingSpaceCity, country,
                String.valueOf(latitude), String.valueOf(longitude), String.valueOf(price), String.valueOf(size),
                String.valueOf(isActive), String.valueOf(isAvailable), parkingSpaceWorkingHours};
        return array;
    }

    public ParkingSpace(){}

    public String getParkingSpaceID() {
        return parkingSpaceID;
    }

    public void setParkingSpaceID(String parkingSpaceID) {
        this.parkingSpaceID = parkingSpaceID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserUID(){return userUID;}

    public String getAddress() {return address;}

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setActive(boolean b){
        isActive = b;
    }

    public boolean isActive() { return isActive;}

    public void setAvailable(boolean available) {isAvailable = available;}

    public boolean isAvailable(){ return isAvailable;}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getParkingSpaceCity() {
        return parkingSpaceCity;
    }

    public void setCity(String city) {
        this.parkingSpaceCity = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ArrayList<String> getParkingSpaceImages() {
        return parkingSpaceImages;
    }

    public void setParkingSpaceImages(ArrayList<String> parkingSpaceImages) {
        this.parkingSpaceImages = parkingSpaceImages;
    }

    public String getParkingSpaceName() {
        return parkingSpaceName;
    }

    public void setParkingSpaceName(String parkingSpaceName) {
        this.parkingSpaceName = parkingSpaceName;
    }

    public String getParkingSpaceWorkingHours() {
        return parkingSpaceWorkingHours;
    }

    public void setParkingSpaceWorkingHours(String parkingSpaceWorkingHours) {
        this.parkingSpaceWorkingHours = parkingSpaceWorkingHours;
    }
}
