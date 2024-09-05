package com.boris.schuimschuld.account;

import android.content.Context;
import android.graphics.Bitmap;

import com.boris.schuimschuld.dataservices.AccountImageDataService;
import com.boris.schuimschuld.util.SerialBitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {
    private AccountImageDataService imageDataService;

    private String name;
    private Double balance;
    private ArrayList<Group> groups;
    private SerialBitmap profilePicture;

    public Account(String name, Double balance, ArrayList<Group> groups, Context context) {
        this.name = name;
        this.balance = balance;
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

    public void setGroups(ArrayList<Group> newGroups) {
        this.groups = newGroups;
    }

    public void setPicture(Context context, Bitmap picture) {
        profilePicture = new SerialBitmap(picture);
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

    public Bitmap getPicture() {
        return profilePicture.getBitmap();
    }

    public void delete(Context context) {
        imageDataService.delete(context);
    }

    public void pay() {
        this.balance -= 1;
    }
}
