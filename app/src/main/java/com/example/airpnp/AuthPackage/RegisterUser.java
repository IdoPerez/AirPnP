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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.airpnp.MapPackage.MapActivity;
import com.example.airpnp.R;
import com.example.airpnp.ContactUser.SmsSender;
import com.example.airpnp.RentPackage.RentActivity;
import com.example.airpnp.UserPackage.User;

/**
 * @author Ido Perez
 * @version 0.1
 * @since 20.5.2021
 */
public class RegisterUser extends AppCompatActivity {

    private EditText edName, edEmail, edPhoneNum,edPassword;
    LinearLayout infoLayout;
    ImageButton backBtn;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        edName = findViewById(R.id.ed_name);
        edEmail = findViewById(R.id.ed_email);
        edPhoneNum = findViewById(R.id.ed_phoneNum);
        edPassword = findViewById(R.id.ed_password);
        infoLayout = findViewById(R.id.infoLayout);
        backBtn = findViewById(R.id.btn_backFromReg);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//        final ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("g");
//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, arrayList);

    }

    public void createUser(View view) {
        createUserDetails();
    }

    /**
     * input user details and put them into an array for transfer them to RentSecStep Activity.
     */
    private void createUserDetails() {
        final String name = edName.getText().toString().trim();
        final String email = edEmail.getText().toString().trim();
        final String password = edPassword.getText().toString().trim();
        final String phoneNum = edPhoneNum.getText().toString().trim();

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
        if(phoneNum.isEmpty()){
            edPhoneNum.setError("Full name is empty");
            edPhoneNum.requestFocus();
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
        String[] userDetails = {name, email, phoneNum, password};
        Intent intent = new Intent(this, RegisterSecStep.class);
        intent.putExtra("userDetails", userDetails);
        startActivity(intent);
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