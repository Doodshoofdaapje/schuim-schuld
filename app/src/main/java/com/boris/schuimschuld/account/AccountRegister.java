package com.boris.schuimschuld.account;

import android.content.Context;

import com.boris.schuimschuld.dataservices.AccountRegisterDataService;

import java.util.ArrayList;

public class AccountRegister {
    private AccountRegisterDataService dataService;
    private ArrayList<Account> register;
    private Context context;

    public AccountRegister(Context context) {
        this.dataService = new AccountRegisterDataService(context);
        this.register = dataService.load();
        this.context = context;
    }

    public void register(Account account) {
        register.add(account);
        dataService.save(this);
    }

    public void deregister(Account account) {
        if (register.contains(account)) {
            register.remove(account);
            account.delete(context);
            dataService.save(this);
        }
    }

    public Integer size() {
        return register.size();
    }

    public boolean has(Account account) {
        return register.contains(account);
    }

    public ArrayList<Account> getAccounts() {
        return new ArrayList<>(this.register);
    }

    public void save() {
        dataService.save(this);
    }
}
