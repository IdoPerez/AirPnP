package com.example.airpnp.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.airpnp.LocationPackage.LocationControl;
import com.example.airpnp.UserPackage.Order;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.example.airpnp.UserPackage.User;
import com.example.airpnp.UserPackage.UserInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * @author Ido Perez
 * @version 0.1
 * @since 10.1.2021
 */
public class FirebaseHelper {

    //firebase instances:
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    public static final String USER_UID = firebaseUser.getUid();
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    //data base references:
    public static DatabaseReference usersRef = firebaseDatabase.getReference("Users");
    public static DatabaseReference parkingSpacesRef = firebaseDatabase.getReference("ParkingSpaces");
    public static DatabaseReference ordersRef = firebaseDatabase.getReference("Orders");
    public static StorageReference userLogoRef = FirebaseStorage.getInstance().getReference("UserLogo");

    public static ChildEventListener childEventListener;

//    public static boolean isParkingSpacesListenerOn = false;

    public FirebaseHelper(){

    }

/*
    //FirebaseStorage.getInstance().getReference().child("userLogo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    public void uploadFile(final String fileLocation, Uri imageUri, final Context context, final ActionDone actionDone){
        if(imageUri != null){
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(fileLocation);
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            fileUrl = uri.toString();

                            Log.d("DownloadUrl", fileUrl);
                            Toast.makeText(context, "uploading completed", Toast.LENGTH_LONG).show();
                            actionDone.onSuccess();
                        }
                    });
                }
            });
        }
    }
    //.child("userLogo/")
    //                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    public void downloadFile(String fileLocation){
        mStorageRef.child(fileLocation);
        mStorageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
               Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
            }
        });
    }

    public void downloadImage(String fileLocation, String url){
        mStorageRef.child(fileLocation);
        mStorageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);

            }
        });
    }
    public String getDownloadImageUrl(String fileLocation){
        mStorageRef.child(fileLocation).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for fileLocation'
                imageUrl = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        return imageUrl;
    }

 */

    public void sendEmailVerification(final Context context, final Button btn){
        btn.setEnabled(false);
        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Please verify email", Toast.LENGTH_LONG).show();
                    btn.setText("Continue");
                }
                else{
                    Toast.makeText(context, "Theres been an error please try again", Toast.LENGTH_LONG).show();
                }
                btn.setEnabled(true);
            }
        });
    }

    public void userUpdate(String email, String password, final Context activity){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //redirect email verification
                    Toast.makeText(activity, "user updated", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(activity, "theres been an error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public boolean isEmailVerified() {
        return firebaseUser.isEmailVerified();
    }

    public String getUserUid(){
        return firebaseUser.getUid();
    }

    /**
     * upload parking space to fire base real time.
     * @see com.example.airpnp.RentPackage.RentActivity
     * @param parkingSpace
     */
    public void uploadParkingSpace(ParkingSpace parkingSpace){
        String parkingSpaceKey;
        if (parkingSpace.getParkingSpaceID() == null){
             parkingSpaceKey = parkingSpacesRef.push().getKey();
            parkingSpace.setParkingSpaceID(parkingSpaceKey);
        } else
            parkingSpaceKey = parkingSpace.getParkingSpaceID();

        parkingSpacesRef.child(parkingSpaceKey).setValue(parkingSpace);
        Log.v("ParkingSpace",parkingSpace.toString());
    }

    /**
     * upload a new order
     * @see com.example.airpnp.MapPackage.PaymentChoiceFragment
     * @param order
     */
    public void uploadOrder(Order order, final ActionDone actionDone){
        order.setOrderID(ordersRef.push().getKey());
        ordersRef.child(firebaseUser.getUid()).child(order.getOrderID()).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                actionDone.onSuccess();
            }
        });
        Log.v("Order Path",order.toString());
    }

    // final Map<ParkingSpace, Boolean> parkingSpacesOnMap

    /**
     * gets all the parking spaces in the user current city.
     * @see LocationControl
     * @see ParkingSpaceControl
     * @see com.example.airpnp.MapPackage.MapActivity
     * @see com.example.airpnp.MapPackage.MapControl
     * @see #childEventListener
     */
    public void getAllParkingSpaces(){
        //String path = ParkingSpaceControl.parkingSpacesPath+locationControl.getCountry();
        final ParkingSpaceControl parkingSpaceControl = ParkingSpaceControl.getInstance();
        //Log.v("userCityName:", locationControl.getUserCityName());
        parkingSpacesRef = FirebaseDatabase.getInstance().getReference("ParkingSpaces");
        Query query = parkingSpacesRef;
        if (childEventListener != null)
            query.addChildEventListener(childEventListener);
        //isParkingSpacesListenerOn = true;
    }

    /*
                @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parkingSpaceControl.parkingSpacesList.clear();
//                parkingSpaceControl.userParkingSpacesList.clear();
                for (DataSnapshot data :
                        snapshot.getChildren()) {
                    ParkingSpace parkingSpace = data.getValue(ParkingSpace.class);
                        parkingSpaceControl.parkingSpacesList.add(parkingSpace);
                }
                actionDone.onSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
        Query query = parkingSpaceRef.orderByChild("available").equalTo(true);
        query.addValueEventListener(parkingSpaceListener);
         */


    /**
     * filter from data base user parking spaces and gets them.
     * @see ParkingSpaceControl
     * @param actionDone
     */
    public void getUserParkingSpaces(final ActionDone actionDone){
        final ParkingSpaceControl parkingSpaceControl = ParkingSpaceControl.getInstance();
        Query query = parkingSpacesRef.orderByChild("userUID").equalTo(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parkingSpaceControl.userParkingSpacesList.clear();
                for (DataSnapshot data :
                        snapshot.getChildren()) {
                    ParkingSpace parkingSpace = data.getValue(ParkingSpace.class);
                    parkingSpaceControl.userParkingSpacesList.add(parkingSpace);
                }
                actionDone.onSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                actionDone.onFailed();
            }
        });
    }

    public void getUserOnStartUserData(){

    }

    public void getUserOrders(ValueEventListener valueEventListener){
        Query query = ordersRef.child(firebaseUser.getUid());
        query.addValueEventListener(valueEventListener);
    }


    /**
     * update parking space status in real time.
     * @param parkingSpace
     * @param state true - active false- inactive
     */
    public void setParkingSpaceActive(ParkingSpace parkingSpace, boolean state){
        parkingSpacesRef.child(parkingSpace.getParkingSpaceID())
                .child("active")
                .setValue(state);
        parkingSpace.setActive(state);
    }

    /**
     * gets from data base the user instance by uid.
     * @see User
     * @see UserInstance
     */
    public void getUserById(String UID ,final GetUserOnActionDone actionDone){
        usersRef.child(UID).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                actionDone.singleUserRead(user);
            }
        });
//        usersRef.orderByChild("userID").equalTo(UID)
//        databaseReference.child("Users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            User user;
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
//                    user = childSnapshot.getValue(User.class);
//                }
//                actionDone.singleUserRead(user);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public interface GetUserOnActionDone {
         void singleUserRead(User user);
         void groupUserRead(ArrayList<User> users);
    }
}
