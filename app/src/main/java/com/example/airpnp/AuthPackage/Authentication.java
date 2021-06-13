package com.example.airpnp.AuthPackage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airpnp.ContactUser.SmsSender;
import com.example.airpnp.Helper.ActionDone;
import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.MapPackage.MainActivityBotNav;
import com.example.airpnp.MapPackage.MapActivity;
import com.example.airpnp.R;
import com.example.airpnp.RentPackage.RentActivity;
import com.example.airpnp.UserPackage.Order;
import com.example.airpnp.UserPackage.OrdersControl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Authentication extends AppCompatActivity {
    public EditText edEmail, edPassword;
    private FirebaseAuth mAuth;

    //progress bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);

        mAuth = FirebaseAuth.getInstance();
    }

    public void Auth(View view) {
        userLogin();
    }

    public void userRegister(View view) {
        startActivity(new Intent(this, RegisterUser.class));
    }


    /**
     * checks input log in user to firebase realtime and download the user instance.
     */
    private void userLogin() {
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if(email.isEmpty()){
            edEmail.setError("Email required");
            edEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edEmail.setError("Email not valid");
            edEmail.requestFocus();
        }

        if(password.isEmpty()){
            edPassword.setError("Age name is empty");
            edPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            edPassword.setError("Min characters 6");
            edPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    edEmail.setHint("Email");
                    edPassword.setHint("Password");
                    getAllUserData();
                }
                else{
                    Toast.makeText(Authentication.this, "login failed! Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getAllUserData(){
        final OrdersControl ordersControl = OrdersControl.getInstance();
        final FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.getUserOrders(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:
                     snapshot.getChildren()) {
                    Order order = data.getValue(Order.class);
                    ordersControl.userOrdersList.add(order);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getUserData(firebaseHelper);
    }

    public void getUserData(FirebaseHelper firebaseHelper){
        firebaseHelper.getCurrentUser(new ActionDone() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(Authentication.this, MainActivityBotNav.class));
            }

            @Override
            public void onFailed() {

            }
        });
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