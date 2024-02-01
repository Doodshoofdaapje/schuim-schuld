package com.boris.bier.accountoverview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.bier.MainActivity;
import com.boris.bier.R;

public class AccountOverviewEdit extends AccountOverviewBase {

    public AccountOverviewEdit(Context context, MainActivity activity, Fragment fragment) {
        super(context, activity, fragment);
    }

    @Override
    protected void configureCard(AccountCard accountCard) {
        double balance = accountCard.getAccount().getBalance();
        if (balance >= 0) {
            accountCard.setColor(R.color.soft_green);
        }
        if (balance < 0) {
            accountCard.setColor(R.color.button_color_orange);
        }
        if (balance < -25) {
            accountCard.setColor(R.color.button_color_red);
        }

        accountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("ACCOUNT_DETAILS", accountCard.getAccount());
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_fragmentEditAccount_to_accountEditFragment, bundle);
            }
        });
    }
}
