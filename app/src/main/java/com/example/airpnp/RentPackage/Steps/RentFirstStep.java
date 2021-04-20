package com.example.airpnp.RentPackage.Steps;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.airpnp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RentFirstStep extends Fragment {

    private TextInputLayout tdPrice,tdLocation,tdTime;
    private TextInputEditText edPrice;

    public RentFirstStep() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rent_first_step, container, false);
        /*
        tdPrice = view.findViewById(R.id.layoutPrice);
        tdLocation = view.findViewById(R.id.layoutLocation);
        tdTime = view.findViewById(R.id.layoutTime);
         */
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tdPrice = view.findViewById(R.id.layoutPrice);
        tdLocation = view.findViewById(R.id.autocomplete_fragment);
        tdTime = view.findViewById(R.id.layoutTime);
    }

    public String[] getInputText(){
        String[] input = new String[3];
        //Eror: tdPrice = Null;
        input[0] = tdPrice.getEditText().getText().toString().trim();
        /*
        input[1] = tdLocation.getEditText().getText().toString().trim();
        input[2] = tdTime.getEditText().getText().toString().trim();

         */
        return input;
    }
}