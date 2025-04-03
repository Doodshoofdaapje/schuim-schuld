package com.boris.schuimschuld.dataservices.managers;

import android.content.Context;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.dataservices.AccountListDataService;

import java.util.ArrayList;
import java.util.UUID;

public class TransactionManagerJson implements ITransactionManager{

    private AccountListDataService dataService;
    private Context context;

    public TransactionManagerJson(Context context) {
        this.dataService = new AccountListDataService(context);
        this.context = context;
    }

    public void create(Account account, double amount) {
        // Do nothing
    }

    public int count(Account account) {
        return account.getConsumptionCount().intValue();
    }

    public ArrayList<UUID> getHighestCount() {
        return null;
    }
}
