package com.boris.schuimschuld.dataservices.managers;

import android.content.Context;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.dataservices.AccountListDataService;

import java.util.ArrayList;
import java.util.UUID;

public class AccountManagerJson implements IAccountManager {
    private AccountListDataService dataService;
    private Context context;

    public AccountManagerJson(Context context) {
        this.dataService = new AccountListDataService(context);
        this.context = context;
    }

    public void create(Account account) {
        ArrayList<Account> accounts = dataService.load();
        accounts.add(account);
        dataService.save(accounts);
    }

    public Account get(UUID uuid) {
        ArrayList<Account> accounts = dataService.load();
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (account.getUuid().equals(uuid)) {
                return account;
            }
        }
        return null;
    }

    public ArrayList<Account> getAll() {
        ArrayList<Account> accounts = dataService.load();
        return accounts;
    }

    public void update(Account account) {
        ArrayList<Account> accounts = dataService.load();
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (acc.getUuid().equals(account.getUuid())) {
                accounts.set(i, account);
                break;
            }
        }
        dataService.save(accounts);
    }

    public void delete(Account account) {
        ArrayList<Account> accounts = dataService.load();
        for (int i = accounts.size() - 1; i >= 0; i--) {
            Account acc = accounts.get(i);
            if (acc.getUuid().equals(account.getUuid())) {
                accounts.remove(i);
                account.delete(context); // Move somewhere else in future
                break;
            }
        }
        dataService.save(accounts);
    }

    public int size() {
        ArrayList<Account> accounts = dataService.load();
        return accounts.size();
    }

    public void close() {
        return;
    }
}
