package com.boris.schuimschuld.dataservices;

import com.boris.schuimschuld.account.Account;

import java.util.ArrayList;
import java.util.UUID;

public interface IAccountManager {

    void create(Account account);
    Account get(UUID uuid);
    ArrayList<Account> getAll();
    void update(Account account);
    void delete(Account account);

    int size();
}
