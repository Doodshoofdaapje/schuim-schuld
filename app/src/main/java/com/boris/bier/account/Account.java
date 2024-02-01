package com.boris.bier.account;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.widget.ImageView;

import com.boris.bier.R;
import com.boris.bier.util.ImageWriterReader;
import com.boris.bier.util.SerialBitmap;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.Locale;

public class Account implements Serializable {
    private String name;
    private Double balance;
    private AgeGroup group;
    private SerialBitmap profilePicture;

    public Account(String name, Double balance, AgeGroup group, Context context) {
        this.name = name;
        this.balance = balance;
        this.group = group;
        this.profilePicture = loadScaledProfilePicture(context);
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setBalance(Double newBalance) {
        this.balance = newBalance;
    }

    public void setGroup(AgeGroup newGroup) {
        this.group = newGroup;
    }

    public void setPicture(Context context, Bitmap picture) {
        profilePicture = scalePicture(context, picture);
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
        return scalePicture(context, profilePicture);
    }

    private SerialBitmap scalePicture(Context context, Bitmap bitmap) {
        return new SerialBitmap(Bitmap.createScaledBitmap(bitmap, 80, 80, true));
    }

    public AgeGroup getGroup() {
        return this.group;
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

    public JSONObject toJson() {
        JSONObject accountDetails = new JSONObject();

        accountDetails.put("name", name);
        accountDetails.put("balance", balance);
        accountDetails.put("group", group.toString());

        return accountDetails;
    }
}
