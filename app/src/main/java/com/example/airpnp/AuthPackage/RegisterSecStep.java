package com.example.airpnp.AuthPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.airpnp.Helper.FirebaseHelper;
import com.example.airpnp.R;
import com.example.airpnp.Resources.CustomAdapterCarList;
import com.example.airpnp.Resources.CustomAdapterSizeMenu;
import com.example.airpnp.Resources.SizeItem;
import com.example.airpnp.UserPackage.User;
import com.example.airpnp.UserPackage.UserCar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
/**
 * @author Ido Perez
 * @version 0.1
 * @since 20.5.2021
 */
public class RegisterSecStep extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText edCarName, edCarNumber;
    ListView userCarList;
    ArrayList<UserCar> userCars;
    ArrayList<SizeItem> sizeItems = new ArrayList<>();
    ImageButton addCarBtn;
    Spinner sizeSpinner;

    String carName, carNumber;
    String username, email, phoneNum,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sec_step);

        Intent intent = getIntent();
        String[] details = intent.getStringArrayExtra("userDetails");
        username = details[0];
        email = details[1];
        phoneNum = details[2];
        password = details[3];

        mAuth = FirebaseAuth.getInstance();

        userCarList = findViewById(R.id.userCar_list);
        addCarBtn = findViewById(R.id.addCar_btn);
        edCarName = findViewById(R.id.ed_carName);
        edCarNumber = findViewById(R.id.ed_carNumber);
        sizeSpinner = (Spinner) findViewById(R.id.spinnerSize);

        sizeItems.add(new SizeItem(R.drawable.car_icon_a));
        sizeItems.add(new SizeItem(R.drawable.van_icon));
        sizeItems.add(new SizeItem(R.drawable.truck_icon));

        final CustomAdapterSizeMenu adapterSizeMenu = new CustomAdapterSizeMenu(this, sizeItems);
        //adapterSizeMenu.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapterSizeMenu);

        userCars = new ArrayList<>();
        final CustomAdapterCarList customAdapterCarList = new CustomAdapterCarList(this, userCars);
        userCarList.setAdapter(customAdapterCarList);
        addCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carName = edCarName.getText().toString();
                carNumber = edCarNumber.getText().toString();
                if ((!carName.isEmpty() && !carNumber.isEmpty()) || carEditTextCheck()){
                    userCars.add(new UserCar(carName, carNumber, sizeSpinner.getSelectedItemPosition()));
                    edCarName.getText().clear();
                    edCarName.setHint("Car name");
                    edCarNumber.getText().clear();
                    edCarNumber.setHint("Car number");
                    customAdapterCarList.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * calls the method createUserFireBase on click if the input check ok.
     * @param view
     */
    public void createUser(View view) {
        if (carEditTextCheck())
            createUserFireBase();
    }

    /**
     * checks if the car arrays are empty or if the user filled only half of the fields.
     * @return true if the input check ok. else false.
     */
    private boolean carEditTextCheck(){
        String name = edCarName.getText().toString();
        String number = edCarNumber.getText().toString();

        if (!name.isEmpty() && !number.isEmpty()){
            Toast.makeText(this, "Press add to create new car", Toast.LENGTH_LONG).show();
            addCarBtn.requestFocus();
            return false;
        }
        else if (userCars.isEmpty()){
            if (name.isEmpty()){
                edCarName.requestFocus();
                edCarName.setError("You must enter car name");
            } else{
                edCarNumber.requestFocus();
                edCarNumber.setError("You must enter car number");
            }
            return false;
        }
//        else{
//            if (name.isEmpty() && !carNumber.isEmpty()){
//                edCarName.requestFocus();
//                edCarName.setError("You must enter car name");
//                return false;
//            }
//            else if(carNumber.isEmpty() && !name.isEmpty()){
//                edCarNumber.requestFocus();
//                edCarNumber.setError("You must enter car number");
//            }
//            else{
//                edCarName.getText().clear();
//                edCarName.setHint("Car name");
//                edCarNumber.getText().clear();
//                edCarNumber.setHint("Car number");
//                return true;
//            }
//        }
    return true;
    }

    /**
     * creates new user in fire base auth and new instance of user in fire base real time.
     */
    private void createUserFireBase(){
        final ProgressDialog pd= ProgressDialog.show(this,"Login","Creating user...",true);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final User user = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), username, email, phoneNum, userCars);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pd.dismiss();
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterSecStep.this, "Register succeed", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegisterSecStep.this, Authentication.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(RegisterSecStep.this, "Register failed! Try again.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(RegisterSecStep.this, "Register failed! Try again.", Toast.LENGTH_LONG).show();
                            //progress bar false;
                        }
                    }
                });
    }
}