package com.example.project;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PickedUpOrder implements Serializable {

    String orderNumber;
    String user;
    int pickupNumber;
    LocalDate pickupDate;

    public PickedUpOrder(String orderNumber, String user, int pickupNumber, String pickupDate) {
        this.orderNumber = orderNumber;
        this.user = user;
        this.pickupNumber = pickupNumber;
        this.pickupDate = getLocalDate(pickupDate);
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPickupNumber() {
        return pickupNumber;
    }

    public void setPickupNumber(int pickupNumber) {
        this.pickupNumber = pickupNumber;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getLocalDate(String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        }
        catch (DateTimeParseException e) {
            e.printStackTrace();
            localDate = null;
        }
        return localDate;
    }
}
