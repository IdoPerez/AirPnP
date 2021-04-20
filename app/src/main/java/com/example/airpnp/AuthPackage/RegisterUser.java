package com.example.airpnp.AuthPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.airpnp.MapPackage.MapActivity;
import com.example.airpnp.R;
import com.example.airpnp.ContactUser.SmsSender;
import com.example.airpnp.RentPackage.RentActivity;
import com.example.airpnp.UserPackage.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;



public class RegisterUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edName, edEmail, edAge,edPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();

        edName = findViewById(R.id.name);
        edEmail = findViewById(R.id.email);
        edAge = findViewById(R.id.age);
        edPassword = findViewById(R.id.password);
    }

    public void createUser(View view) {
        registerUser();
    }

    private void registerUser() {
        final String name = edName.getText().toString().trim();
        final String email = edEmail.getText().toString().trim();
        final String password = edPassword.getText().toString().trim();
        final String age = edAge.getText().toString().trim();

        if(name.isEmpty()){
            edName.setError("Full name is empty");
            edName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            edEmail.setError("Email name is empty");
            edEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edEmail.setError("Email not valid");
            edEmail.requestFocus();
        }
        if(age.isEmpty()){
            edAge.setError("Full name is empty");
            edAge.requestFocus();
            return;
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

        //progresBar = true
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final User user = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, age,email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //PROGRES BAR FALSE;
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "Register succeed", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(RegisterUser.this, "Register failed! Try again.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(RegisterUser.this, "Register failed! Try again.", Toast.LENGTH_LONG).show();
                            //progress bar false;
                        }
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