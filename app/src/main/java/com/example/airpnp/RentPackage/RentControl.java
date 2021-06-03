package com.example.airpnp.RentPackage;

import android.content.Context;
import android.util.Log;

import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.LocationPackage.LocationControl;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;
import com.google.android.gms.maps.model.LatLng;


/* // google translate implementations
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

 */

public class RentControl {
    private CityControl cityControl;
    private LocationControl locationControl;
    private FirebaseHelper firebaseHelper;
    private Context context;
    private UserCityModel userCityModel;
    private boolean connected;
    ParkingSpaceControl parkingSpaceControl;
//    private Translate translate;


    public RentControl(Context context){
        locationControl = new LocationControl(context);
        firebaseHelper = new FirebaseHelper();
        cityControl = CityControl.getInstance();
        this.context = context;
         parkingSpaceControl = ParkingSpaceControl.getInstance();
        //translate = TranslateOptions.getDefaultInstance().getService();
    }

    public void createParkingSpace(String parkingSpaceName, double price, String address, int sizeNum, String workingHours){

        String[] splitAddress = address.split(",");
        String parkingSpaceCity = splitAddress[1];
        parkingSpaceCity = parkingSpaceCity.replaceAll("\\s+","");
        Log.v("ParkingSpaceCity", parkingSpaceCity);
        String parkingSpaceCountry = splitAddress[2].replaceAll("\\s+","");
        Log.v("parkingSpaceContry", parkingSpaceCountry);
        Log.v("Path",ParkingSpaceControl.parkingSpacesPath+parkingSpaceCity);

        //String cityPath = null;
        LatLng addressLatlng = locationControl.getLocationFromAddress(address);
        ParkingSpace parkingSpace = new ParkingSpace(parkingSpaceName,
                address,
                parkingSpaceCity,
                parkingSpaceCountry,
                price,
                sizeNum,
                firebaseHelper.getUserUid(),
                addressLatlng.latitude,
                addressLatlng.longitude, workingHours);

        parkingSpaceControl.userParkingSpacesList.add(parkingSpace);
        Log.v("ParkingSpace", parkingSpace.toString());
        //firebaseHelper.uploadParkingSpace(parkingSpace,ParkingSpaceControl.parkingSpacesPath+parkingSpaceCountry);
        firebaseHelper.uploadParkingSpace(parkingSpace,ParkingSpaceControl.parkingSpacesPath);
        
        /* google translate implementations
        char check = address.charAt(0);
        if (check >= 'a' && check <= 'z'){
            cityPath = parkingSpaceCity;
        } else{
            if (checkInternetConnection()){
                getTranslateService();
                cityPath = translate(parkingSpaceCity);
            }
            else Toast.makeText(context, R.string.no_connection, Toast.LENGTH_LONG).show();
        }
        if (cityPath != null){
            firebaseHelper.uploadParkingSpace(parkingSpace, ParkingSpaceControl.parkingSpacesPath+cityPath);
            Log.v("ParkingSpaceFinalPayh",ParkingSpaceControl.parkingSpacesPath+cityPath);
        }

         */

    }
    /*
    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = context.getResources().openRawResource(R.raw.credentials)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }
    public String translate(String textForTranslate) {

        //Creating the translate options.
        Translation translation = translate.translate(textForTranslate, Translate.TranslateOption.targetLanguage("en"), Translate.TranslateOption.model("base"));

        //Translated text and original text are set to TextViews:
        //translatedTv.setText(translatedText);
        return translation.getTranslatedText();
    }
    public boolean checkInternetConnection() {

        //Check internet connection:
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    } */


    private String[] splitAddressLocation(String address, int index){
        return address.split(",");
    }
}
