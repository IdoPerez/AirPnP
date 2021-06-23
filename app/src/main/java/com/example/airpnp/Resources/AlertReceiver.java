package com.example.airpnp.Resources;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.example.airpnp.Helper.DataTransferHelper;
import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.UserPackage.Order;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class AlertReceiver extends BroadcastReceiver {
    FirebaseHelper firebaseHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        //DataTransferHelper dataTransferHelper = DataTransferHelper.getInstance();
        //Order order = dataTransferHelper.getOrderByKey("order");
        String id = intent.getStringExtra("orderId");
        firebaseHelper = new FirebaseHelper();
        if (id != null)
            FirebaseHelper.ordersRef.child(firebaseHelper.getUserUid()).child(id).child("active").setValue(false);
        Toast.makeText(context, "Hey! parking time is over.", Toast.LENGTH_LONG).show();
    }
}
