package com.boris.schuimschuld.account;

import android.content.Context;

import com.boris.schuimschuld.dataservices.AccountListDataService;

import java.util.ArrayList;

public class AccountRegister {
    private AccountListDataService dataService;
    private ArrayList<Account> register;
    private Context context;

    public AccountRegister(Context context) {
        this.dataService = new AccountListDataService(context);
        this.register = dataService.load();
        this.context = context;
    }

    public void register(Account account) {
        register.add(account);
        dataService.save(this.register);
    }

    public void deregister(Account account) {
        if (register.contains(account)) {
            register.remove(account);
            account.delete(context);
            dataService.save(this.register);
        }
    }

    public Integer size() {
        return register.size();
    }

    public ArrayList<Account> getAccounts() {
        return new ArrayList<>(this.register);
    }

    public void save() {
        dataService.save(this.register);
    }
}
