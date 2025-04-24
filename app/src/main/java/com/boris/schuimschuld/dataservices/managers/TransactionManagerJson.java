package com.boris.schuimschuld.dataservices.managers;

import android.content.Context;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.dataservices.AccountListDataService;
import com.boris.schuimschuld.util.Tuple;

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

    public int countPastMonth(Account account) { return 0; }

    public Tuple<ArrayList<String>, ArrayList<Integer>> countPerMonth(Account account) { return new Tuple<>(new ArrayList<>(), new ArrayList<>()); }

    public ArrayList<UUID> getHighestCount() {
        return null;
    }
}
