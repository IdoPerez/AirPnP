package com.example.airpnp.RentPackage;

import java.util.ArrayList;
//useless class(?) think if use that class.
public class CityControl {
    public static CityControl cityControl_instance = null;
    public static ArrayList<City> cities;
    private City currentCity;

    public CityControl() {
        cities = new ArrayList<>();
    }

    public static CityControl getInstance() {
        if (cityControl_instance == null) {
            cityControl_instance = new CityControl();
        }
        return cityControl_instance;
    }

    //checks if this city exists. yes return the city object no returns null
    /*
    public boolean isExists(String stCity){
        for (City city: cities){
            if (city.getCityName().replaceAll("\\s+","").equals(stCity.replaceAll("\\s+",""))){
                currentCity = city;
                return true;
            }
        }
        return false;
    }

    public City getCurrentCity() {
        return currentCity;
    }
}

     */
}
