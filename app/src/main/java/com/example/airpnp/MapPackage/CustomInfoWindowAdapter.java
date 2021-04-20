package com.example.airpnp.MapPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.airpnp.R;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mContext;
    private ParkingSpace parkingSpace;
    TextView tvAddress, tvPrice;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_window_layout, null);
    }

    private void windowAction(Marker marker, View view){
        tvAddress = view.findViewById(R.id.tvAddress);
        tvPrice = view.findViewById(R.id.tvPrice);
    }

    public void onInfoWindowClickListener(Marker marker){
        windowAction(marker, mWindow);
        tvAddress.setText("IM FUCKING RENTED");
        tvPrice.setText("");
    }


    public void onMarkerClicked(MarkerButton markerButton){
        parkingSpace = markerButton.getParkingSpace();
        windowAction(markerButton.getMarker(), mWindow);
        tvAddress.setText("Address:"+" "+parkingSpace.getAddress());
        tvPrice.setText("Price:"+" "+String.valueOf(parkingSpace.getPrice()));
    }

    @Override
    public View getInfoWindow(Marker marker) {
        windowAction(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        windowAction(marker, mWindow);
        return mWindow;
    }
}
