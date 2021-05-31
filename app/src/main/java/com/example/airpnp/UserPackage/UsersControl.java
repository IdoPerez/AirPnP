package com.example.airpnp.UserPackage;

import com.example.airpnp.Helper.FirebaseHelper;

import java.util.ArrayList;

public class UsersControl {
    public static UsersControl usersControl_instance = null;
    public User currentUser;
    private User orderUser;
    private FirebaseHelper firebaseHelper;


    private UsersControl(){

    }

    public static UsersControl getInstance(){
        if (usersControl_instance == null){
            usersControl_instance = new UsersControl();
        }
        return usersControl_instance;
    }
}
