package com.boris.bier.accountoverview;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.bier.MainActivity;
import com.boris.bier.account.Account;

import java.io.File;

public class AccountOverviewDelete extends AccountOverviewBase{

    public AccountOverviewDelete(Context context, MainActivity activity, Fragment fragment) {
        super(context, activity, fragment);
    }

    @Override
    protected void configureCard(AccountCard accountCard) {
        accountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlert(accountCard.getAccount());
            }
        });
    }

    private void createAlert(Account account) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Bevestig");
        builder.setMessage("Weet je zeker dat je door wilt gaan?");
        MainActivity activity = this.activity;

        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.accountRegister.deregister(account);
                dialog.dismiss();
                NavHostFragment.findNavController(fragment).popBackStack();
            }
        });

        builder.setNegativeButton("Nee", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
