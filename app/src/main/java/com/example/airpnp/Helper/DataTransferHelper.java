package com.example.airpnp.Helper;

import com.example.airpnp.UserPackage.Order;
import com.example.airpnp.UserPackage.ParkingSpace;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class DataTransferHelper {
    public static DataTransferHelper dataTransferHelper_instance = null;
    private ArrayList<Order> orders;
    private ArrayList<ParkingSpace> parkingSpaces;
    private ParkingSpace parkingSpace;
    private Order order;
    private String parentTAG;
    public Dictionary<String, Object> data;

    public DataTransferHelper(){
        orders = new ArrayList<>();
        parkingSpaces = new ArrayList<>();
        data = new Hashtable<>();
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

    public void setParentTAG(String TAG){
        parentTAG = TAG;
    }

    public void putParkingSpace(String key ,ParkingSpace parkingSpaceForTransfer){
        data.put(key, parkingSpaceForTransfer);
    }
    public void putOrder(String key ,Order orderForTransfer){
        data.put(key, orderForTransfer);
    }

    public Order getOrderByKey(String key){
        return (Order) data.get(key);
    }

    public ParkingSpace getParkingSpaceByKey(String key){
        return (ParkingSpace) data.get(key);
    }

    public String getParentTAG() {
        return parentTAG;
    }
}
