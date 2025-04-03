package com.boris.schuimschuld.dataservices.managers;

import com.boris.schuimschuld.account.Account;

import java.util.ArrayList;
import java.util.UUID;

public interface ITransactionManager {

    void create(Account account, double amount);
    int count(Account account);
    ArrayList<UUID> getHighestCount();

}
