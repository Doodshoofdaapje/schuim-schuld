package com.boris.schuimschuld.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.boris.schuimschuld.R;

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

    public static SerialBitmap scalePicture(Bitmap bitmap, int width, int height) {
        return new SerialBitmap(Bitmap.createScaledBitmap(bitmap, width, height, true));
    }
}
