package com.boris.schuimschuld.dataservices.managers;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.util.Tuple;

import java.util.ArrayList;
import java.util.UUID;

public interface ITransactionManager {

    void create(Account account, double amount);
    int count(Account account);
    int countPastMonth(Account account);
    Tuple<ArrayList<String>, ArrayList<Integer>> countPerMonth(Account account);
    ArrayList<UUID> getHighestCount();

}
