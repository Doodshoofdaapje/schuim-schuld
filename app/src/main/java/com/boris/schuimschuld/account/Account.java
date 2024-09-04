package com.boris.schuimschuld.account;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.util.ImageWriterReader;
import com.boris.schuimschuld.util.SerialBitmap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class Account implements Serializable {
    private String name;
    private Double balance;
    private ArrayList<Group> groups;
    private SerialBitmap profilePicture;

    public Account(String name, Double balance, ArrayList<Group> groups, Context context) {
        this.name = name;
        this.balance = balance;
        this.groups = groups;
        this.profilePicture = loadScaledProfilePicture(context);
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
        profilePicture = scalePicture(picture);
        saveImage(context, picture);
    }

    public void saveImage(Context context, Bitmap picture) {
        ImageWriterReader writerPictures = new ImageWriterReader(context);
        writerPictures.saveToInternal(picture, getName().toLowerCase(Locale.ROOT) + ".jpg");
    }

    public void delete(Context context) {
        File directory = context.getDir("profile_pictures", Context.MODE_PRIVATE);
        new File(directory, getName().toLowerCase() + ".jpg").delete();
    }

    public void pay() {
        this.balance -= 1;
    }

    public SerialBitmap loadProfilePicture(Context context) {
        ImageWriterReader reader = new ImageWriterReader(context);
        Bitmap profilePicture = reader.loadFromInternal(name.toLowerCase() + ".jpg");
        if (profilePicture == null) {
            return new SerialBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.placeholder_pfp));
        } else {
            return new SerialBitmap(profilePicture);
        }
    }

    public SerialBitmap loadScaledProfilePicture(Context context) {
        Bitmap profilePicture = loadProfilePicture(context).getBitmap();
        return scalePicture(profilePicture);
    }

    private SerialBitmap scalePicture(Bitmap bitmap) {
        return new SerialBitmap(Bitmap.createScaledBitmap(bitmap, 80, 80, true));
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
}
