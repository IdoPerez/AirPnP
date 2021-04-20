package com.example.airpnp.UserPackage;

public class Order {

    private String orderID;
    private String parkingSpaceID;
    private String tenantUID;
    private String renterUID;
    private String checkInTime, checkOutTime;

    public Order(String parkingSpaceID, String tenantUID, String renterUID, String checkInTime, String checkOutTime){
        this.parkingSpaceID = parkingSpaceID; this.tenantUID = tenantUID; this.renterUID = renterUID; this.checkInTime = checkInTime; this.checkOutTime = checkOutTime;
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

    public String getRenterUID() {
        return renterUID;
    }

    public void setRenterUID(String renterUID) {
        this.renterUID = renterUID;
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
}
