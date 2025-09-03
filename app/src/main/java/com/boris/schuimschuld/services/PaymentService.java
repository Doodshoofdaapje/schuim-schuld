package com.boris.schuimschuld.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.dataservices.managers.IAccountManager;
import com.boris.schuimschuld.dataservices.managers.ITransactionManager;
import com.boris.schuimschuld.dataservices.managers.TransactionFactory;
import com.boris.schuimschuld.dataservices.managers.TransactionManagerSQL;

public class PaymentService {

    private IAccountManager accountManager;
    private ITransactionManager transactionManagerSQL;
    private double consumptionPrice;

    public PaymentService(Context context, IAccountManager accountManager) {
        this.accountManager = accountManager;
        this.transactionManagerSQL = TransactionFactory.create(context);

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.settings_filename), Context.MODE_PRIVATE);
        String defaultValue = context.getResources().getString(R.string.settings_default_consumption_price);
        String consumptionPriceString = sharedPref.getString(
                context.getResources().getString(R.string.settings_consumption_price), defaultValue);
        consumptionPrice = Double.parseDouble(consumptionPriceString);
    }

    public Boolean chargeAccount(Account account, int amount) {
        if (amount < 0) {
            return false;
        }

        double currentBalance = account.getBalance();
        double newBalance = currentBalance - amount * consumptionPrice;
        account.setBalance(newBalance);

        double currentConsumptionCount = account.getConsumptionCount();
        double newConsumptionCount = currentConsumptionCount + amount;
        account.setConsumptionCount(newConsumptionCount);

        transactionManagerSQL.create(account, amount);
        accountManager.update(account);

        return true;
    }
}
