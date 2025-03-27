package com.boris.schuimschuld.services;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.dataservices.IAccountManager;

public class PaymentService {

    private IAccountManager accountManager;

    public PaymentService(IAccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public Boolean chargeAccount(Account account, int amount) {
        if (amount < 0) {
            return false;
        }

        double currentBalance = account.getBalance();
        double newBalance = currentBalance - amount;
        account.setBalance(newBalance);

        double currentConsumptionCount = account.getConsumptionCount();
        double newConsumptionCount = currentConsumptionCount + amount;
        account.setConsumptionCount(newConsumptionCount);

        accountManager.update(account);
        return true;
    }
}
