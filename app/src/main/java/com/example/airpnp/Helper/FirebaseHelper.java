package com.example.airpnp.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.airpnp.LocationPackage.LocationControl;
import com.example.airpnp.UserPackage.Order;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseHelper {

    private String fileUrl;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDataBase;
    private final StorageReference mStorageRef;
    private String imageUrl;

    public static String CURRENT_USER_UID;

    public FirebaseHelper(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        CURRENT_USER_UID = user.getUid();
    }

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

    public void sendEmailVerification(final Context context, final Button btn){
        btn.setEnabled(false);
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
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
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    public boolean isEmailVerified() {
        return user.isEmailVerified();
    }

    public String getUserUid(){
        return user.getUid();
    }

    public void uploadParkingSpace(ParkingSpace parkingSpace, String path){
        String parkingSpaceKey = getObjectKey(path);
        parkingSpace.setParkingSpaceID(parkingSpaceKey);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child(path).child(parkingSpaceKey).setValue(parkingSpace);
        Log.v("ParkingSpace Path",parkingSpace.toString()+", "+ "Path:"+path+parkingSpaceKey);
    }

    public void uploadOrder(Order order, String path){
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child(path).child(order.getOrderID()).setValue(order);
        Log.v("Order Path",order.toString()+","+"order Path:"+path);
    }

    // final Map<ParkingSpace, Boolean> parkingSpacesOnMap
    public void getAllParkingSpacesInCity(LocationControl locationControl, final ActionDone actionDone){
        final ParkingSpaceControl parkingSpaceControl = ParkingSpaceControl.getInstance();
        //String path = ParkingSpaceControl.parkingSpacesPath+locationControl.getCountry();
        String path = ParkingSpaceControl.parkingSpacesPath;
        mDataBase = FirebaseDatabase.getInstance().getReference();
        Log.v("Path", path);
        //orderByChild("parkingSpaceCity").equalTo(locationControl.getUserCityName());
        //Query query = mDataBase.orderByChild("parkingSpaceCity").equalTo(locationControl.getUserCityName());;
        Log.v("userCityName:", locationControl.getUserCityName());
        mDataBase.child(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parkingSpaceControl.parkingSpacesList.clear();
                parkingSpaceControl.userParkingSpacesList.clear();
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
    }

    public String getObjectKey(String fileLocation){
        return mDataBase.child(fileLocation).push().getKey();
    }
}
