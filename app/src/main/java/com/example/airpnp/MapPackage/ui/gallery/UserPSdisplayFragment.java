package com.example.airpnp.MapPackage.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.airpnp.R;
import com.example.airpnp.UserPackage.ParkingSpace;
import com.example.airpnp.UserPackage.ParkingSpaceControl;

import java.util.ArrayList;


public class UserPSdisplayFragment extends Fragment implements AdapterView.OnItemClickListener{

    ListView parkingSpacesListView;
    TextView listHeader;
    private ParkingSpaceControl parkingSpaceControl;
    private ArrayList<String> parkingSpacesNames;
    ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        parkingSpacesListView = root.findViewById(R.id.parkingSpacesList);
        listHeader = root.findViewById(R.id.listHeader);
        parkingSpaceControl = ParkingSpaceControl.getInstance();

        parkingSpacesNames = new ArrayList<>();
        for (ParkingSpace parkingSpace:
             parkingSpaceControl.userParkingSpacesList) {
            parkingSpacesNames.add(parkingSpace.getAddress());
        }
        parkingSpacesListView.setOnItemClickListener(this);

        adapter = new ArrayAdapter<String>(requireContext(), R.layout.support_simple_spinner_dropdown_item, parkingSpacesNames);
        parkingSpacesListView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parkingSpaceControl.userParkingSpacesList.size() > position){
            ParkingSpace parkingSpace = parkingSpaceControl.userParkingSpacesList.get(position);
        }
    }
}