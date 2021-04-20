package com.example.airpnp.RentPackage;

public class UserCityModel {
    private City userCity;
    private int parkingSpacesCounter;
    public final int maxParkingSpacesModel = 5;

    public UserCityModel(City city){
        userCity = city;
        parkingSpacesCounter = 0;
    }

    public void checkCityModel(City city){

    }

    public City getUserCity(){
        return userCity;
    }

    public int getCounter() {
        return parkingSpacesCounter;
    }

    public void diffCity(){
        parkingSpacesCounter++;
    }

    public void updateCityModel() {
    }
}
