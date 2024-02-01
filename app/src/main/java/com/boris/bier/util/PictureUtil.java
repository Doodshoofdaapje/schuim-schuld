package com.boris.bier.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.boris.bier.R;

public class PictureUtil {

    public static void roundPicture(ImageView profilePictureView) {
        Bitmap mPicture = ((BitmapDrawable)profilePictureView.getDrawable()).getBitmap();
        Bitmap pictureRounded = Bitmap.createBitmap(mPicture.getWidth(), mPicture.getHeight(), mPicture.getConfig());
        Canvas canvas = new Canvas(pictureRounded);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mPicture, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mPicture.getWidth(), mPicture.getHeight())), 15, 15, mpaint);
        profilePictureView.setImageBitmap(pictureRounded);
    }
}
