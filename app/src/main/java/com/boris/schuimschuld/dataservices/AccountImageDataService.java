package com.boris.schuimschuld.dataservices;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.util.BitmapFileHandler;
import com.boris.schuimschuld.util.SerialBitmap;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.Locale;

public class AccountImageDataService implements Serializable{

    private String fileName;
    private final String IMAGE_DIRECTORY = "profile_pictures";

    public AccountImageDataService(String fileName) {
        this.fileName = fileName.toLowerCase(Locale.ROOT) + ".jpeg";
    }

    public void save(Context context, Bitmap picture) {
        BitmapFileHandler writerPictures = new BitmapFileHandler(context);
        writerPictures.saveToInternal(picture, fileName, IMAGE_DIRECTORY);
    }

    public Bitmap load(Context context) {
        BitmapFileHandler reader = new BitmapFileHandler(context);
        Bitmap profilePicture = reader.loadFromInternal(fileName, IMAGE_DIRECTORY);
        if (profilePicture == null) {
            Bitmap placeholder = BitmapFactory.decodeResource(context.getResources(), R.drawable.placeholder_pfp);
            save(context, placeholder);
            return placeholder;
        } else {
            return profilePicture;
        }
    }

    public void delete(Context context) {
        File directory = context.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        new File(directory, fileName).delete();
    }

    public Uri getURI(Context context) {
        File directory = context.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        File file = new File(directory, fileName);

        Uri fileUri = Uri.fromFile(file);
        return fileUri;
    }
}
