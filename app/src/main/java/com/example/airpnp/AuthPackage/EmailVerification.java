package com.example.airpnp.AuthPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.R;

public class EmailVerification extends AppCompatActivity {
    private FirebaseHelper firebaseHelper;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        btn = findViewById(R.id.verifyBtn);

        firebaseHelper = new FirebaseHelper();

        if(firebaseHelper.isEmailVerified()){
            btn.setText("Continue");
        }
    }

    public void startEmailVerification(View view) {
        sendEmailVerification();
    }

    public void sendEmailVerification(){
        firebaseHelper.userUpdate("258258ido@gmail.com","123456" , this);
        if(!firebaseHelper.isEmailVerified()){
            firebaseHelper.sendEmailVerification(this,btn);
        } else{
            Toast.makeText(this, "email is verified", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Authentication.class));
        }
    }
}