package com.boris.bier.accountoverview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.boris.bier.R;
import com.boris.bier.account.Account;
import com.boris.bier.util.ImageWriterReader;
import com.boris.bier.util.PictureUtil;

import java.util.Locale;

public class AccountCard extends ConstraintLayout {

    private Account account;

    public AccountCard(Context context, String text, Account account) {
        super(context, null, android.R.style.Widget_Button);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout content = (ConstraintLayout) inflater.inflate(R.layout.account_card, null);
        this.addView(content);
        this.setMinimumWidth(300);

        this.account = account;

        TextView textOutputName = (TextView) findViewById(R.id.textOutputNameaccountCard);
        ImageView imageOutputAccount = (ImageView) findViewById(R.id.imageOutputAccountCard);

        textOutputName.setText(account.getName());

        Bitmap profilePicture = account.getPicture();
        imageOutputAccount.setImageBitmap(profilePicture);

        PictureUtil.roundPicture(imageOutputAccount);
    }

    public void setColor(int index) {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.containerAccountCard);
        layout.setBackgroundResource(index);
    }

    public String getName() {
        return account.getName();
    }

    public Account getAccount() {
        return this.account;
    }
}
