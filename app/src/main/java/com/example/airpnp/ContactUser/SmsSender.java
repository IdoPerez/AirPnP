package com.example.airpnp.ContactUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.airpnp.AuthPackage.Authentication;
import com.example.airpnp.AuthPackage.RegisterUser;
import com.example.airpnp.MapPackage.MapActivity;
import com.example.airpnp.R;
import com.example.airpnp.RentPackage.RentActivity;

public class SmsSender extends AppCompatActivity {

    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1 ;
    Button sendBtn;
    EditText txtphoneNo;
    EditText txtMessage;
    String phoneNo;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_sender);

        sendBtn = (Button) findViewById(R.id.btnSendSMS);
        txtphoneNo = (EditText) findViewById(R.id.editText);
        txtMessage = (EditText) findViewById(R.id.editText2);
        sendBtn.setEnabled(false);
        if(checkPermission(Manifest.permission.SEND_SMS)){
            sendBtn.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }


    public void send(View view) {
        String phoneNum = txtphoneNo.getText().toString();
        String message = txtMessage.getText().toString();

        if(checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNum, null, message, null, null);
            Toast.makeText(this, "sms sent", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return(check == PackageManager.PERMISSION_GRANTED);
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
            case R.id.RentActivity:{
                intent = new Intent(this, RentActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.register:{
                intent = new Intent(this, RegisterUser.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }
}