package com.boris.schuimschuld.dataservices.managers;

import android.content.Context;

import com.boris.schuimschuld.BuildConfig;

public class TransactionFactory {

    public static ITransactionManager create(Context context) {
        if (BuildConfig.DB_SQL) {
            return new TransactionManagerSQL(context);
        } else {
            return new TransactionManagerJson(context);
        }
    }
}
