package com.boris.schuimschuld.accountoverview;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.util.PictureUtil;

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

        Bitmap profilePicture = PictureUtil.scalePicture(account.getPicture(), 80, 80).getBitmap();
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
