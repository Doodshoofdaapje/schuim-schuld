package com.boris.schuimschuld.dataservices.managers;

import com.boris.schuimschuld.account.Account;

public interface ITransactionManager {

    void create(Account account, double amount);
    int count(Account account);

}
