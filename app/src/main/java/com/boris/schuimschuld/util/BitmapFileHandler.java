package com.boris.schuimschuld.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapFileHandler {

    private Context context;

    public BitmapFileHandler(Context context) {
        this.context = context;
    }

    public void saveToInternal(Bitmap bitmapImage, String fileName, String directoryName){
        File directory = context.getDir(directoryName, Context.MODE_PRIVATE);
        File file = new File(directory, fileName);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (Exception e) {
            Log.e("BitmapFileHandler", "Failed to save image");
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                Log.e("BitmapFileHandler", "Failed to save image");
                e.printStackTrace();
            }
        }
    }

    public Bitmap loadFromInternal(String fileName, String directoryName) {
        File directory = context.getDir(directoryName, Context.MODE_PRIVATE);
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
