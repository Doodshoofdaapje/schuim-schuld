package com.boris.schuimschuld.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageWriterReader {

    private Context context;

    public ImageWriterReader(Context context) {
        this.context = context;
    }

    public void saveToInternal(Bitmap bitmapImage, String fileName){
        File directory = context.getDir("profile_pictures", Context.MODE_PRIVATE);
        File file = new File(directory, fileName);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap loadFromInternal(String fileName) {
        File directory = context.getDir("profile_pictures", Context.MODE_PRIVATE);
        FileInputStream inputStream = null;
        File file = new File(directory, fileName);
        try {
            inputStream = new FileInputStream(file);
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
