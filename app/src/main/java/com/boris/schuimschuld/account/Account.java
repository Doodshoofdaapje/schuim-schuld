package com.boris.schuimschuld.account;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.boris.schuimschuld.dataservices.AccountImageDataService;
import com.boris.schuimschuld.util.SerialBitmap;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;

public class Account implements Serializable {
    private AccountImageDataService imageDataService;

    private String name;
    private Double balance;
    private Double consumptionCount;
    private ArrayList<Group> groups;
    private transient Bitmap profilePicture;

    public Account(Context context, String name, Double balance, Double consumptionCount, ArrayList<Group> groups) {
        this.name = name;
        this.balance = balance;
        this.consumptionCount = consumptionCount;
        this.groups = groups;
        this.imageDataService = new AccountImageDataService(name);
        this.profilePicture = imageDataService.load(context);
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setBalance(Double newBalance) {
        this.balance = newBalance;
    }

    public void setConsumptionCount(Double newConsumptionCount) {
        this.consumptionCount = newConsumptionCount;
    }

    public void setGroups(ArrayList<Group> newGroups) {
        this.groups = newGroups;
    }

    public void setPicture(Context context, Bitmap picture) {
        profilePicture = picture;
        imageDataService.save(context, picture);
    }

    public ArrayList<Group> getGroups() {
        return this.groups;
    }

    public String getName() {
        return this.name;
    }

    public Double getBalance() {
        return this.balance;
    }

    public Double getConsumptionCount() {
        return this.consumptionCount;
    }

    public Bitmap getPicture() {
        return profilePicture;
    }

    public Uri getPictureURI(Context context) {
        return imageDataService.getURI(context);
    }

    public void delete(Context context) {
        imageDataService.delete(context);
    }

    public void pay() {
        this.balance -= 1;
        this.consumptionCount += 1;
    }
}
