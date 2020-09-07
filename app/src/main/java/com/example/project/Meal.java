package com.example.project;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Meal implements Serializable {

    String id;
    String name;
    Double price;
    String thingsIncluded;
    String allergy;
    ArrayList<String> imageUri;
    String uniqueNumber;

    public Meal(String id, String name, Double price, String thingsIncluded, String allergy, ArrayList<String> imageUri, String uniqueNumber) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.thingsIncluded = thingsIncluded;
        this.allergy = allergy;
        this.imageUri = imageUri;
        this.uniqueNumber = uniqueNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getThingsIncluded() {
        return thingsIncluded;
    }

    public void setThingsIncluded(String thingsIncluded) {
        this.thingsIncluded = thingsIncluded;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public ArrayList<String> getImageUri() {
        return imageUri;
    }

    public void setImageUri(ArrayList<String> imageUri) {
        this.imageUri = imageUri;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }
}
