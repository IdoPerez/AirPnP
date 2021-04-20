package com.example.airpnp.RentPackage;

public class City {
    private String country;
    private String cityName, dataBaseCityName;

    public City(String cityName, String dataBaseCityName) {
        this.cityName = cityName; this.dataBaseCityName = dataBaseCityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getEnglishCityName() {
        return dataBaseCityName;
    }

    public void setEnglishCityName(String englishCityName) {
        this.dataBaseCityName = englishCityName;
    }
}
