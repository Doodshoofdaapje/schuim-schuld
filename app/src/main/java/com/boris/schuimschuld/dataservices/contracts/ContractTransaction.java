package com.boris.schuimschuld.dataservices.contracts;

import android.provider.BaseColumns;

import com.boris.schuimschuld.dataservices.contracts.ContractAccounts;

public final class ContractTransaction {
    private ContractTransaction() {}

    /* Inner class that defines the table contents */
    public static class TransactionEntry implements BaseColumns {
        public static final String TABLE_NAME = "transactions";
        public static final String ACCOUNT_UUID = "account_uuid";
        public static final String AMOUNT = "amount";
        public static final String DATE = "date";
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TransactionEntry.TABLE_NAME + " (" +
                    TransactionEntry._ID + " INTEGER PRIMARY KEY, " +
                    TransactionEntry.ACCOUNT_UUID + " TEXT, " +
                    TransactionEntry.AMOUNT + " REAL, " +
                    TransactionEntry.DATE + " TEXT, " +
                    "FOREIGN KEY(" + TransactionEntry.ACCOUNT_UUID + ") REFERENCES " +
                    ContractAccounts.AccountEntry.TABLE_NAME + "(" + ContractAccounts.AccountEntry.UUID + ") " +
                    "ON DELETE CASCADE)";

    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TransactionEntry.TABLE_NAME;

}