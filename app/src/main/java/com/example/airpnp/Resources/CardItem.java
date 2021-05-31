package com.example.airpnp.Resources;

import android.view.View;
import android.widget.Button;

import com.example.airpnp.UserPackage.Order;

public class CardItem {
    private Order order;
    private String imageUrl;
    private boolean aboardChecked;
    private View.OnClickListener aboardOnClick;

    public CardItem(Order order, String imageUrl, View.OnClickListener aboardOnClick){
        aboardChecked = false;
        this.order = order;
        this.imageUrl = imageUrl;
        this.aboardOnClick = aboardOnClick;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAboardChecked() {
        return aboardChecked;
    }

    public void setAboardChecked(boolean aboardChecked) {
        this.aboardChecked = aboardChecked;
    }

    public View.OnClickListener getAboardOnClick() {
        return aboardOnClick;
    }

    public void setAboardOnClick(View.OnClickListener aboardOnClick) {
        this.aboardOnClick = aboardOnClick;
    }
}
