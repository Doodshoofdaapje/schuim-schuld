package com.boris.schuimschuld.dataservices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.util.BitmapFileHandler;
import com.boris.schuimschuld.util.SerialBitmap;

import java.io.File;
import java.io.Serializable;
import java.util.Locale;

public class AccountImageDataService implements Serializable{
    private String fileName;

    private final String IMAGE_DIRECTORY = "profile_pictures";

    public AccountImageDataService(String fileName) {
        this.fileName = fileName.toLowerCase(Locale.ROOT) + ".jpg";
    }

    public void save(Context context, Bitmap picture) {
        BitmapFileHandler writerPictures = new BitmapFileHandler(context);
        writerPictures.saveToInternal(picture, fileName, IMAGE_DIRECTORY);
    }

    public SerialBitmap load(Context context) {
        BitmapFileHandler reader = new BitmapFileHandler(context);
        Bitmap profilePicture = reader.loadFromInternal(fileName, IMAGE_DIRECTORY);
        if (profilePicture == null) {
            return new SerialBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.placeholder_pfp));
        } else {
            return new SerialBitmap(profilePicture);
        }
    }

    public void delete(Context context) {
        File directory = context.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        new File(directory, fileName).delete();
    }
}
