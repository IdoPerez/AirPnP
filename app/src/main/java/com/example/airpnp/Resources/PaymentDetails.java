package com.example.airpnp.Resources;

public class PaymentDetails {
    private String titleName;
    private String subText;

    public PaymentDetails(String hours, String price){
        this.titleName = hours;
        this.subText = price;

    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
}
