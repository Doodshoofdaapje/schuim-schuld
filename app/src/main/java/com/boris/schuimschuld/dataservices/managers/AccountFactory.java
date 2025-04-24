package com.boris.schuimschuld.dataservices.managers;

import android.content.Context;

import com.boris.schuimschuld.BuildConfig;

public class AccountFactory {

    public static IAccountManager create(Context context) {
        if (BuildConfig.DB_SQL) {
            return new AccountManagerSQL(context);
        } else {
            return new AccountManagerJson(context);
        }
    }
}
