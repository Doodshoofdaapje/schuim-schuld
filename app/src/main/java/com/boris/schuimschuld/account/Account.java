package com.boris.schuimschuld.account;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.boris.schuimschuld.dataservices.AccountImageDataService;
import java.util.UUID;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;

public class Account implements Serializable {
    private AccountImageDataService imageDataService;

    private UUID uuid;
    private String name;
    private Double balance;
    private Double consumptionCount;
    private ArrayList<Group> groups;
    private transient Bitmap profilePicture;

    public Account(Context context, String name, Double balance, Double consumptionCount, ArrayList<Group> groups) {
        this(context, UUID.randomUUID(), name, balance, consumptionCount, groups);
    }

    public Account(Context context, UUID uuid, String name, Double balance, Double consumptionCount, ArrayList<Group> groups) {
        this.uuid = uuid;
        this.name = name;
        this.balance = balance;
        this.consumptionCount = consumptionCount;
        this.groups = groups;
        this.imageDataService = new AccountImageDataService(uuid.toString());
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

    public UUID getUuid() { return this.uuid; }
    public String getName() {
        return this.name;
    }
    public Double getBalance() {
        return this.balance;
    }
    public Double getConsumptionCount() {
        return this.consumptionCount;
    }
    public ArrayList<Group> getGroups() {
        return this.groups;
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
}
