package com.example.airpnp.UserPackage;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.example.airpnp.Helper.ActionDone;
import com.example.airpnp.Helper.FirebaseHelper;
import com.squareup.picasso.Picasso;

public class ProfileControl {
    public static final int IMAGE_REQUEST_CODE = 1001;
    private String imageUri;
    private ImageView profileImage;
    private FirebaseHelper firebaseHelper;
    private User user;
    private Context profileContext;

    public ProfileControl(Context context, ImageView imageView){
        firebaseHelper = new FirebaseHelper();
        profileContext = context;
        profileImage = imageView;
    }
    //

    //upload image to firebase, sets the image url. ,, !!! hard coded change the file locations.
    public void uploadImage(Uri uri){
        final ProgressDialog pd = new ProgressDialog(profileContext);
        pd.setMessage("uploading");
        pd.show();

        String fileLocation = "userLogo/"+firebaseHelper.getUserUid();
        firebaseHelper.uploadFile(fileLocation, uri, profileContext, new ActionDone() {
            @Override
            public void onSuccess() {
                downloadProfileImage();
            }

            @Override
            public void onFailed() {

            }
        });
        pd.dismiss();
    }
    // downloading image from fire base to image view, hard codded file location!!;
    public void downloadProfileImage(){
        String fileLocation = "userLogo/"+firebaseHelper.getUserUid();
        imageUri = firebaseHelper.getDownloadImageUrl(fileLocation);
        Picasso.with(profileContext).load(imageUri).into(profileImage);
    }

    public boolean isProfileImageEmpty(){
        return profileImage.getDrawable() == null;
    }
    //belongs to the method download.
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
}
