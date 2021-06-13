package com.example.airpnp.UserPackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airpnp.AuthPackage.Authentication;
import com.example.airpnp.ContactUser.SmsSender;
import com.example.airpnp.Helper.ActionDone;
import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.MapPackage.MapActivity;
import com.example.airpnp.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    private static final int IMAGE_REQUEST = 1001;
    private Uri imageUri;
    private ImageView imageView;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageView = findViewById(R.id.imageView);
        downloadProfileImage();
    }

    //picking image with intent, moving the user to the gallery.
    public void pickImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();
            //uploadImage();
        }
    }
//    //מעלה תמונה לפיירבייס
//    private void uploadImage(){
//        final ProgressDialog pd = new ProgressDialog(this);
//        pd.setMessage("uploading");
//        pd.show();
//
//        String fileLocation = "userLogo/"+getUserUid();
//        firebaseHelper.uploadFile(fileLocation, imageUri, ProfileActivity.this, new ActionDone() {
//            @Override
//            public void onSuccess() {
//                downloadProfileImage();
//            }
//
//            @Override
//            public void onFailed() {
//
//            }
//        });
//        pd.dismiss();
//    }

            /*
        if(imageUri != null){
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("userLogo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Log.d("DownloadUrl", url);
                            pd.dismiss();
                            Toast.makeText(ProfileActivity.this, "uploading competed", Toast.LENGTH_LONG).show();
                            downloadImage();
                        }
                    });
                }
            });
        }
         */

    private void downloadProfileImage(){
        /*
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference()
                .child("userLogo/")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        imageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                imageView.setImageBitmap(bitmap);
                Toast.makeText(ProfileActivity.this, "download complete", Toast.LENGTH_SHORT).show();
            }
        });
         */
        //firebaseHelper.downloadFile("userLogo/"+FirebaseAuth.getInstance().getCurrentUser().getUid(),);
        String fileLocation = "userLogo/"+getUserUid();
        //firebaseHelper.downloadImage(fileLocation, imageView);
    }

    private String getUserUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        switch (id){
            case R.id.Auth:{
                intent = new Intent(this, Authentication.class);
                startActivity(intent);
                break;
            }
            case R.id.MapActiv:{
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.SmsSender:{
                intent = new Intent(this, SmsSender.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }
}