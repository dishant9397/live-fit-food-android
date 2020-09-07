package com.example.project;

import android.util.Log;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;

public class Order implements Serializable {

    String id;
    String user;
    Double orderAmount;
    LocalDate orderDate;
    ArrayList<LocalDate> pickUpDates = new ArrayList<LocalDate>();
    String mealId;
    String mealName;
    Double mealPrice;
    String mealThingsIncluded;
    String orderNumber;

    public Order(String user, Double orderAmount, String orderDate, String orderNumber) {
        this.user = user;
        this.orderAmount = orderAmount;
        this.orderDate = getLocalDate(orderDate);
        this.orderNumber = orderNumber;
    }

    public Order(String id, String user, Double orderAmount, String orderDate, ArrayList<String> pickUpDates, String mealId, String mealName, Double mealPrice, String mealThingsIncluded, String orderNumber) {
        this.id = id;
        this.user = user;
        this.orderAmount = orderAmount;
        this.orderDate = getLocalDate(orderDate);
        for (int i = 0; i < pickUpDates.size(); i++) {
            LocalDate localDate = getLocalDate(pickUpDates.get(i));
            this.pickUpDates.add(localDate);
        }
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealPrice = mealPrice;
        this.mealThingsIncluded = mealThingsIncluded;
        this.orderNumber = orderNumber;
    }

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public ArrayList<LocalDate> getPickUpDates() {
        return pickUpDates;
    }

    public void setPickUpDates(ArrayList<LocalDate> pickUpDates) {
        this.pickUpDates = pickUpDates;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public Double getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(Double mealPrice) {
        this.mealPrice = mealPrice;
    }

    public String getMealThingsIncluded() {
        return mealThingsIncluded;
    }

    public void setMealThingsIncluded(String mealThingsIncluded) {
        this.mealThingsIncluded = mealThingsIncluded;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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
