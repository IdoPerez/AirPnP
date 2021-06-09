package com.example.airpnp.Helper;

import com.example.airpnp.UserPackage.Order;
import com.example.airpnp.UserPackage.ParkingSpace;

import java.util.ArrayList;

public class DataTransferHelper {
    public static DataTransferHelper dataTransferHelper_instance = null;
    private ArrayList<Order> orders;
    private ArrayList<ParkingSpace> parkingSpaces;
    private ParkingSpace parkingSpace;
    private Order order;

    public DataTransferHelper(){
        orders = new ArrayList<>();
        parkingSpaces = new ArrayList<>();
    }

    public static DataTransferHelper newInstance(){
        dataTransferHelper_instance = new DataTransferHelper();
        return dataTransferHelper_instance;
    }

    public static DataTransferHelper getInstance(){
        if (dataTransferHelper_instance != null)
            return dataTransferHelper_instance;
        return  newInstance();
    }

    public void putParkingSpace(ParkingSpace parkingSpaceForTransfer){
        parkingSpace = parkingSpaceForTransfer;
    }
    public void putOrder(Order orderForTransfer){
        order = orderForTransfer;
    }

    public Order getOrder(){
        return order;
    }

    public ParkingSpace getParkingSpace(){
        return parkingSpace;
    }
}
