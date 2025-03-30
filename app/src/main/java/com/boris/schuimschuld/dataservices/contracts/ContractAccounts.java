package com.boris.schuimschuld.dataservices.contracts;

import android.provider.BaseColumns;

public final class ContractAccounts {
    private ContractAccounts() {}

    /* Inner class that defines the table contents */
    public static class AccountEntry implements BaseColumns {
        public static final String TABLE_NAME = "accounts";
        public static final String UUID = "_id";
        public static final String NAME = "name";
        public static final String BALANCE = "balance";
    }

    public static final String CREATE_TABLE =
        "CREATE TABLE " + AccountEntry.TABLE_NAME + " (" +
            AccountEntry.UUID + " TEXT PRIMARY KEY," +
            AccountEntry.NAME + " TEXT," +
            AccountEntry.BALANCE + " REAL )";

    public static final String DROP_TABLE =
        "DROP TABLE IF EXISTS " + AccountEntry.TABLE_NAME;

}
