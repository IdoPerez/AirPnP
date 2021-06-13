package com.example.airpnp.Helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseControl {
    public static FirebaseAuth auth;
    public static FirebaseUser user;

    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static DatabaseReference usersRef = firebaseDatabase.getReference("User");
    public static DatabaseReference parkingSpacesRef = firebaseDatabase.getReference("ParkingSpaces");
    public static DatabaseReference ordersRef = firebaseDatabase.getReference("Orders");
    public static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("UserLogo");

    public FirebaseControl(){

    }
}
