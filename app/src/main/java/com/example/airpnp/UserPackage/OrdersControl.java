package com.example.airpnp.UserPackage;

import com.example.airpnp.Helper.FirebaseHelper;

import java.util.ArrayList;

public class OrdersControl {
    private static OrdersControl ordersControl_instance = null;
    public ArrayList<Order> userOrdersList;
    public static final String ordersPath =  "Orders/";
    private FirebaseHelper firebaseHelper;

    private OrdersControl(){
        firebaseHelper = new FirebaseHelper();
        userOrdersList = new ArrayList<>();
    }

    public static OrdersControl getInstance(){
        if (ordersControl_instance == null){
            ordersControl_instance = new OrdersControl();
        }
        return ordersControl_instance;
    }

    public void placeNewOrder(String parkingSpaceID, String tenantUID, String renterUID, boolean active){
        Order order = new Order(parkingSpaceID, tenantUID, renterUID, null , null, active);
        String orderPath = ordersPath+firebaseHelper.getUserUid();
        order.setOrderID(firebaseHelper.getObjectKey(orderPath));
        firebaseHelper.uploadOrder(order, orderPath);
    }
}
