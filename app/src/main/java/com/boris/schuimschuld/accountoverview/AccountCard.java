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
    private TextView name;
    private ImageView picture;
    private ImageView plusSign;
    private ImageView minusSign;
    private TextView counter;
    private ImageView crown;

    public AccountCard(Context context, String text, Account account) {
        super(context, null, android.R.style.Widget_Button);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout content = (ConstraintLayout) inflater.inflate(R.layout.account_card, null);
        this.addView(content);
        this.setMinimumWidth(300);

        this.account = account;
        this.name = findViewById(R.id.accountCardName);
        this.picture = findViewById(R.id.accountCardImage);
        this.plusSign = findViewById(R.id.accountCardPlus);
        this.minusSign = findViewById(R.id.accountCardMinus);
        this.counter = findViewById(R.id.accountCardCounter);
        this.crown = findViewById(R.id.accountCardCrown);

        Bitmap profilePicture = PictureUtil.scalePicture(account.getPicture(), 80, 80).getBitmap();
        picture.setImageBitmap(profilePicture);
        name.setText(account.getName());

        PictureUtil.roundPicture(picture);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected); // Call the original behavior

        int visibilitySigns = selected ? VISIBLE : INVISIBLE;
        int visibilityPicture = selected ? INVISIBLE : VISIBLE;

        plusSign.setVisibility(visibilitySigns);
        minusSign.setVisibility(visibilitySigns);
        counter.setVisibility(visibilitySigns);
        picture.setVisibility(visibilityPicture);
        crown.setVisibility(visibilityPicture);
    }

    public void assignCrown(int n) {
        switch(n) {
            case 0: crown.setImageResource(R.drawable.crown_gold); break;
            case 1: crown.setImageResource(R.drawable.crown_silver); break;
            case 2: crown.setImageResource(R.drawable.crown_bronze); break;
            default: crown.setImageDrawable(null); break;
        }
    }

    public void setCounter(int value) {
        counter.setText(String.valueOf(value));
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
