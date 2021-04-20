package com.example.airpnp.UserPackage;

public class ParkingSpace {

    private String parkingSpaceID;
    private String userUID;
    private String address;
    private String parkingSpaceCity;
    private String country;
    private double latitude, longitude;
    private double price;
    private int size;
    private boolean isActive;
    private boolean isAvailable;

    public ParkingSpace(String address, String city, String country, double price, int size, String userUID, double latitude, double longitude){
        this.address = address;
        this.parkingSpaceCity = city;
        this.price = price;
        this.size = size;
        this.userUID = userUID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
    }

    public String toString(){
        return "Address:"+address+", "+"City:"+", "+parkingSpaceCity+", "+"Price:"+price+", "+"UserUID:"+", "+userUID+", "+"Location:"+latitude+", "+longitude;
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
}
