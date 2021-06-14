package com.example.airpnp.UserPackage;

import com.example.airpnp.Helper.FirebaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ido Perez
 * @version 0.1
 * @since 25.5.2021
 */
public class OrdersControl {
    private static OrdersControl ordersControl_instance = null;
    public ArrayList<Order> userOrdersList;
    public Map<String, User> orderUsers;
    public static final String ordersPath =  "Orders/";
    private FirebaseHelper firebaseHelper;

    private OrdersControl(){
        firebaseHelper = new FirebaseHelper();
        userOrdersList = new ArrayList<>();
        orderUsers = new HashMap<>();
    }

    /**
     * create instance if null for creating singleton.
     * @return
     */
    public static OrdersControl getInstance(){
        if (ordersControl_instance == null){
            ordersControl_instance = new OrdersControl();
        }
        return ordersControl_instance;
    }

    /**
     * creats new order and upload her to fire base.
     * @param parkingSpace
     * @param parkingHours
     */
    public void createNewOrder(ParkingSpace parkingSpace, int parkingHours){
        Order order = new Order(parkingSpace.getParkingSpaceID(), parkingSpace.getUserUID(), firebaseHelper.getUserUid(), parkingHours, null , null, true);
        String orderPath = ordersPath+firebaseHelper.getUserUid();
        firebaseHelper.uploadOrder(order);
        userOrdersList.add(order);
    }
}
