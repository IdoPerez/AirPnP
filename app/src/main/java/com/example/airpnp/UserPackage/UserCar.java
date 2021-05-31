package com.example.airpnp.UserPackage;

public class UserCar {
    private String carName;
    private String carNumber;
    private int carSize;

    public UserCar(String name, String number, int size){
        carName = name;
        carNumber = number;
        carSize = size;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public int getCarSize() {
        return carSize;
    }

    public void setCarSize(int carSize) {
        this.carSize = carSize;
    }
}
