package com.boris.schuimschuld.services;

import android.content.Context;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.dataservices.managers.IAccountManager;
import com.boris.schuimschuld.dataservices.managers.ITransactionManager;
import com.boris.schuimschuld.dataservices.managers.TransactionFactory;
import com.boris.schuimschuld.dataservices.managers.TransactionManagerSQL;

public class PaymentService {

    private IAccountManager accountManager;
    private ITransactionManager transactionManagerSQL;

    public PaymentService(Context context, IAccountManager accountManager) {
        this.accountManager = accountManager;
        this.transactionManagerSQL = TransactionFactory.create(context);
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

        transactionManagerSQL.create(account, amount);
        accountManager.update(account);

        return true;
    }
}
