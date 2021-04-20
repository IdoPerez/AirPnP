package com.example.airpnp.RentPackage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.airpnp.RentPackage.Steps.RentFirstStep;
import com.example.airpnp.RentPackage.Steps.RentSecStep;

public class FragmentCollectionAdapter extends FragmentPagerAdapter {
    public FragmentCollectionAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        RentFirstStep rentFirstStep = new RentFirstStep();
        switch (position){
            case 0: {
                return rentFirstStep;
            }
            case 1: {
                RentSecStep rentSecStep = new RentSecStep();
                return rentSecStep;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
