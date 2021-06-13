package com.example.airpnp.UserPackage;

public class Order {

    private String orderID;
    private String parkingSpaceID;
    private String tenantUID;
    private String currentUserUid;
    private String checkInTime, checkOutTime;
    private int parkingHours;
    private double price;
    private boolean active;

    public Order(String parkingSpaceID, String tenantUID, String currentUserUid, int parkingHours, String checkInTime, String checkOutTime, boolean active){
        this.parkingSpaceID = parkingSpaceID;
        this.tenantUID = tenantUID;
        this.currentUserUid = currentUserUid;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.parkingHours = parkingHours;
        this.active = active;
    }

    public Order(){

    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getParkingSpaceID() {
        return parkingSpaceID;
    }

    public void setParkingSpaceID(String parkingSpaceID) {
        this.parkingSpaceID = parkingSpaceID;
    }

    public String getTenantUID() {
        return tenantUID;
    }

    public void setTenantUID(String tenantUID) {
        this.tenantUID = tenantUID;
    }

    public String getCurrentUserUid() {
        return currentUserUid;
    }

    public void setCurrentUserUid(String currentUserUid) {
        this.currentUserUid = currentUserUid;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getParkingHours() {
        return parkingHours;
    }

    public void setParkingHours(int parkingHours) {
        this.parkingHours = parkingHours;
    }
}
