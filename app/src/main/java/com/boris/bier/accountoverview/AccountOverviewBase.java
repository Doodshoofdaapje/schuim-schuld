package com.boris.bier.accountoverview;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.fragment.app.Fragment;

import com.boris.bier.MainActivity;
import com.boris.bier.account.Account;

import java.util.ArrayList;

abstract public class AccountOverviewBase {

    protected Context context;
    protected MainActivity activity;
    protected Fragment fragment;

    public AccountOverviewBase(Context context, MainActivity activity, Fragment fragment) {
        this.context = context;
        this.activity = activity;
        this.fragment = fragment;
    }

    abstract protected void configureCard(AccountCard accountCard);

    public void fillLayout(TableLayout layout, ArrayList<Account> accounts) {
        int accountCounter = 0;
        layout.removeAllViews();
        TableRow tr = new TableRow(context);
        for (Account account : accounts) {
            if (accountCounter % 4 == 0) {
                tr = new TableRow(context);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                layout.addView(tr);
            }

            AccountCard accountCard = new AccountCard(context, account.getName(), account);
            configureCard(accountCard);

            tr.addView(accountCard);

            View spacer = new View(context);
            spacer.setMinimumWidth(25);
            spacer.setMinimumHeight(25);
            tr.addView(spacer);

            accountCounter++;
        }
    }

    public void fillLayout(TableLayout layout) {
        fillLayout(layout, activity.accountRegister.getAccounts());
    }
}
